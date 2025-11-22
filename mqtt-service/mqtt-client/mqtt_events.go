package mqttclient

import (
	"encoding/json"
	"log"
	"net/http"
)

type MqttEvent struct {
    Event      string `json:"event"`
    ClientID   string `json:"clientid"`
    Username   string `json:"username"`
    IPAddress  string `json:"ipaddress"`
    ConnectedAt int64 `json:"connected_at"`
}


func onConnect(w http.ResponseWriter, r *http.Request) {
    var evt MqttEvent

    if err := json.NewDecoder(r.Body).Decode(&evt); err != nil {
        log.Println("Failed to decode EMQX event:", err)
        http.Error(w, "bad request", http.StatusBadRequest)
        return
    }

    // Handle event
    log.Printf("New MQTT client connected: %s (user=%s, ip=%s)", evt.ClientID, evt.Username, evt.IPAddress)

    // Example: update database, publish to RabbitMQ, etc.
    // deviceService.MarkOnline(evt.ClientID)

    // Return 200 to EMQX
    w.WriteHeader(http.StatusOK)
}

func onDisconnect(w http.ResponseWriter, r *http.Request) {
    var evt MqttEvent

    if err := json.NewDecoder(r.Body).Decode(&evt); err != nil {
        log.Println("Failed to decode EMQX event:", err)
        http.Error(w, "bad request", http.StatusBadRequest)
        return
    }

    // Handle event
    log.Printf("MQTT client disconnected: %s (user=%s, ip=%s)", evt.ClientID, evt.Username, evt.IPAddress)

    // Example: update database, publish to RabbitMQ, etc.
    // deviceService.MarkOnline(evt.ClientID)

    // Return 200 to EMQX
    w.WriteHeader(http.StatusOK)
}

func SetupEventListeners(){
	http.HandleFunc("/on-connect", onConnect)
	http.HandleFunc("/on-disconnect", onDisconnect)
}