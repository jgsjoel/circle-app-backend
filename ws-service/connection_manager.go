package main

import (
	"log"
	"sync"

	"github.com/gofiber/websocket/v2"
)

type ConnectionManager struct {
	UserConns *sync.Map
}

// NewConnectionManager initializes the manager
func NewConnectionManager() *ConnectionManager {
	return &ConnectionManager{
		UserConns: &sync.Map{},
	}
}

func (m *ConnectionManager) GetUserSessionCount(userID string) int {
	if conns, ok := m.UserConns.Load(userID); ok {
		count := 0
		conns.(*sync.Map).Range(func(key, value interface{}) bool {
			count++
			return true
		})
		return count
	}
	return 0
}

// AddConnection adds a new connection for a specific user.
func (m *ConnectionManager) AddConnection(userID string, c *websocket.Conn) {
	// Load the inner map of connections for this user, or create one if it doesn't exist
	conns, _ := m.UserConns.LoadOrStore(userID, &sync.Map{})

	// Add the new connection to the inner map using its remote address as a unique key
	conns.(*sync.Map).Store(c.RemoteAddr().String(), c)
	log.Printf("User %s connected. Total sessions: %d", userID, m.GetUserSessionCount(userID))
}

// RemoveConnection removes a closed connection for a specific user.
func (m *ConnectionManager) RemoveConnection(userID string, c *websocket.Conn) {
	if conns, ok := m.UserConns.Load(userID); ok {
		// Delete the connection from the inner map
		conns.(*sync.Map).Delete(c.RemoteAddr().String())

		// If the inner map is now empty, remove the user entry
		count := m.GetUserSessionCount(userID)
		if count == 0 {
			m.UserConns.Delete(userID)
			log.Printf("User %s disconnected. No active sessions left.", userID)
		} else {
			log.Printf("User %s session closed. Remaining sessions: %d", userID, count)
		}
	}
}

// SendToUser sends a message to ALL active sessions belonging to the specified user.
func (m *ConnectionManager) SendToUser(userID string, message []byte) {
	if conns, ok := m.UserConns.Load(userID); ok {
		conns.(*sync.Map).Range(func(key, value interface{}) bool {
			conn := value.(*websocket.Conn)

			// Use WriteMessage for sending data
			if err := conn.WriteMessage(websocket.TextMessage, message); err != nil {
				log.Printf("Error sending message to %s (%s): %v", userID, key, err)
				// Note: Handle disconnection cleanup here if desired, but usually done in the reader loop
			}
			return true // continue iteration
		})
	} else {
		log.Printf("User %s is not currently connected.", userID)
	}
}
