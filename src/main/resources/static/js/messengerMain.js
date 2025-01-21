let stompClient;
let subscriptions = {};
let empMap;

// WebSocket 연결
function connectWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        console.log("Connected to WebSocket-Messenger");

        // 초기 채팅방 구독
        initializeChatRoomSubscriptions();
    });
}

// 사용자가 초대된 모든 채팅방을 구독
function initializeChatRoomSubscriptions() {
    const userId = $('.messenger-header').data('sender-id'); // 현재 사용자 ID

    // AJAX 요청으로 사용자가 초대된 모든 채팅방 ID 가져오기
    $.ajax({
        url: `/api/messenger/chatRooms/${userId}`,
        type: 'GET',
        success: function (chatRooms) {
            chatRooms.forEach(chatRoomId => {
                joinChatRoom(chatRoomId); // 각 채팅방 구독
            });
        },
        error: function (error) {
            console.error("Failed to fetch chat rooms:", error);
        }
    });
}

// 로그인 시 초대 알림 구독
function subscribeToInvitations(userId) {
    stompClient.subscribe(`/topic/user/${userId}`, (message) => {
        const notification = JSON.parse(message.body);

        console.log(`New chat room invitation: ${notification.chatRoomId} - ${notification.chatRoomName}`);

        // sendMessage(chatRoomId, senderId, content);

        // 초대받은 채팅방 자동 구독
        joinChatRoom(notification.chatRoomId);
    });
}

// 특정 채팅방 구독
function joinChatRoom(chatRoomId) {
    if (subscriptions[chatRoomId]) {
        console.log(`Already subscribed to chatRoom ${chatRoomId}`);
        return;
    }

    subscriptions[chatRoomId] = stompClient.subscribe(`/topic/chatRoom/${chatRoomId}`, (message) => {
        const chatMessage = JSON.parse(message.body);
        let chatRoomId = $('.messenger-room-active').data('chat-room');
        let userId = $('.messenger-header').data('sender-id'); // 현재 사용자 ID

        console.log("chatMessage = ", chatMessage)

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
    });

    console.log(`Subscribed to chatRoom ${chatRoomId}`);
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

    // 채팅방 메시지 출력
    joinChatRoom(chatRoomId);
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
    if (subscriptions[chatRoomId]) {
        subscriptions[chatRoomId].unsubscribe(); // 구독 해제
        delete subscriptions[chatRoomId]; // 구독 정보 삭제
        console.log(`Unsubscribed from chatRoom ${chatRoomId}`);
    } else {
        console.log(`No active subscription for chatRoom ${chatRoomId}`);
    }
}

// 메시지 출력
function displayMessage(chatMessage, isMyMessage) {
    const $messengerContentBody = $('.messenger-content-body');
    let userId = $('.messenger-header').data('sender-id'); // 현재 사용자 ID
    console.log("message : ", chatMessage)

    let formattedDate = new Date(chatMessage.chatMessageDate).toLocaleString().substring(12, 20);

    let formattedDated = new Date(chatMessage.chatMessageDate).toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long', // 요일 추가
    });

    let standardDay = String($('.standardDay-content').last().text());
    let html = '';

    if (standardDay !== formattedDated) {
        html = `
                <div class="text-center standardDay">
                    <div class="standardDay-content">${formattedDated}</div>
                </div>`;
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
function sendMessage(chatRoomId, senderId, content) {
    stompClient.send(`/app/chatRoom/${chatRoomId}`, {}, JSON.stringify({
        chatRoomId: chatRoomId,
        senderId: senderId,
        chatMessageContent: content
    }));
}

// 초기 WebSocket 연결
connectWebSocket();

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

    console.log("format:", formattedDate.substring(0, 12), " today:", today.toLocaleString().substring(0, 12))
    if (formattedDate.substring(0, 12) === today.toLocaleString().substring(0, 12)) {
        formattedDate = formattedDate.substring(12);
    } else {
        formattedDate = formattedDate.substring(0, 12)
    }
    return formattedDate;
}

//채팅방 리스트 출력
function loadChatRoomData(data) {
    const chatRoomDTOList = data.chatRoomDTOList;
    const $messengerBody = $('#messenger-body');
    $messengerBody.removeClass('messenger-no-chat');
    $messengerBody.html('');

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
function loadChatRoomList() {
    $.ajax({
        url: '/api/messenger/rooms',
        contentType: 'application/json',
        dataType: 'json',
        success: function (response) {
            if (response.chatRoomDTOList.length !== 0) {
                loadChatRoomData(response);
            } else {
                loadNoChatRoom();
            }
        },
        error: function (xhr, status, error) {
            console.error('Error creating chat room:', error);
        }
    });
}

function loadChatMessageData(data) {
    const chatMessageList = data.chatMessageList;
    const $messengerMiddle = $('.messenger-middle');
    const $messengerContentBody = $('.messenger-content-body');
    let userId = $('.messenger-header').data('sender-id'); // 현재 사용자 ID
    let standardDay = null;

    chatMessageList.forEach((chatMessage, index) => {
        // JavaScript에서 Date 객체로 변환하여 포맷
        let formattedDate = new Date(chatMessage.chatMessageDate).toLocaleString().substring(12, 20);

        let formattedDated = new Date(chatMessage.chatMessageDate).toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            weekday: 'long', // 요일 추가
        });

        console.log("chatMessage.senderId = ", chatMessage.senderId, " userId = ", userId)
        let html = '';

        if (standardDay !== formattedDated) {
            //기준일 초기화
            standardDay = formattedDated;
            html += `
                <div class="text-center standardDay">
                    <div class="standardDay-content">${formattedDated}</div>
                </div>`;
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
                        <span class="messenger-chat-room">${response.messengerNameMap[response.chatRoomId]}</span>
                        <button id="info-toggle"><i class="bi bi-list fs-1"></i></button>
                       </div>
                       <div class="messenger-content-body scrollbar">`;
            $messengerMiddle.append(html);

            if (response.chatMessageList.length !== 0) {
                console.log('0이 아님')
                loadChatMessageData(response);
            }

            html = `</div>
            <div class="messenger-footer">
                <input id="chatMessageContent" type="text" placeholder="Message...">
                <button id="insertMassageBtn"><i class="bi bi-send fs-4"></i></button>
            </div>`;
            $messengerMiddle.append(html);

            //스크롤바 하단으로 조정
            let $messengerContentBody = $('.messenger-content-body');
            $messengerContentBody.scrollTop($messengerContentBody.prop("scrollHeight"));
        },
        error: function (xhr, status, error) {
            console.error('Error creating chat room:', error);
        }
    })
}

$(function () {
    //채팅방 리스트 불러오기
    loadChatRoomList();

    $(document).on('load', 'messenger-body', function () {
        $('.messenger-room').each(function () {
            let chatRoomId = $(this).data('chat-room');
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
        const myId = `[(${empId})]`;

        if (empId === myId) {
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
                loadChatRoomList();
            },
            error: function (xhr, status, error) {
                console.error('Error creating chat room:', error);
            }
        });
    })

    //채팅방 클릭
    $(document).on('click', '.messenger-room', function () {
        $('.messenger-room-active').removeClass('messenger-room-active');
        $(this).addClass('messenger-room-active');
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

        sendMessage(chatRoomId, senderId, content);
        //메시지 입력란 초기화
        chatMessageContent.val('');
    })

    $(document).on('keyup', '#chatMessageContent', function () {
        if ($('#chatMessageContent').val().trim() === '') {
            $('#insertMassageBtn').css('display', 'none');
        } else {
            $('#insertMassageBtn').css('display', 'block');
        }

    })
});
