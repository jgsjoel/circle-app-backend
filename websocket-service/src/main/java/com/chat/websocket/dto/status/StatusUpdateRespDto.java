package com.chat.websocket.dto.status;

import com.chat.websocket.dto.messages.MsgStatRespDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusUpdateRespDto {

    private String updatedById;
    private String origSenderId;
    private MsgStatRespDto msgStatRespDto;

}
