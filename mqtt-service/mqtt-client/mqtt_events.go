package mqttclient

import (
	"encoding/json"
	"io"
	"log"
	"net/http"
)

type MqttEvent struct {
	Event       string `json:"event"`
	ClientID    string `json:"clientid"`
	Username    string `json:"username"`
	IPAddress   string `json:"ipaddress"`
	ConnectedAt int64  `json:"connected_at"`
}

// onConnect handles EMQX client connect webhook
func onConnect(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	body, _ := io.ReadAll(r.Body)

	var evt MqttEvent
	if err := json.Unmarshal(body, &evt); err != nil {
		log.Println("Failed to decode EMQX event:", err, "Body:", string(body))
		http.Error(w, "bad request", http.StatusBadRequest)
		return
	}

	log.Printf("[EMQX CONNECT] client=%s user=%s ip=%s", evt.ClientID, evt.Username, evt.IPAddress)
	w.WriteHeader(http.StatusOK)
}

// onDisconnect handles EMQX client disconnect webhook
func onDisconnect(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	body, _ := io.ReadAll(r.Body)

	var evt MqttEvent
	if err := json.Unmarshal(body, &evt); err != nil {
		log.Println("Failed to decode EMQX event:", err, "Body:", string(body))
		http.Error(w, "bad request", http.StatusBadRequest)
		return
	}

	log.Printf("[EMQX DISCONNECT] client=%s user=%s ip=%s", evt.ClientID, evt.Username, evt.IPAddress)
	w.WriteHeader(http.StatusOK)
}

// Register HTTP handlers (does NOT start server)
func RegisterEventHandlers() {
	http.HandleFunc("/on-connect", onConnect)
	http.HandleFunc("/on-disconnect", onDisconnect)
}

// Start server on a given port
func StartHTTPServer(addr string) {
	log.Println("Webhook server listening on", addr)
	log.Fatal(http.ListenAndServe(addr, nil))
}
