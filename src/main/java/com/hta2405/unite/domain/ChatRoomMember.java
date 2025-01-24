package com.hta2405.unite.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatRoomMember {
    private Long chatRoomMemberId;
    private Long chatRoomId;
    private String userId;
    private String chatRoomName;
    private Long lastReadMessageId;
    private LocalDateTime chatRoomJoinedAt;
}
