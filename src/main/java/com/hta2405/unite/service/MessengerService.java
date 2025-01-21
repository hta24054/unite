package com.hta2405.unite.service;


import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.domain.ChatRoom;
import com.hta2405.unite.domain.ChatRoomMember;
import com.hta2405.unite.dto.ChatRoomDTO;
import com.hta2405.unite.mybatis.mapper.EmpMapper;
import com.hta2405.unite.mybatis.mapper.MessengerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessengerService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessengerMapper messengerMapper;
    private final EmpService empService;

    @Autowired
    public MessengerService(SimpMessagingTemplate messagingTemplate, MessengerMapper messengerMapper, EmpService empService) {
        this.messagingTemplate = messagingTemplate;
        this.messengerMapper = messengerMapper;
        this.empService = empService;
    }

    public void sendMessageToChatRoom(Long chatRoomId, ChatMessage message) {
        // 브로드캐스트 메시지 전송
        messagingTemplate.convertAndSend("/topic/" + chatRoomId, message);
    }

    public void sendMessageToUser(String userId, String message) {
        // 특정 사용자에게 1:1 메시지 전송
        messagingTemplate.convertAndSendToUser(userId, "/queue/messages", message);
    }

    public Map<String, String> getIdToENameMap() {
        return empService.getIdToENameMap();
    }

    public Map<String, Object> getIdToRoomNameMap() {
        List<Map<String, Object>> resultList = messengerMapper.getIdToRoomNameMap();

        Map<String, Object> resultMap = new HashMap<>();
        for (Map<String, Object> row : resultList) {
            String chatRoomId = String.valueOf(row.get("chat_room_id"));
            String chatRoomName = (String) row.get("chat_room_name");
            resultMap.put(chatRoomId, chatRoomName);
        }

        System.out.println("resultMap = " + resultMap);
        return resultMap;
    }

    // ChatRoom 관련
    public List<ChatRoomDTO> getAllChatRooms(String empId) {
        return messengerMapper.findAllRooms(empId);
    }

    public List<ChatMessage> getChatRoomById(Long chatRoomId) {
        return messengerMapper.findRoomById(chatRoomId);
    }

    public boolean createChatRoom(List<String> userIds, String empId) {
        if (!Objects.equals(empId, "admin")) {
            userIds.add(empId);
        }

        Map<String, String> empMap = getIdToENameMap();
        StringBuilder chatRoomName = new StringBuilder();

        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.get(i);
            chatRoomName.append(empMap.get(userId));

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
        Long chatMessageId = chatMessage.getChatMessageId();

        chatMessage = messengerMapper.findMessageById(chatMessageId);
        messagingTemplate.convertAndSend("/topic/chatRoom/" + chatMessage.getChatRoomId(), chatMessage); // 브로드캐스트
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

    public int getUnreadMessageCount(Long chatRoomId, String userId) {
        return messengerMapper.getUnreadMessageCount(chatRoomId, userId);
    }

    public void updateLastReadMessageId(Long chatRoomId, String userId) {
        messengerMapper.updateLastReadMessageId(chatRoomId, userId);
    }
}
