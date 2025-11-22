package main

import (
	"fmt"
	"log"
	"net/http"
	mqttclient "server/mqtt-client"
	rabbitmq "server/rabbit-mq"
)

type MessagePayload struct {
	ID      string `json:"id"`
	Content string `json:"content"`
}




func main() {

	ch,  rps:= rabbitmq.SetupRabbitMq()
	defer ch.Close()

	client := mqttclient.NewMQTTClient()
	defer client.Disconnect(250)

    //setup rabbit consumers
    rabbitmq.SetupConsumerService(ch, client)

    //setup mqtt listeners
	mqttclient.SetupMessageListeners(client, rps)
	mqttclient.SetupEventListeners()

	fmt.Println("Webhook server listening on :3000")
	log.Fatal(http.ListenAndServe(":3000", nil))

}
