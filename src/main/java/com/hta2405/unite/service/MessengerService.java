package com.hta2405.unite.service;


import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.domain.ChatRoom;
import com.hta2405.unite.domain.ChatRoomMember;
import com.hta2405.unite.mybatis.mapper.MessengerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MessengerService {
    private final MessengerMapper messengerMapper;

    @Autowired
    public MessengerService(MessengerMapper messengerMapper) {
        this.messengerMapper = messengerMapper;
    }

    // ChatRoom 관련
    public List<ChatRoom> getAllChatRooms() {
        return messengerMapper.findAllRooms();
    }

    public ChatRoom getChatRoomById(Long chatRoomId) {
        return messengerMapper.findRoomById(chatRoomId);
    }

    public boolean createChatRoom(List<String> userIds, String empId) {
        if (!Objects.equals(empId, "admin")) {
            userIds.add(empId);
        }

        StringBuilder chatRoomName = new StringBuilder();

        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.get(i);
            chatRoomName.append(userId);

            // 마지막 요소가 아니면 ',' 추가
            if (i < userIds.size() - 1) {
                chatRoomName.append(",");
            }

            if (chatRoomName.length() > 30) {
                chatRoomName = new StringBuilder(chatRoomName.substring(0, 30));
                break;
            }
        }

        //userIds 비어있다는 것은 admin 혼자 채팅방에 있을 경우
        if (chatRoomName.toString().isEmpty()) {
            chatRoomName.append("admin");
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(chatRoomName.toString())
                .creatorId(empId).build();
        messengerMapper.createRoom(chatRoom);

        if (!userIds.isEmpty()) {
            return insertRoomMember(userIds, chatRoom.getChatRoomId());
        }
        return true;
    }

    public Boolean insertRoomMember(List<String> userIds, Long chatRoomId) {
        List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
        for (String userId : userIds) {
            ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                    .chatRoomId(chatRoomId)
                    .userId(userId).build();
            chatRoomMemberList.add(chatRoomMember);
        }
        return messengerMapper.insertRoomMember(chatRoomMemberList) > 0;
    }

    public void deleteChatRoom(Long chatRoomId) {
        messengerMapper.deleteRoom(chatRoomId);
    }

    // ChatMessage 관련
    public List<ChatMessage> getMessagesByRoomId(Long chatRoomId) {
        return messengerMapper.findMessagesByRoomId(chatRoomId);
    }

    public void saveMessage(ChatMessage chatMessage) {
        messengerMapper.saveMessage(chatMessage);
    }

    // ChatRoomMember 관련
    public List<String> getMembersByRoomId(Long chatRoomId) {
        return messengerMapper.findMembersByRoomId(chatRoomId);
    }

    public void addMember(ChatRoomMember member) {
        messengerMapper.addMember(member);
    }

    public void removeMember(Long chatRoomMemberId) {
        messengerMapper.removeMember(chatRoomMemberId);
    }
}
