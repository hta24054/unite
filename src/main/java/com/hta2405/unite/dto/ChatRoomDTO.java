package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ChatRoomDTO {
    private Long chatRoomId;
    private String chatRoomName;
    private String latestMessage;
    private LocalDateTime latestMessageDate;
    private Long unreadCount;
}
