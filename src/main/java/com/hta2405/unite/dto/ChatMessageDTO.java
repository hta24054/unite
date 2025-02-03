package com.hta2405.unite.dto;

import com.hta2405.unite.enums.ChatMessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class ChatMessageDTO {
    private ChatMessageType chatMessageType;
    private Long chatMessageId;
    private Long chatRoomId;
    private String senderId;
    private String chatMessageContent;
    private LocalDateTime chatMessageDate;
    private List<String> userIds;
}
