package com.hta2405.unite.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatRoom {
    private Long chatRoomId;
    private String chatRoomName;
    private String creatorId;
}
