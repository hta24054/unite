package com.hta2405.unite.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatRoomMember {
    private Long chatRoomMemberId;
    private Long chatRoomId;
    private String userId;
}
