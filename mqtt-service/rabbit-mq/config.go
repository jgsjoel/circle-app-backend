package rabbitmq

import (
	"log"

	"github.com/rabbitmq/amqp091-go"
)

const (
	// Exchanges
	MessageExchange    = "message.exchange"
	FcmMessageExchange = "fcm.message.exchange"
	LastSeenExchange   = "lstSeen.exchange"

	// Queues
	FcmMessageProcessQueue     = "fcm.process"
	MessageProcessQueue        = "message.process"
	MessageResponseQueue       = "message.response"
	UndeliveredRequestQueue    = "undelivered.request"
	UndeliveredResponseQueue   = "undelivered.response"
	MessageStatusResponseQueue = "msgstat.response"
	MessageStatusProcessQueue  = "msgstat.request"
	LastSeenQueue              = "lstSeen.process"

	// Routing keys (for direct exchanges, usually same as queue names)
	RoutingKeyMessageResponse       = "message.response"
	RoutingKeyUndeliveredResponse   = "undelivered.response"
	RoutingKeyMessageStatusResponse = "msgstat.response"

	Address = "amqp://myuser:secret@rabbitmq:5672/"
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Panicf("%s: %s", msg, err)
	}
}

// ConnectToRabbitMq connects and returns a channel
func ConnectToRabbitMq() *amqp091.Channel {
	conn, err := amqp091.Dial(Address)
	failOnError(err, "Failed to connect to RabbitMQ")

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	return ch
}

// DeclareExchange declares a direct exchange
func DeclareExchange(ch *amqp091.Channel, exchangeName string) {
	err := ch.ExchangeDeclare(
		exchangeName, // name
		"direct",     // type
		true,         // durable
		false,        // auto-deleted
		false,        // internal
		false,        // no-wait
		nil,          // arguments
	)
	failOnError(err, "Failed to declare an exchange")
}

// QueueDeclare declares a queue
func QueueDeclare(ch *amqp091.Channel, queueName string) {
	_, err := ch.QueueDeclare(
		queueName, // name
		true,      // durable
		false,     // delete when unused
		false,     // exclusive
		false,     // no-wait
		nil,       // arguments
	)
	failOnError(err, "Failed to declare a queue")
}

// BindQueue binds a queue to an exchange with a routing key
func BindQueue(ch *amqp091.Channel, queueName, exchangeName, routingKey string) {
	err := ch.QueueBind(
		queueName,    // queue name
		routingKey,   // routing key
		exchangeName, // exchange name
		false,        // no-wait
		nil,          // arguments
	)
	failOnError(err, "Failed to bind queue to exchange")
}

func AutoSetupQueues(ch *amqp091.Channel) {
	// Declare exchanges
	DeclareExchange(ch, MessageExchange)
	DeclareExchange(ch, FcmMessageExchange)
	DeclareExchange(ch, LastSeenExchange)

	// Declare queues
	QueueDeclare(ch, FcmMessageProcessQueue)
	QueueDeclare(ch, MessageProcessQueue)
	QueueDeclare(ch, MessageResponseQueue)
	QueueDeclare(ch, UndeliveredRequestQueue)
	QueueDeclare(ch, UndeliveredResponseQueue)
	QueueDeclare(ch, MessageStatusResponseQueue)
	QueueDeclare(ch, MessageStatusProcessQueue)
	QueueDeclare(ch, LastSeenQueue)

	// Bind queues
	BindQueue(ch, MessageResponseQueue, MessageExchange, RoutingKeyMessageResponse)
	BindQueue(ch, UndeliveredResponseQueue, MessageExchange, RoutingKeyUndeliveredResponse)
	BindQueue(ch, MessageStatusResponseQueue, MessageExchange, RoutingKeyMessageStatusResponse)

	// Note: Other queues like process queues may not need binding if they only publish
	log.Println("All exchanges, queues, and bindings are set up")
}

func SetupRabbitMq() (*amqp091.Channel, *PublisherService) {
	ch := ConnectToRabbitMq()

	AutoSetupQueues(ch)

	// 2️⃣ Create publisher and consumer services
	publisher := &PublisherService{Channel: ch}

	return ch, publisher
}
