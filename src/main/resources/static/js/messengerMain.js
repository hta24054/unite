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

//채팅방 리스트 출력
function loadChatRoomData(data) {
    const chatRoomDTOList = data.chatRoomDTOList;
    const $messengerBody = $('#messenger-body');
    $messengerBody.removeClass('messenger-no-chat');
    $messengerBody.html('');

    chatRoomDTOList.forEach((chatRoomDTO) => {
        // JavaScript에서 Date 객체로 변환하여 포맷
        let latestMessageDate = chatRoomDTO.latestMessageDate;
        let formattedDate = '';
        if (latestMessageDate !== null) {
            formattedDate = new Date(latestMessageDate).toLocaleString();
        }
        let latestMessage = chatRoomDTO.latestMessage;
        if (latestMessage == null) {
            latestMessage = '메시지가 없습니다.';
        }
        let unreadCount = chatRoomDTO.unreadCount;

        let html = `
                <div class="messenger-room" data-chat-room=${chatRoomDTO.chatRoomId}>
                    <div class="messenger-room-title">
                        <div class="messenger-chat">${chatRoomDTO.chatRoomName}</div>
                        <span class="messenger-last-message-date">${formattedDate}</span>
                    </div>
                    <div class="messenger-room-content">
                        <span class="messenger-last-message">${latestMessage}</span>
                        `;

        if (unreadCount !== 0) {
            html += `<div class="messenger-unread-message-count">${chatRoomDTO.unreadCount}</div>`;
        }

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
                console.log('0 아님')
                loadChatRoomData(response);
            } else {
                console.log('0')
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

    chatMessageList.forEach((chatMessage) => {
        // JavaScript에서 Date 객체로 변환하여 포맷
        let formattedDate = new Date(chatMessage.chatMessageDate).toLocaleString();

        let html = `
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

        html += `
                    </div>
                </div>
                 `;
        $messengerMiddle.append(html);
    });
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
                <input id="chatMessageContent" type="text" placeholder="Enter message">
                <button id="insertMassageBtn">Send</button>
            </div>`;
            $messengerMiddle.append(html);
        },
        error: function (xhr, status, error) {
            console.error('Error creating chat room:', error);
        }
    })
}

function joinChatRoom(chatRoomId) {
    stompClient.subscribe(`/topic/chatRoom/${chatRoomId}`, (message) => {
        const chatMessage = JSON.parse(message.body);
        console.log(`Message from chatRoom ${chatRoomId}:`, chatMessage);
        displayMessage(chatMessage);
    });
}

function sendMessage(chatRoomId, senderId, content) {
    stompClient.send(`/app/chatRoom/${chatRoomId}`, {}, JSON.stringify({
        chatRoomId: chatRoomId,
        senderId: senderId,
        content: content
    }));
}

$(function () {
    //채팅방 리스트 불러오기
    loadChatRoomList();

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

        console.log("data", data)

        $('#employeeModal').modal('hide');

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

        loadChatMessageList(chatRoomId);

    })

    //메세지 입력 버튼 클릭
    $(document).on('click', '#insertMassageBtn', function () {
        let message = $("#chatMessageContent").text();
        let chatRoomId = $('.messenger-room-active').data('chat-room');

        $.ajax({
            type: 'POST',
            url: '/api/messenger/messages',
            data: JSON.stringify(message, chatRoomId), // JSON 데이터로 변환하여 전송
            contentType: 'application/json', // JSON 형식 명시
            dataType: 'json', // 서버에서 JSON 응답을 기대
            success: function (response) {
                console.log('Chat room created successfully:', response);
                loadChatRoomList();
            },
            error: function (xhr, status, error) {
                console.error('Error creating chat room:', error);
            }
        })
    })
});
