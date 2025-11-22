package main

import (
	"server/mqtt-client"
	"server/rabbit-mq"
)

func main() {
	// Setup RabbitMQ
	ch, rps := rabbitmq.SetupRabbitMq()
	defer ch.Close()

	// Setup MQTT client
	client := mqttclient.NewMQTTClient()
	defer client.Disconnect(250)

	// Setup RabbitMQ consumers
	rabbitmq.SetupConsumerService(ch, client)

	// Setup MQTT message listeners
	mqttclient.SetupMessageListeners(client, rps)

	// Register webhook HTTP handlers
	mqttclient.RegisterEventHandlers()

	// Start webhook server (blocking)
	mqttclient.StartHTTPServer(":3000")
}
