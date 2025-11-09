package main

import (
	"log"
	"net"
	"os"
	"strconv"
	"time"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/gofiber/fiber/v2/middleware/logger"
	"github.com/hudl/fargo"
)

type FCMRequest struct {
	Token string                 `json:"token"`
	Title string                 `json:"title"`
	Body  string                 `json:"body"`
	Data  map[string]interface{} `json:"data,omitempty"`
	Topic string                 `json:"topic,omitempty"`
}

type FCMResponse struct {
	Success bool   `json:"success"`
	Message string `json:"message"`
	Error   string `json:"error,omitempty"`
}

func main() {
	// Get configuration from environment variables
	port := getEnv("SERVER_PORT", "8087")
	serviceName := "fcm-service"
	eurekaURL := getEnv("EUREKA_DEFAULT_ZONE", "http://localhost:8761/eureka")

	// Get local IP address
	localIP := getLocalIP()

	// Create Fiber app
	app := fiber.New(fiber.Config{
		AppName: "FCM Service v1.0",
	})

	// Middleware
	app.Use(cors.New())
	app.Use(logger.New())

	// Health check endpoint
	app.Get("/health", func(c *fiber.Ctx) error {
		return c.JSON(fiber.Map{
			"status":  "UP",
			"service": serviceName,
			"time":    time.Now(),
		})
	})

	// FCM endpoints
	app.Post("/fcm/send", sendNotification)
	app.Post("/fcm/send-to-topic", sendTopicNotification)
	app.Get("/fcm/status", getStatus)

	// Register with Eureka
	go registerWithEureka(serviceName, localIP, port, eurekaURL)

	// Start server
	log.Printf("Starting %s on %s:%s", serviceName, localIP, port)
	log.Fatal(app.Listen(":" + port))
}

func sendNotification(c *fiber.Ctx) error {
	var req FCMRequest
	if err := c.BodyParser(&req); err != nil {
		return c.Status(400).JSON(FCMResponse{
			Success: false,
			Error:   "Invalid request body",
		})
	}

	// Validate required fields
	if req.Token == "" || req.Title == "" || req.Body == "" {
		return c.Status(400).JSON(FCMResponse{
			Success: false,
			Error:   "Token, title, and body are required",
		})
	}

	// TODO: Implement actual FCM sending logic here
	log.Printf("Sending notification to token: %s, title: %s, body: %s", req.Token, req.Title, req.Body)

	return c.JSON(FCMResponse{
		Success: true,
		Message: "Notification sent successfully",
	})
}

func sendTopicNotification(c *fiber.Ctx) error {
	var req FCMRequest
	if err := c.BodyParser(&req); err != nil {
		return c.Status(400).JSON(FCMResponse{
			Success: false,
			Error:   "Invalid request body",
		})
	}

	// Validate required fields
	if req.Topic == "" || req.Title == "" || req.Body == "" {
		return c.Status(400).JSON(FCMResponse{
			Success: false,
			Error:   "Topic, title, and body are required",
		})
	}

	// TODO: Implement actual FCM topic sending logic here
	log.Printf("Sending notification to topic: %s, title: %s, body: %s", req.Topic, req.Title, req.Body)

	return c.JSON(FCMResponse{
		Success: true,
		Message: "Topic notification sent successfully",
	})
}

func getStatus(c *fiber.Ctx) error {
	return c.JSON(fiber.Map{
		"service": "fcm-service",
		"status":  "running",
		"version": "1.0.0",
		"time":    time.Now(),
	})
}

func registerWithEureka(serviceName, ip, port, eurekaURL string) {
	// Parse port to int
	portInt, err := strconv.Atoi(port)
	if err != nil {
		log.Printf("Invalid port: %s", port)
		return
	}

	// Create Eureka connection
	conn := fargo.NewConn(eurekaURL)

	// Create application instance
	instance := &fargo.Instance{
		HostName:         ip,
		Port:             portInt,
		App:              serviceName,
		IPAddr:           ip,
		VipAddress:       serviceName,
		SecureVipAddress: serviceName,
		Status:           fargo.UP,
		DataCenterInfo: fargo.DataCenterInfo{
			Class: "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
			Name:  "MyOwn",
		},
		LeaseInfo: fargo.LeaseInfo{
			RenewalIntervalInSecs: 30,
			DurationInSecs:        90,
		},
		Metadata: fargo.InstanceMetadata{},
	}

	// Register the service
	log.Printf("Registering %s with Eureka at %s", serviceName, eurekaURL)
	err = conn.RegisterInstance(instance)
	if err != nil {
		log.Printf("Failed to register with Eureka: %v", err)
		return
	}

	log.Printf("Successfully registered %s with Eureka", serviceName)

	// Send heartbeats
	ticker := time.NewTicker(30 * time.Second)
	defer ticker.Stop()

	for range ticker.C {
		err := conn.HeartBeatInstance(instance)
		if err != nil {
			log.Printf("Failed to send heartbeat: %v", err)
		} else {
			log.Printf("Heartbeat sent for %s", serviceName)
		}
	}
}

func getLocalIP() string {
	conn, err := net.Dial("udp", "8.8.8.8:80")
	if err != nil {
		log.Printf("Error getting local IP: %v", err)
		return "localhost"
	}
	defer conn.Close()

	localAddr := conn.LocalAddr().(*net.UDPAddr)
	return localAddr.IP.String()
}

func getEnv(key, defaultValue string) string {
	value := os.Getenv(key)
	if value == "" {
		return defaultValue
	}
	return value
}
