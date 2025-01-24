let stompClient;
let subscriptions = {};
let empMap;

// WebSocket 연결
function connectWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        console.log("Connected to WebSocket-Messenger");

        const userId = $('.messenger-header').data('sender-id');
        subscribeToInvitations(userId);

        // 초기 채팅방 구독
        initializeChatRoomSubscriptions(userId);
    });
}

// 사용자가 초대된 모든 채팅방을 구독
function initializeChatRoomSubscriptions(userId) {
    // AJAX 요청으로 사용자가 초대된 모든 채팅방 ID 가져오기
    $.ajax({
        url: `/api/messenger/chatRooms/${userId}`,
        type: 'GET',
        success: function (chatRooms) {
            chatRooms.forEach(chatRoomId => {
                console.log("1");
                joinChatRoom(chatRoomId); // 각 채팅방 구독
            });
        },
        error: function (error) {
            console.error("Failed to fetch chat rooms:", error);
        }
    });
}

// 초대 알림 구독
function subscribeToInvitations(userId) {
    console.log("login subscribe ok")
    stompClient.subscribe(`/topic/user/${userId}`, (message) => {
        let chatRoomId = $('.messenger-room-active').data('chat-room');
        const notification = JSON.parse(message.body);
        console.log(`New chat room invitation: ${notification.chatRoomId}`);

        console.log("2");
        joinChatRoom(notification.chatRoomId).then(() => {
            loadChatRoomList()
                .then(() => {
                    // `loadChatRoomList` 실행 완료 후 실행
                    let chatRoom = $('#chatRoom-' + chatRoomId);
                    chatRoom.addClass('messenger-room-active');
                    chatRoom.find('.messenger-chat').addClass('messenger-chat-active');
                    openChatRoom(chatRoomId);
                    console.log("초대당함");
                })
            console.log("초대")
        });

    });
}

// 특정 채팅방 구독
function joinChatRoom(chatRoomId) {
    return new Promise((resolve, reject) => {
        if (subscriptions[chatRoomId]) {
            console.log(`Already subscribed to chatRoom ${chatRoomId}`);
            return;
        }
        console.log(`subscribing to chatRoom ${chatRoomId}`);

        subscriptions[chatRoomId] = stompClient.subscribe(`/topic/chatRoom/${chatRoomId}`, (message) => {
            const chatMessage = JSON.parse(message.body);
            let chatRoomId = $('.messenger-room-active').data('chat-room');
            let userId = $('.messenger-header').data('sender-id'); // 현재 사용자 ID

            console.log("chatMessage = ", chatMessage);
            console.log("chatMessageType = ", chatMessage.chatMessageType);

            if (chatMessage.chatMessageType === 'NORMAL') {
                if (chatMessage.chatRoomId === chatRoomId) {
                    // 자신이 보낸 메시지인지 확인
                    const isMyMessage = chatMessage.senderId === String(userId);

                    // 추가된 메시지 업데이트
                    displayMessage(chatMessage, isMyMessage);
                }

                // 채팅방 목록 업데이트
                updateChatRoomList(chatMessage);

                // 읽지 않은 메시지 개수 갱신
                updateUnreadCount(chatMessage.chatRoomId, chatMessage.senderId);
            } else if (chatMessage.chatMessageType === 'INVITE' || chatMessage.chatMessageType === 'LEAVE') {
                if (chatMessage.chatRoomId === $('.messenger-room-active').data('chat-room')) {
                    displayStateMessage(chatMessage);
                }
            } else if (chatMessage.chatMessageType === 'LEAVE_ALL') {
                displayRemoveChatRoom(chatMessage);
            }
        });

        console.log(`Subscribed to chatRoom ${chatRoomId}`);
        resolve(); // 구독 완료 후 Promise 해결
    });
}

function updateChatRoomList(chatMessage) {
    let chatRoomElement = $(`#chatRoom-${chatMessage.chatRoomId}`);

    // 마지막 메시지와 날짜 업데이트
    chatRoomElement.find('.messenger-last-message').text(chatMessage.chatMessageContent);
    let formattedDate = formattedLocalDate(chatMessage.chatMessageDate);
    chatRoomElement.find('.messenger-last-message-date').text(formattedDate);

    // chatRoomElement를 messenger-body의 맨 앞으로 이동
    chatRoomElement.prependTo('.messenger-body');
}

function updateUnreadCount(chatRoomId, senderId) {
    let userId = $('.messenger-header').data('sender-id'); // 현재 사용자 ID
    let chatRoomElement = $(`#chatRoom-${chatRoomId}`);
    let unreadCountElement = chatRoomElement.find('.messenger-unread-message-count');
    let activeChatRoomId = $('.messenger-room-active').data('chat-room');

    if (activeChatRoomId === chatRoomId) {
        // 읽음 상태로 처리 (서버에 마지막 읽은 메시지 ID 갱신)
        markMessagesAsRead(chatRoomId);
    }

    // 메시지 보낸 사람이 본인이면 읽지 않은 메시지 계산 스킵
    if (senderId === String(userId) || activeChatRoomId === chatRoomId) {
        console.log("skip")
        unreadCountElement.hide();
        return;
    }
    console.log("no skip")

    // AJAX 요청으로 읽지 않은 메시지 개수 가져오기
    $.ajax({
        url: `/api/messenger/chatRoom/${chatRoomId}/unreadCount/${userId}`,
        type: 'GET',
        success: function (count) {
            let activeChatRoomId = $('messenger-room-active').data('chat-room');

            if (activeChatRoomId === chatRoomId) {
                unreadCountElement.hide();
            } else {
                unreadCountElement.text(count).show();
            }
        }
    });
}

function openChatRoom(chatRoomId) {
    let chatRoomElement = $(`#chatRoom-${chatRoomId}`);
    let unreadCountElement = chatRoomElement.find('.messenger-unread-message-count');
    unreadCountElement.hide();

    if (chatRoomElement.find('.messenger-last-message-date').text() !== '') {
        // 읽음 상태로 처리 (서버에 마지막 읽은 메시지 ID 갱신)
        markMessagesAsRead(chatRoomId);
    }

    // 채팅방 메세지 리스트 로드
    loadChatMessageList(chatRoomId);

    // joinChatRoom(chatRoomId);
}

// 서버에 읽음 상태 전송
function markMessagesAsRead(chatRoomId) {
    const userId = $('.messenger-header').data('sender-id'); // 현재 사용자 ID

    $.ajax({
        url: `/api/messenger/chatRoom/${chatRoomId}/read/${userId}`,
        type: 'POST',
        success: function () {
            console.log(`Marked messages as read for chatRoom ${chatRoomId}`);
        },
        error: function (error) {
            console.error("Failed to mark messages as read:", error);
        }
    });
}

// 특정 채팅방 구독 해제
function leaveChatRoom(chatRoomId) {
    return new Promise((resolve, reject) => {
        console.log("leave try")

        if (subscriptions[chatRoomId]) {
            subscriptions[chatRoomId].unsubscribe(); // 구독 해제
            delete subscriptions[chatRoomId]; // 구독 정보 삭제
            console.log(`Unsubscribed from chatRoom ${chatRoomId}`);
        } else {
            console.log(`No active subscription for chatRoom ${chatRoomId}`);
        }
        resolve();
    });
}

function displayStateMessage(chatMessage) {
    const $messengerContentBody = $('.messenger-content-body');
    let html = `
                <div class="text-center standardDay">
                    <div class="leaveMessage">${chatMessage.chatMessageContent}</div>
                </div>`;


    if (chatMessage.chatMessageType === 'LEAVE') {
        console.log('content leave= ' + chatMessage.chatMessageContent)
        $('.member-' + chatMessage.senderId).remove();
    } else if (chatMessage.chatMessageType === 'INVITE') {
        console.log('content invite= ' + chatMessage.chatMessageContent)
        html += ``;

        if (empMap !== undefined) {
            let memberHtml = ``;
            chatMessage.userIds.forEach((userId) => {
                memberHtml += `
                            <div class="member member-${userId}" data-user-id="${userId}">
                                <img class="member-img" src="/api/emp/profile-image?empId=${userId}" alt="프로필" style="width: 30px;height: 30px;">
                                <span>${empMap[userId]}</span>
                            </div>`;
            })
            $('.btn-invite').before(memberHtml);
        }

    }
    $messengerContentBody.append(html);

    //스크롤바 하단으로 조정
    $messengerContentBody.scrollTop($messengerContentBody.prop("scrollHeight"));
}

//채팅방 삭제
function displayRemoveChatRoom(chatMessage) {
    leaveChatRoom(chatMessage.chatRoomId).then(() => {
        location.href = "/messenger";
    });
}

// 메시지 출력
function displayMessage(chatMessage, isMyMessage) {
    const $messengerContentBody = $('.messenger-content-body');
    let creatorId = $('.bi-star-fill').parent().find('span').text(); // 생성자 ID
    let invitedUserMessage = creatorId + '님이'; // 초대 메시지 시작 부분
    let userIds = [creatorId]; // 배열로 초기화
    $('.member').each(function () {
        let memberId = $(this).find('span').text(); // 각 멤버의 ID 추출
        if (memberId) {
            userIds.push(memberId); // userIds 배열에 추가
        }
    });
    for (const userId of userIds) {
        if (creatorId !== userId) {
            invitedUserMessage += '님, ' + userId;
        }
    }
    let index = invitedUserMessage.indexOf('님,');
    if (index !== -1) {
        invitedUserMessage = invitedUserMessage.substring(0, index) + invitedUserMessage.substring(index + 2);
    }
    invitedUserMessage += '님을 채팅방에 초대했습니다.'
    console.log("invitedUserMessage : ", invitedUserMessage)

    console.log("date=" + chatMessage.chatMessageDate)
    let formattedDate = new Date(chatMessage.chatMessageDate).toLocaleString().substring(12, 20);

    let formattedDated = new Date(chatMessage.chatMessageDate).toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long', // 요일 추가
    });

    let standardDay = String($('.standardDay-content').last().text());
    let check = $('.inviteMessage').text() === "";
    let html = '';

    console.log("check=", check)
    console.log("$('.inviteMessage').text()=", $('.inviteMessage').text())

    if (standardDay !== formattedDated) {
        html += `
                <div class="text-center standardDay">
                    <div class="standardDay-content">${formattedDated}</div>`;
        if (check) {
            html += `<div class="inviteMessage">${invitedUserMessage}</div>`;
        }
        html += `</div>`;
    }

    if (isMyMessage) {
        html += `
                <div class="messenger-message" style="flex-direction: row-reverse;">
                    <div class="messenger-message-middle">
                        <div class="messenger-message-text">${chatMessage.chatMessageContent}</div>
                    </div>
                    <div class="messenger-message-end" style="margin:0 5px;">${formattedDate}</div>
                </div>`;
    } else {
        html += `
                <div class="messenger-message">
                    <div class="messenger-message-start">
                        <div class="messenger-sender-img-box">
                            <img class="messenger-sender-img" src="/api/emp/profile-image?empId=${chatMessage.senderId}" alt="프로필">
                        </div>
                    </div>
                    <div class="messenger-message-middle">
                        <div class="messenger-sender-name">${empMap[chatMessage.senderId]}</div>
                        <div class="messenger-message-text">${chatMessage.chatMessageContent}</div>
                    </div>
                    <div class="messenger-message-end">${formattedDate}</div>
                </div>`;
    }
    $messengerContentBody.append(html);

    //스크롤바 하단으로 조정
    $messengerContentBody.scrollTop($messengerContentBody.prop("scrollHeight"));
}

// 메시지 전송
function sendMessage(chatMessageType, chatRoomId, senderId, content) {
    stompClient.send(`/app/chatRoom/${chatRoomId}`, {}, JSON.stringify({
        chatMessageType: chatMessageType,
        chatRoomId: chatRoomId,
        senderId: senderId,
        chatMessageContent: content
    }));
}

function addUser(name, empId) {
    // 새로운 태그 생성
    const $tag = $(`<div class="user-tag">${name}<span class="remove">×</span></div>`);

    // 삭제 버튼 클릭 이벤트 추가
    $tag.find(".remove").click(function () {
        $tag.next().remove(); // hidden 태그 삭제
        $tag.remove(); // 해당 태그 삭제
    });

    const $user = $("#user-list");
    // 리스트에 태그 추가
    $user.append($tag);

    // 숨겨진 필드로 empId 추가
    $user.append('<input type="hidden" name="userId" value="' + empId + '">');
}

//날짜 계산 함수
function formattedLocalDate(latestMessageDate) {
    let formattedDate = new Date(latestMessageDate).toLocaleString();
    let today = new Date();

    if (formattedDate.substring(0, 12) === today.toLocaleString().substring(0, 12)) {
        formattedDate = formattedDate.substring(12);
    } else {
        formattedDate = formattedDate.substring(0, 12)
    }
    return formattedDate;
}

//채팅방 리스트 출력
function loadChatRoomData(data, check) {
    const chatRoomDTOList = data.chatRoomDTOList;
    const $messengerBody = $('#messenger-body');
    $messengerBody.removeClass('messenger-no-chat');
    $messengerBody.html('');
    console.log("chatRoomDTOList=", chatRoomDTOList[0].chatRoomId)
    chatRoomDTOList.forEach((chatRoomDTO) => {

        //날짜 계산 함수
        let formattedDate = '';
        if (chatRoomDTO.latestMessageDate != null) {
            formattedDate = formattedLocalDate(chatRoomDTO.latestMessageDate);
        }

        let latestMessage = chatRoomDTO.latestMessage;
        if (latestMessage == null) {
            latestMessage = '메시지가 없습니다.';
            formattedDate = '';
        }
        // else if(latestMessage == ){
        //
        // }
        let unreadCount = chatRoomDTO.unreadCount;

        let html = `
                <div id="chatRoom-${chatRoomDTO.chatRoomId}" class="messenger-room" data-chat-room=${chatRoomDTO.chatRoomId}>
                    <div class="messenger-room-title">
                        <div class="messenger-chat">${chatRoomDTO.chatRoomName}</div>
                        <span class="messenger-last-message-date">${formattedDate}</span>
                    </div>
                    <div class="messenger-room-content">
                        <span class="messenger-last-message">${latestMessage}</span>
                        `;

        html += `<div class="messenger-unread-message-count" style="display: ${unreadCount !== 0 ? 'block' : 'none'};">${unreadCount}</div>`;

        html += `
                    </div>
                </div>
                 `;

        $messengerBody.append(html);
    });

    console.log("loadChatRoomData finish")

    //채팅방을 개설한 사람은 채팅방으로 이동
    if (check) {
        $('.messenger-room').first().click();
    }
}

function loadNoChatRoom() {
    const $messengerBody = $('#messenger-body');

    $messengerBody.addClass('messenger-no-chat');
    $messengerBody.html('');

    let html = `
                    <i class="bi bi-chat fw-semibold" style="font-size: 5rem; color:#ccc; height: 105px;"></i>
                    <div class="fs-2 fw-semibold" style="color:#ccc">
                        No Chat
                    </div>
    `;

    $messengerBody.append(html);
}

//채팅방 리스트 불러오기
function loadChatRoomList(check = false) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/api/messenger/rooms',
            contentType: 'application/json',
            dataType: 'json',
            success: function (response) {
                if (response.chatRoomDTOList.length !== 0) {
                    loadChatRoomData(response, check);
                } else {
                    loadNoChatRoom();
                }
                resolve(response); // 성공 시 데이터를 resolve로 전달
            },
            error: function (xhr, status, error) {
                console.error('Error creating chat room:', error);
                reject(error); // 실패 시 Promise를 reject
            }
        });
    });
}

function loadChatMessageData(data) {
    const chatMessageList = data.chatMessageList;
    const userIds = data.userIds;
    const chatRoom = data.chatRoom;
    const $messengerMiddle = $('.messenger-middle');
    const $messengerContentBody = $('.messenger-content-body');
    let userId = $('.messenger-header').data('sender-id'); // 현재 사용자 ID
    let standardDay = null;
    let check = true;

    let invitedUserMessage = empMap[chatRoom.creatorId] + '님이';
    for (const userId of userIds) {
        if (chatRoom.creatorId !== userId) {
            invitedUserMessage += '님, ' + empMap[userId];
        }
    }
    let index = invitedUserMessage.indexOf('님,');
    if (index !== -1) {
        invitedUserMessage = invitedUserMessage.substring(0, index) + invitedUserMessage.substring(index + 2);
    }
    invitedUserMessage += '님을 채팅방에 초대했습니다.';
    console.log("invitedUserMessage : ", invitedUserMessage)

    chatMessageList.forEach((chatMessage) => {
        // JavaScript에서 Date 객체로 변환하여 포맷
        let formattedDate = new Date(chatMessage.chatMessageDate).toLocaleString().substring(12, 20);

        let formattedDated = new Date(chatMessage.chatMessageDate).toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            weekday: 'long', // 요일 추가
        });

        let html = '';
        if (chatMessage.chatMessageType === 'NORMAL') {

            if (standardDay !== formattedDated) {
                //기준일 초기화
                standardDay = formattedDated;
                html += `
                        <div class="text-center standardDay">
                            <div class="standardDay-content">${formattedDated}</div>`;
                if (check) {
                    check = false;
                    html += `<div class="inviteMessage">${invitedUserMessage}</div>`;
                }
                html += `</div>`;
            }

            if (chatMessage.senderId === String(userId)) {
                html += `
                <div class="messenger-message" style="flex-direction: row-reverse;">
                    <div class="messenger-message-middle">
                        <div class="messenger-message-text">${chatMessage.chatMessageContent}</div>
                    </div>
                    <div class="messenger-message-end" style="margin:0 5px;">${formattedDate}</div>
                </div>`;
            } else {
                html += `
                <div class="messenger-message">
                    <div class="messenger-message-start">
                        <div class="messenger-sender-img-box">
                            <img class="messenger-sender-img" src="/api/emp/profile-image?empId=${chatMessage.senderId}" alt="프로필">
                        </div>
                    </div>
                    <div class="messenger-message-middle">
                        <div class="messenger-sender-name">${empMap[chatMessage.senderId]}</div>
                        <div class="messenger-message-text">${chatMessage.chatMessageContent}</div>
                    </div>
                    <div class="messenger-message-end">${formattedDate}</div>
                </div>
                        `;
            }
        } else if (chatMessage.chatMessageType === 'INVITE') {
            check = false; //단체 초대메시지 안나오게 함
            if (standardDay !== formattedDated) {
                //기준일 초기화
                standardDay = formattedDated;
                html += `
                        <div class="text-center standardDay">
                            <div class="standardDay-content">${formattedDated}</div>
                            <div class="inviteMessage">${chatMessage.chatMessageContent}</div>
                        </div>`;
            } else {
                html += `
                        <div class="text-center standardDay">
                            <div class="inviteMessage">${chatMessage.chatMessageContent}</div>
                        </div>`;
            }
        } else if (chatMessage.chatMessageType === 'LEAVE') {
            html += `
                <div class="text-center standardDay">
                    <div class="leaveMessage">${chatMessage.chatMessageContent}</div>
                </div>`;
        }

        $messengerContentBody.append(html);
    });
    let html = `
                    </div>
                </div>
                 `;
    $messengerMiddle.append(html);
}

function loadChatMessageList(chatRoomId) {
    $.ajax({
        url: '/api/messenger/rooms/' + chatRoomId,
        contentType: 'application/json',
        dataType: 'json',
        success: function (response) {
            const $messengerMiddle = $('.messenger-middle');
            $messengerMiddle.removeClass('messenger-no-chat');
            $messengerMiddle.html('');
            empMap = response.empMap;

            let html = `<div class="messenger-header">
                                <span class="messenger-chat-room">${response.messengerNameMap[response.chatRoom.chatRoomId]}</span>
                                <button id="info-toggle"><i class="bi bi-list fs-1" style="color:azure"></i></button>
                               </div>
                               <div class="messenger-content-body scrollbar">`;
            $messengerMiddle.append(html);

            if (response.chatMessageList.length !== 0) {
                loadChatMessageData(response);
            }

            html = `</div>
            <div class="messenger-footer">
                <textarea id="chatMessageContent" class="scroll" name="story" cols="40" placeholder="Message..."></textarea>
                <button id="insertMassageBtn"><i class="bi bi-send fs-2"></i></button>
            </div>`;
            $messengerMiddle.append(html);

            //스크롤바 하단으로 조정
            let $messengerContentBody = $('.messenger-content-body');
            $messengerContentBody.scrollTop($messengerContentBody.prop("scrollHeight"));

            //채팅창 정보 항목 로드
            loadChatRoomInfo(response);
        },
        error: function (xhr, status, error) {
            console.error('Error creating chat room:', error);
        }
    })
}

function loadChatRoomInfo(response) {
    const chatRoomName = response.messengerNameMap[response.chatRoom.chatRoomId];
    const userIds = response.userIds;
    const $memberBody = $('.member-body');

    $('#chatRoomName').text(chatRoomName);

    $memberBody.html(''); //초기화
    let html = '';
    userIds.forEach((userId) => {
        html += `<div class="member member-${userId}" data-user-id="${userId}">
                    <img class="member-img" src="/api/emp/profile-image?empId=${userId}" alt="프로필" style="width: 30px;height: 30px;">
                    <span>${empMap[userId]}</span>
                    ${response.chatRoom.creatorId === userId ? `<i class="bi bi-star-fill" style="margin-left: 5px;color: mediumpurple;"></i>` : ''}
                </div>`;
    });

    html += `<button class="btn-invite" data-bs-toggle="modal" data-bs-target="#employeeModal">대화상대 초대</button>`

    $memberBody.append(html);
}

$(function () {
    //채팅방 리스트 불러오기
    loadChatRoomList();

    $(document).on('load', 'messenger-body', function () {
        $('.messenger-room').each(function () {
            let chatRoomId = $(this).data('chat-room');
            console.log("4");
            joinChatRoom(chatRoomId);
        })
    })


    $("#addChat").on("click", function (e) {
        e.preventDefault();

        const targetId = $(this).data('target');
        localStorage.setItem('selectedEmpId', targetId);

        $('#employeeModal').modal('show');
    });

    //tr 클릭시 사용자 추가
    $(document).on('click', '#employeeTableBody tr', function () {
        const empId = $(this).find("td:eq(0)").text(); // 사번 가져오기
        const name = $(this).find("td:eq(1)").text(); // 이름 가져오기
        const myId = String($('.messenger-header').data('sender-id'));

        console.log("empId=", empId, "myId=", myId)

        if (empId === myId) {
            alert("자기 자신은 기본으로 추가됩니다.");
            return; // 자기 자신 추가x
        }

        // 함수 실행 종료 조건
        if ($("#user-list").find(`input:hidden[value="${empId}"]`).length > 0) {
            alert("중복 선택은 불가능합니다.");
            return; // 중복일 경우 함수 종료
        }

        // 사용자 추가
        addUser(name, empId);
    });

    //채팅방 개설
    $("#insertEmpBtn").on("click", function () {
        const data = $('#user-list input[name="userId"]')
            .map(function () {
                return $(this).val(); // 각 input 요소의 value 값을 추출
            }).get(); // jQuery 객체를 배열로 변환

        $('#employeeModal').modal('hide');
        $('#user-list').html('');

        $.ajax({
            type: 'POST',
            url: '/api/messenger/rooms',
            data: JSON.stringify(data), // JSON 데이터로 변환하여 전송
            contentType: 'application/json', // JSON 형식 명시
            dataType: 'json', // 서버에서 JSON 응답을 기대
            success: function (response) {
                console.log('Chat room created successfully:', response);
                loadChatRoomList(true);
            },
            error: function (xhr, status, error) {
                console.error('Error creating chat room:', error);
            }
        });
    })

    //채팅방 클릭
    $(document).on('click', '.messenger-room', function () {
        $('.messenger-room-active').removeClass('messenger-room-active');
        $('.messenger-chat-active').removeClass('messenger-chat-active');

        $(this).addClass('messenger-room-active');
        $(this).find('.messenger-chat').addClass('messenger-chat-active');
        let chatRoomId = $(this).data('chat-room');
        $(".messenger-middle").html('');

        openChatRoom(chatRoomId);
    })

    //메세지 입력 버튼 클릭
    $(document).on('click', '#insertMassageBtn', function () {
        let chatMessageContent = $("#chatMessageContent");
        let content = chatMessageContent.val();
        let chatRoomId = $('.messenger-room-active').data('chat-room');
        const senderId = $('.messenger-header').data('sender-id');
        console.log("Sender ID:", senderId);
        console.log("ChatRoom ID:", chatRoomId);

        sendMessage("NORMAL", chatRoomId, senderId, content);
        //메시지 입력란 초기화
        chatMessageContent.val('');
    })

    $(document).on("keydown", "#chatMessageContent", function (event) {
        if (event.key === "Enter" && !event.shiftKey) { // Shift+Enter는 제외
            event.preventDefault(); // 기본 Enter 동작(줄바꿈) 방지
            $("#insertMassageBtn").click(); // 특정 버튼 클릭 효과 실행
        }
    });

    $(document).on('keyup', '#chatMessageContent', function () {
        if ($('#chatMessageContent').val().trim() === '') {
            $('#insertMassageBtn').css('display', 'none');
        } else {
            $('#insertMassageBtn').css('display', 'block');
        }
    })

    $(document).on('click', '.btn-info-title', function () {
        chatRoomName = $('#chatRoomName').text();
        $('.input-modal-rename').val(chatRoomName);
    })

    //채팅방 이름 변경
    $(document).on('click', '#updateChatRoomName', function () {
        //모달 요소 가져오기
        const modalElement = document.getElementById('chatRoomReNameModal');
        const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);

        let chatRoomId = $('.messenger-room-active').data('chat-room');
        let updateChatRoomName = $('.input-modal-rename').val();
        if (updateChatRoomName === chatRoomName) {
            alert("채팅방 이름이 이전과 같습니다.\n방이름을 변경해주세요.");
            return;
        }
        console.log('chatRoomReNameModal', updateChatRoomName)
        $.ajax({
            type: 'POST',
            url: '/api/messenger/chatRoom/' + chatRoomId + '/rename',
            data: updateChatRoomName, // JSON 데이터로 변환하여 전송
            contentType: 'application/json', // JSON 형식 명시
            dataType: 'json', // 서버에서 JSON 응답을 기대
            success: function (response) {
                console.log('Chat room created successfully:', response.status);
                $(".messenger-middle").html('');

                loadChatRoomList()
                    .then(() => {
                        // `loadChatRoomList` 실행 완료 후 실행
                        let chatRoom = $('#chatRoom-' + chatRoomId);
                        chatRoom.addClass('messenger-room-active');
                        chatRoom.find('.messenger-chat').addClass('messenger-chat-active');
                        openChatRoom(chatRoomId);
                    })
                    .catch((error) => {
                        console.error('채팅방 목록 로드 실패:', error);
                    });
            },
            error: function (xhr, status, error) {
                console.error('Error creating chat room:', error);
            }
        });

        //모달 요소 닫기
        modalInstance.hide();
    })

    $(document).on('click', '.messenger-info-footer', function () {
        let userId = $('.messenger-header').data('sender-id');
        let label = $('#outChatRoomModalLabel');
        let html = `채팅방을 삭제하면 복구할 수 없습니다.<br>그래도 삭제하시겠습니까?`;
        if ($('.member-' + userId).find('.bi-star-fill').length > 0) {
            label.html('');
            label.append(html);
        }
    })

    //채팅방 나가기
    $(document).on('click', '#outChatRoomBtn', function () {
        //모달 요소 가져오기
        const modalElement = document.getElementById('outChatRoomModal');
        const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
        let chatRoomId = $('.messenger-room-active').data('chat-room');

        console.log('chatRoomId=', chatRoomId)

        $.ajax({
            type: 'POST',
            url: `/api/messenger/chatRoom/${chatRoomId}/roomOut`,
            dataType: 'json', // 서버에서 JSON 응답을 기대
            success: function (response) {
                console.log('Chat room created successfully:', response.status);

                leaveChatRoom(chatRoomId).then(() => {
                    location.href = "/messenger";
                });
            },
            error: function (xhr, status, error) {
                console.error('Error creating chat room:', error);
            }
        });

        //모달 요소 닫기
        modalInstance.hide();
    })

    $(document).on('click', '#addChat', function () {
        let label = $('#employeeModalLabel');
        label.html('채팅방 추가');
        $('#insertEmpBtn').css('display', 'block');
        $('#insertMemberBtn').css('display', 'none');
        $('#user-list').html('');
    })

    //채팅 멤버 추가 모달 버튼 클릭
    $(document).on('click', '.btn-invite', function () {
        let label = $('#employeeModalLabel');
        let userList = $('#user-list').html('');
        label.html('대화상대 추가');
        $('#insertEmpBtn').css('display', 'none');
        $('#insertMemberBtn').css('display', 'block');

        userList.html('');
        let html = "";
        $('.member').each(function () {
            let userId = $(this).data('user-id');
            html += `<input type="hidden" name="alreadyUserId" value="${userId}"/>`;
        });
        console.log("html =" + html)
        userList.html(html);
    })


    //채팅 멤버 추가 버튼 클릭
    $(document).on('click', '#insertMemberBtn', function () {
        const data = $('#user-list input[name="userId"]')
            .map(function () {
                return $(this).val(); // 각 input 요소의 value 값을 추출
            }).get(); // jQuery 객체를 배열로 변환
        let chatRoomId = $('.messenger-room-active').data('chat-room');
        $('#employeeModal').modal('hide');
        $('#user-list').html('');

        console.log('chatRoomId=', chatRoomId)

        $.ajax({
            type: 'POST',
            url: `/api/messenger/chatRoom/${chatRoomId}/invite`,
            data: JSON.stringify(data),
            contentType: 'application/json', // JSON 형식 명시
            dataType: 'json', // 서버에서 JSON 응답을 기대
            success: function (response) {
                console.log('Chat room created successfully:', response.status);
                loadChatRoomList(true);
            },
            error: function (xhr, status, error) {
                console.error('Error creating chat room:', error);
            }
        });

    });

});

// 초기 WebSocket 연결
connectWebSocket();

