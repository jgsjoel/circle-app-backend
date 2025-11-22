package mqttclient

import (
	"encoding/json"
	"fmt"
	rabbitmq "server/rabbit-mq"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

const (
    IncommingMessages        = "message/new"
    StatusUpdate             = "message/update"
)

type Listener struct{
	client mqtt.Client
	publisher *rabbitmq.PublisherService
}

func (l *Listener) subToIncomingMessages() {
    token := l.client.Subscribe(IncommingMessages, 0, func(_ mqtt.Client, msg mqtt.Message) {
        fmt.Println("New incoming message:", string(msg.Payload()))
        var message MessageDto
        if err := json.Unmarshal(msg.Payload(), &message); err != nil {
            fmt.Println("❌ Failed to parse incoming message:", err)
            return
        }
        l.publisher.Send(rabbitmq.MessageExchange, rabbitmq.MessageProcessQueue, message)
        fmt.Printf("✅ Parsed MessageDto: %+v\n", message)
    })
    token.Wait() // wait for subscription to be acknowledged
}

func (l *Listener) subToIncomingStatusUpdates() {
    token := l.client.Subscribe(StatusUpdate, 0, func(_ mqtt.Client, msg mqtt.Message) {
        fmt.Println("New status update:", string(msg.Payload()))
        var statusUpdate StatusDto
        if err := json.Unmarshal(msg.Payload(), &statusUpdate); err != nil {
            fmt.Println("❌ Failed to parse status update:", err)
            return
        }
        l.publisher.Send(rabbitmq.MessageExchange, rabbitmq.MessageStatusProcessQueue, statusUpdate)
    })
    token.Wait()
}

func SetupMessageListeners(client mqtt.Client, publisher *rabbitmq.PublisherService) {
    listener := &Listener{client: client, publisher: publisher}

    // Run subscriptions concurrently if desired
    go listener.subToIncomingMessages()
    go listener.subToIncomingStatusUpdates()
}
