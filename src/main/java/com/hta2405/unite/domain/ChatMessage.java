package com.hta2405.unite.domain;

import com.hta2405.unite.enums.ChatMessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatMessage {
    private ChatMessageType chatMessageType;
    private Long chatMessageId;
    private Long chatRoomId;
    private String senderId;
    private String chatMessageContent;
    private LocalDateTime chatMessageDate;
}
