package com.hta2405.unite.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatMessage {
    private Long chatMessageId;
    private Long chatRoomId;
    private String senderId;
    private String chatMessageContent;
    private LocalDateTime chatMessageDate;
}
