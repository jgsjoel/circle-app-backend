package mqttclient

import (
	mqtt "github.com/eclipse/paho.mqtt.golang"
)

const (
	Broker = "tcp://emqx-service:1883"
	ClientID = "go_mqtt_client"
)

func NewMQTTClient() mqtt.Client{
	opts := mqtt.NewClientOptions()
	opts.AddBroker(Broker)
	opts.SetClientID(ClientID)
	opts.SetAutoReconnect(true)

	client := mqtt.NewClient(opts)

	if token := client.Connect(); token.Wait() && token.Error() != nil {
		panic(token.Error())
	}
	return client
}
