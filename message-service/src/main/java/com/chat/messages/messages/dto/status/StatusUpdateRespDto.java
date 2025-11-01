package com.chat.messages.messages.dto.status;

import com.chat.messages.messages.dto.messages.MsgStatRespDto;
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
