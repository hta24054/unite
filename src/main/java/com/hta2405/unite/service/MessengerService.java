package com.hta2405.unite.service;


import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.domain.ChatRoom;
import com.hta2405.unite.domain.ChatRoomMember;
import com.hta2405.unite.mybatis.mapper.MessengerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void createChatRoom(ChatRoom chatRoom) {
        messengerMapper.createRoom(chatRoom);
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
