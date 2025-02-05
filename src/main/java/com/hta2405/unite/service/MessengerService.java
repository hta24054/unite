package com.hta2405.unite.service;


import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.domain.ChatRoom;
import com.hta2405.unite.domain.ChatRoomMember;
import com.hta2405.unite.dto.ChatMessageDTO;
import com.hta2405.unite.dto.ChatRoomDTO;
import com.hta2405.unite.enums.ChatMessageType;
import com.hta2405.unite.mybatis.mapper.MessengerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    public List<ChatRoomDTO> getAllChatRooms(String empId, boolean isHomeMessenger) {
        return messengerMapper.findAllRooms(empId, isHomeMessenger);
    }

    public List<ChatMessage> getChatMessageById(Long chatRoomId, String userId) {
        return messengerMapper.findMessageListById(chatRoomId, userId);
    }

    public HashMap<String, Object> createChatRoom(List<String> userIds, String chatRoomName, String empId) {
        userIds.add(empId);
        Map<String, String> empMap = getIdToENameMap();

        if(chatRoomName.isEmpty()) {
            StringBuilder chatRoomNameBuilder = new StringBuilder();
            for (int i = 0; i < userIds.size(); i++) {
                String userId = userIds.get(i);
                chatRoomNameBuilder.append(empMap.get(userId));

                // 마지막 요소가 아니면 ',' 추가
                if (i < userIds.size() - 1) {
                    chatRoomNameBuilder.append(",");
                }

                if (chatRoomNameBuilder.length() > 30) {
                    chatRoomNameBuilder = new StringBuilder(chatRoomNameBuilder.substring(0, 30));
                    break;
                }
            }
            chatRoomName = chatRoomNameBuilder.toString();
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomDefaultName(chatRoomName)
                .creatorId(empId).build();
        try {
            messengerMapper.createRoom(chatRoom);
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create chat room", e);
        }

        Boolean check;
        if (!userIds.isEmpty()) {
            //새롭게 추가되는 멤버가 아니기 때문에 false
            check = insertRoomMember(userIds, chatRoom, null, false);

            if (check) {
                // DB 저장이 성공한 경우에만 알림 전송
                for (String userId : userIds) {
                    messagingTemplate.convertAndSend("/topic/user/" + userId, chatRoom);
                }
                userIds.remove(empId);
                inviteMessage(chatRoom, userIds, empId);
            }
        } else {
            check = true;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatRoomId", chatRoom.getChatRoomId());
        map.put("status", check);
        return map;
    }

    public Boolean insertRoomMember(List<String> userIds, ChatRoom chatRoom, Long lastReadMessageId, boolean check) {
        List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
        for (String userId : userIds) {
            ChatRoomMember.ChatRoomMemberBuilder chatRoomMemberBuilder = ChatRoomMember.builder()
                    .chatRoomId(chatRoom.getChatRoomId())
                    .userId(userId)
                    .chatRoomName(chatRoom.getChatRoomDefaultName());

            if (check) {
                chatRoomMemberBuilder.lastReadMessageId(lastReadMessageId);
            }

            chatRoomMemberList.add(chatRoomMemberBuilder.build());
        }

        try {
            int insertedRows = messengerMapper.insertRoomMember(chatRoomMemberList, check);
            return insertedRows > 0; // 삽입된 행이 있다면 true 반환
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage(), e);
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

    public void saveMessage(ChatMessageDTO chatMessageDTO) {
        Long chatRoomId = chatMessageDTO.getChatRoomId();

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoomId(chatMessageDTO.getChatRoomId())
                .senderId(chatMessageDTO.getSenderId())
                .chatMessageContent(chatMessageDTO.getChatMessageContent())
                .chatMessageType(chatMessageDTO.getChatMessageType()).build();

        if (!chatMessageDTO.getChatMessageType().equals(ChatMessageType.LEAVE_ALL)) {
            messengerMapper.saveMessage(chatMessage);
            messengerMapper.updateRecentMessageDate(chatRoomId);

            chatMessage = messengerMapper.findMessageById(chatMessage.getChatMessageId()); // 메시지 시간을 가져옴
        }
        chatMessageDTO.setChatMessageId(chatMessage.getChatMessageId());
        chatMessageDTO.setChatMessageDate(chatMessage.getChatMessageDate());
        messagingTemplate.convertAndSend("/topic/chatRoom/" + chatRoomId, chatMessageDTO); // 브로드캐스트
    }

    // ChatRoomMember 관련
    public List<String> getMembersByRoomId(Long chatRoomId) {
        return messengerMapper.findMembersByRoomId(chatRoomId);
    }

    public void inviteMessage(ChatRoom chatRoom, List<String> userIds, String empId) {
        Map<String, String> empMap = getIdToENameMap();
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.get(i);
            message.append(empMap.get(userId));

            // 마지막 요소가 아니면 ', ' 추가
            if (i < userIds.size() - 1) {
                message.append("님, ");
            }
        }

        String messageContent = empMap.get(empId) + "님이 " + message + "님을 초대했습니다.";

        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .senderId(empId)
                .chatMessageContent(messageContent)
                .chatMessageType(ChatMessageType.INVITE)
                .userIds(userIds).build();

        saveMessage(chatMessageDTO);
    }

    public Boolean removeMember(Long chatRoomId, String userId) {
        try {
            String creator = messengerMapper.findChatRoomById(chatRoomId).getCreatorId();
            int deleteRow;

            if (creator.equals(userId)) {
                //채팅방장이 삭제시 모두 방 삭제
                deleteChatMessage(chatRoomId);
                deleteRow = deleteMember(chatRoomId);
                deleteChatRoom(chatRoomId);

                ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                        .chatRoomId(chatRoomId)
                        .senderId(userId)
                        .chatMessageType(ChatMessageType.LEAVE_ALL).build();
                saveMessage(chatMessageDTO);
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

                ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                        .chatRoomId(chatRoomId)
                        .senderId(userId)
                        .chatMessageContent(messageContent)
                        .chatMessageType(ChatMessageType.LEAVE).build();
                saveMessage(chatMessageDTO);
            }
            return deleteRow > 0;
        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 false 반환
            log.error("An error occurred: {}", e.getMessage(), e);
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

    public Boolean addChatRoomMember(Long chatRoomId, List<String> userIds, String empId) {
        ChatRoom chatRoom = messengerMapper.findChatRoomById(chatRoomId);
        Long lastReadMessageId = messengerMapper.findLastMessageByRoomId(chatRoomId).getChatMessageId();

        if (!userIds.isEmpty()) {
            Boolean check = insertRoomMember(userIds, chatRoom, lastReadMessageId, true);

            if (check) {
                // DB 저장이 성공한 경우에만 알림 전송
                for (String userId : userIds) {
                    messagingTemplate.convertAndSend("/topic/user/" + userId, chatRoom);
                }
                inviteMessage(chatRoom, userIds, empId);
            }

            return check;
        } else {
            return true;
        }
    }
}
