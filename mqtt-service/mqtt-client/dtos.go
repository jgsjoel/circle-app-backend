package mqttclient


type MessageStatus string

const (
    StatusSent     MessageStatus = "SENT"
    StatusReceived MessageStatus = "RECEIVED"
    StatusRead     MessageStatus = "READ"
)

type MediaDto struct {
    URL      string `json:"url"`
    PublicID string `json:"public_id"`
}

type MessageDto struct {
    MessageID       string     `json:"message_id"`        // required (like @NotBlank)
    Message         string     `json:"message"`
    ChatID          string     `json:"chat_id"`
    SenderID        string     `json:"sender_id"`         // required
    ReceiverID      string     `json:"receiver_id"`       // required
    SenderTimestamp string     `json:"sender_timestamp"`  // required
    MessageType     string     `json:"message_type"`      // required
    MediaList       []MediaDto `json:"media_list,omitempty"`
}

type StatusDto struct {
    MessageID   string        `json:"message_id"`
    UpdatedByID string        `json:"updated_by_id"`
    Status      MessageStatus `json:"status"`
}