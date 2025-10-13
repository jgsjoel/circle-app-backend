package main

import (
	"log"
	"time"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/recover"
	"github.com/gofiber/websocket/v2"
)

var connManager *ConnectionManager

func main() {
	connManager = NewConnectionManager()
	app := fiber.New()

	app.Use(recover.New())

	app.Use("/ws", func(c *fiber.Ctx) error {
		if websocket.IsWebSocketUpgrade(c) {
			userID := "user-123"
			c.Locals("userID", userID)
			return c.Next()
		}
		return fiber.ErrUpgradeRequired
	})

	app.Get("/ws/:userID", websocket.New(handleWebSocket))

	log.Fatal(app.Listen(":3000"))
}

func handleWebSocket(c *websocket.Conn) {
	userID := c.Locals("userID").(string)

	connManager.AddConnection(userID, c)

	defer func() {
		connManager.RemoveConnection(userID, c)
		c.Close()
	}()

	for {
		mt, msg, err := c.ReadMessage()
		if err != nil {
			log.Printf("User %s read error: %v", userID, err)
		}

		if mt == websocket.TextMessage {
			log.Printf("User %s received: %s", userID, msg)

			if err := c.WriteMessage(websocket.TextMessage, []byte("Server received: "+string(msg))); err != nil {
				log.Printf("Write error: %v", err)
				break
			}
		}
	}
}

// -----------------------------------------------------------
// --- REST API Logic ---
// -----------------------------------------------------------

// handleSendMessage is an example endpoint to publish a message via HTTP
// to all active WebSocket sessions of a target user.
func handleSendMessage(c *fiber.Ctx) error {
	targetUserID := c.Params("userID")
	var data map[string]string

	// Parse the JSON body
	if err := c.BodyParser(&data); err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{"error": "Invalid JSON"})
	}

	message := data["message"]

	// Send the message using the connection manager
	go connManager.SendToUser(targetUserID, []byte(message))

	// Simulate some latency for the response
	time.Sleep(100 * time.Millisecond)

	return c.Status(fiber.StatusOK).JSON(fiber.Map{
		"message":    "Message queued for delivery.",
		"recipients": targetUserID,
		"sessions":   connManager.GetUserSessionCount(targetUserID),
	})
}
