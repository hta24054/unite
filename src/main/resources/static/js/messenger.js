$(function () {
    const messengerList = $('#messengerList'); // 메신저 메시지 목록
    const noMessengerMessage = $('#noMessengerMessage'); // "채팅을 시작해보세요" 메시지
    const messengerBadge = $('#messengerBadge'); // 배지 요소
    let messengerMessages; // 전체 메신저 메시지 데이터

    $("#MessengerButton").on('click', function () {
        location.href = "/messenger"
    })

    // 전체 메신저 메시지 로드
    function loadMessengerMessages() {
        const isHomeMessenger = true; // 메신저 메인화면이면 false, 아니면 true
        $.get(`/api/messenger/rooms?isHomeMessenger=${isHomeMessenger}`)
            .done(function (data) {
                messengerMessages = data.chatRoomDTOList; // 전체 데이터를 배열에 저장
                if (messengerMessages.length > 0) {
                    let cnt = 0;

                    messengerMessages.filter(function (messengerMessage) {
                        let unreadCount = messengerMessage.unreadCount;
                        if (unreadCount > 0) {
                            cnt += messengerMessage.unreadCount;
                        }
                    })

                    showMessengerMessages(); // 최대 5개 표시
                    updateMessengerBadge(cnt); // 읽지 않은 알림 수로 배지 초기화
                } else {
                    noMessengerMessage.show(); // 알림이 없는 경우 메시지 표시
                }
            })
            .fail(function (error) {
                console.error('Error loading notifications:', error);
            });
    }

    // 메신저 메시지 표시 함수
    function showMessengerMessages() {
        const nextMessengerMessages = messengerMessages.slice(0, 5); // 최신순 5개
        nextMessengerMessages.forEach(function (MessengerMessage) {
            addMessengerMessage(MessengerMessage, MessengerMessage.unreadCount);
        });
    }

    function addMessengerMessage(MessengerMessage) {
        let formattedDate = formattedLocalDate(MessengerMessage.latestMessageDate); // 날짜 포맷 함수
        const newMessengerMessage = $(`
            <a class="dropdown-item d-flex align-items-center" 
               href="/messenger" data-id="${MessengerMessage.chatRoomId}">
                <div>
                    <div class="small text-gray-500">${formattedDate}</div>
                    <span class="font-weight-bold" style="font-size: 16px; font-weight: bold">${MessengerMessage.chatRoomName}</span><br>
                    <div class="messenger-room-content1">
                    <span class="messenger-last-message1" style="font-size: 12px;">
                        ${
            (MessengerMessage.latestMessage === null)
                ? `메세지가 없습니다.`
                : `${MessengerMessage.latestMessage}`
        }
                    </span>
                    ${
            (MessengerMessage.unreadCount === 0)
                ? ``
                : `<div class="unread-message-count1" style="display: block;">${MessengerMessage.unreadCount}</div>`
        }
                    </div>
                </div>
            </a>
        `);

        // "새로운 알림이 없습니다." 메시지 제거
        noMessengerMessage.hide();
        messengerList.append(newMessengerMessage);

        newMessengerMessage.on('click', function (event) {
            event.preventDefault();

            // const messengerMessageElement = $(this); // 클릭된 요소를 가져옴

            updateMessengerBadge(-1);

            // 서버에 읽음 처리 요청
            // $.post(`/api/notification/read/${notification.id}`)
            //     .done(function () {
            //         console.log(`Notification ${notification.id} marked as read`);
            //     })
            //     .fail(function () {
            //         messengerMessageElement.removeClass('text-muted');
            //         messengerMessageElement.find('.icon-circle').removeClass('bg-secondary').addClass('bg-primary');
            //         updateMessengerBadge(1);
            //     });

            // 알림 클릭 시 URL로 이동
            window.location.href = "/messenger";
        });
    }

    // 배지 업데이트
    function updateMessengerBadge(change) {
        let currentCount = parseInt(messengerBadge.text()) || 0;
        currentCount += change;

        if (currentCount > 0) {
            messengerBadge.text(currentCount > 99 ? '99+' : currentCount);
            messengerBadge.show();
        } else {
            messengerBadge.hide();
        }
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

    // 초기 메신저 메시지 로드
    loadMessengerMessages();
});