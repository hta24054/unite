package com.hta2405.unite.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatMessageType {
    NORMAL,   // 일반 메시지
    INVITE,   // 초대 메시지
    LEAVE,    // 나간 메시지
    LEAVE_ALL // 모두 나간 메시지
}