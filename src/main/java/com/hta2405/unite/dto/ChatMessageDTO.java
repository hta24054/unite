package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ChatMessageDTO {
    private int messageType;
    private Long chatMessageId;
    private Long chatRoomId;
    private String senderId;
    private String chatMessageContent;
    private LocalDateTime chatMessageDate;
}
