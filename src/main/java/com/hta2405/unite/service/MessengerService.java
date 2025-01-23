package com.hta2405.unite.service;


import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.domain.ChatRoom;
import com.hta2405.unite.domain.ChatRoomMember;
import com.hta2405.unite.dto.ChatRoomDTO;
import com.hta2405.unite.enums.ChatMessageType;
import com.hta2405.unite.mybatis.mapper.MessengerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public Map<String, Object> getIdToRoomNameMap(String userId) {
        List<Map<String, Object>> resultList = messengerMapper.getIdToRoomNameMap(userId);

        Map<String, Object> resultMap = new HashMap<>();
        for (Map<String, Object> row : resultList) {
            String chatRoomId = String.valueOf(row.get("chat_room_id"));
            String chatRoomName = (String) row.get("chat_room_name");
            resultMap.put(chatRoomId, chatRoomName);
        }
        return resultMap;
    }

    // ChatRoom 관련
    public List<ChatRoomDTO> getAllChatRooms(String empId) {
        return messengerMapper.findAllRooms(empId);
    }

    public List<ChatMessage> getChatMessageById(Long chatRoomId) {
        return messengerMapper.findMessageListById(chatRoomId);
    }

    public HashMap<String, Object> createChatRoom(List<String> userIds, String empId) {
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
                .chatRoomDefaultName(chatRoomName.toString())
                .creatorId(empId).build();
        try {
            messengerMapper.createRoom(chatRoom);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create chat room", e);
        }

        Boolean check;
        if (!userIds.isEmpty()) {
            check = insertRoomMember(userIds, chatRoom);

            if (check) {
                // DB 저장이 성공한 경우에만 알림 전송
                for (String userId : userIds) {
                    messagingTemplate.convertAndSend("/topic/user/" + userId, chatRoom);
                }
            }
        } else {
            check = true;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatRoomId", chatRoom.getChatRoomId());
        map.put("status", check);
        return map;
    }

    public Boolean insertRoomMember(List<String> userIds, ChatRoom chatRoom) {
        List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
        for (String userId : userIds) {
            ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                    .chatRoomId(chatRoom.getChatRoomId())
                    .userId(userId)
                    .chatRoomName(chatRoom.getChatRoomDefaultName()).build();
            chatRoomMemberList.add(chatRoomMember);
        }

        try {
            // Batch insert 수행
            int insertedRows = messengerMapper.insertRoomMember(chatRoomMemberList);
            return insertedRows > 0; // 삽입된 행이 있다면 true 반환
        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 false 반환
            e.printStackTrace();
            return false;
        }
    }

    public void deleteChatRoom(Long chatRoomId) {
        messengerMapper.deleteRoom(chatRoomId);
    }

    // ChatMessage 관련
    public List<ChatMessage> getMessagesByRoomId(Long chatRoomId) {
        return messengerMapper.findMessagesByRoomId(chatRoomId);
    }

    public void saveMessage(ChatMessage chatMessage) {
        Long chatRoomId = chatMessage.getChatRoomId();

        if (!chatMessage.getChatMessageType().equals(ChatMessageType.LEAVE_ALL)) {
            messengerMapper.saveMessage(chatMessage);
            messengerMapper.updateRecentMessageDate(chatRoomId);

            chatMessage = messengerMapper.findMessageById(chatMessage.getChatMessageId());
        }
        messagingTemplate.convertAndSend("/topic/chatRoom/" + chatRoomId, chatMessage); // 브로드캐스트
    }

    // ChatRoomMember 관련
    public List<String> getMembersByRoomId(Long chatRoomId) {
        return messengerMapper.findMembersByRoomId(chatRoomId);
    }

    public void addMember(ChatRoomMember member) {
        messengerMapper.addMember(member);
    }

    public Boolean removeMember(Long chatRoomId, String userId) {
        try {
            String creator = messengerMapper.findChatRoomById(chatRoomId).getCreatorId();
            int deleteRow;

            System.out.println("creator = " + creator + "userId = " + userId);
            if (creator.equals(userId)) {
                //채팅방장이 삭제시 모두 방 삭제
                deleteChatMessage(chatRoomId);
                deleteRow = deleteMember(chatRoomId);
                deleteChatRoom(chatRoomId);

                ChatMessage chatMessage = ChatMessage.builder()
                        .chatRoomId(chatRoomId)
                        .senderId(userId)
                        .chatMessageType(ChatMessageType.LEAVE_ALL).build();
                saveMessage(chatMessage);
            } else {
                deleteRow = messengerMapper.removeMember(chatRoomId, userId);
                List<String> memberList = messengerMapper.findMembersByRoomId(chatRoomId);

                if (memberList.isEmpty()) { // 채팅방에 아무도 없다면 채팅방 삭제
                    deleteChatMessage(chatRoomId);
                    deleteChatRoom(chatRoomId);
                }

                Map<String, String> empMap = getIdToENameMap();

                // 나가기 메시지 생성
                String messageContent = empMap.get(userId) + "님이 채팅방을 나갔습니다.";

                ChatMessage chatMessage = ChatMessage.builder()
                        .chatRoomId(chatRoomId)
                        .senderId(userId)
                        .chatMessageContent(messageContent)
                        .chatMessageType(ChatMessageType.LEAVE).build();
                saveMessage(chatMessage);
            }
            return deleteRow > 0;
        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 false 반환
            e.printStackTrace();
            return false;
        }
    }

    private void deleteChatMessage(Long chatRoomId) {
        messengerMapper.deleteMessageById(chatRoomId);
    }

    private int deleteMember(Long chatRoomId) {
        return messengerMapper.deleteMember(chatRoomId);
    }

    public int getUnreadMessageCount(Long chatRoomId, String userId) {
        return messengerMapper.getUnreadMessageCount(chatRoomId, userId);
    }

    public void updateLastReadMessageId(Long chatRoomId, String userId) {
        messengerMapper.updateLastReadMessageId(chatRoomId, userId);
    }

    public List<Long> getChatRoomsForUser(String userId) {
        List<ChatRoomMember> chatRoomMemberList = messengerMapper.getChatRoomsById(userId);

        return chatRoomMemberList.stream()
                .map(ChatRoomMember::getChatRoomId)
                .collect(Collectors.toList());
    }

    public ChatRoom getChatRoomById(Long id) {
        return messengerMapper.findChatRoomById(id);
    }

    public Boolean updateChatRoomName(Long chatRoomId, String userId, String chatRoomName) {
        messengerMapper.updateChatRoomName(chatRoomId, userId, chatRoomName);
        return true;
    }
}
