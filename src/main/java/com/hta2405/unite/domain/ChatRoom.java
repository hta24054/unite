package com.hta2405.unite.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatRoom {
    private Long chatRoomId;
    private String chatRoomDefaultName;
    private String creatorId;
    private LocalDateTime recentMessageDate;
}
