package rabbitmq

import (
	"log"

	mqtt "github.com/eclipse/paho.mqtt.golang"
	"github.com/rabbitmq/amqp091-go"
)

type ConsumerService struct {
	Channel *amqp091.Channel
	client  mqtt.Client
}

func (cs *ConsumerService) consumeQueue(queueName string) <-chan amqp091.Delivery{

	msgs, err := cs.Channel.Consume(
		queueName, // queue
		"",        // consumer
		false,      // auto-ack
		false,     // exclusive
		false,     // no-local
		false,     // no-wait
		nil,       // args
	)
	failOnError(err, "Failed to register a consumer")
	return msgs

}

func (cs *ConsumerService) readFromMessageResponseQueue() {
	msgs := cs.consumeQueue(MessageResponseQueue)

	go func() {
		for d := range msgs {
			log.Printf("Received a message: %s", d.Body)
		
			// process message here
		
			d.Ack(false) // acknowledge
		}
		
	}()
}

func (cs *ConsumerService) readFromUndeliveredResponseQueue() {
	msgs := cs.consumeQueue(UndeliveredResponseQueue)

	go func() {
		for d := range msgs {
			log.Printf("Received a message: %s", d.Body)
		
			// process message here
		
			d.Ack(false) // acknowledge
		}
		
	}()
}

func (cs *ConsumerService) readFromMessageStatusResponseQueue() {
	msgs := cs.consumeQueue (MessageStatusResponseQueue)
	
	go func() {
		for d := range msgs {
			log.Printf("Received a message: %s", d.Body)
		
			// process message here
		
			d.Ack(false) // acknowledge
		}
		
	}()
}

func SetupConsumerService(ch *amqp091.Channel, mqttClient mqtt.Client) {
	consumer := &ConsumerService{
		Channel: ch,
		client:  mqttClient,
	}
	consumer.readFromMessageResponseQueue()
	consumer.readFromUndeliveredResponseQueue()
	consumer.readFromMessageStatusResponseQueue()

}
