package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ChatRoomRequestDTO {
    private List<String> userIds;  // 사용자 ID 목록
    private String roomName;       // 방 이름
}
