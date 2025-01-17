$(document).ready(function () {
    const notificationList = $('#notificationList'); // 알림 목록
    const noNotificationsMessage = $('#noNotificationsMessage'); // "새로운 알림이 없습니다." 메시지
    const loadMoreButton = $('#loadMoreButton'); // "더 보기" 버튼
    const notificationBadge = $('#notificationBadge'); // 배지 요소
    // const toastContainer = $('#toastContainer'); // 우측 하단 알림 컨테이너
    const loggedInUser = $('#user-info').data('username'); // 로그인한 사용자
    const notifications = []; // 전체 알림 데이터
    let currentIndex = 0; // 현재 표시 중인 알림 인덱스
    const limit = 5; // 한 번에 표시할 알림 수
    const categoryIcons = {
        PROJECT: 'fas fa-angle-down', // 프로젝트 아이콘
        DOC: 'fas fa-book-open',      // 전자문서 아이콘
        SCHEDULE: 'fa-solid fa-calendar-days',  // 캘린더 아이콘
        BOARD: 'fas fa-angle-down',
        default: 'fas fa-info-circle'     // 기본 아이콘
    };

    // WebSocket 연결
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        stompClient.subscribe(`/user/queue/notification`, function (message) {
            const notification = JSON.parse(message.body);
            showToastNotification(notification); // 토스트 알림 표시
            addNotification(notification); // 알림 목록에도 추가
            updateNotificationBadge(1); // 배지 업데이트
        });
    });

    // 전체 알림 로드
    function loadNotifications() {
        $.get(`/api/notification/${loggedInUser}`)
            .done(function (data) {
                notifications.push(...data); // 전체 데이터를 배열에 저장
                if (notifications.length > 0) {
                    showNotifications(); // 처음 5개 표시
                    updateNotificationBadge(
                        notifications.filter(function (n) {
                            return !n.isRead;
                        }).length
                    ); // 읽지 않은 알림 수로 배지 초기화
                } else {
                    noNotificationsMessage.show(); // 알림이 없는 경우 메시지 표시
                    loadMoreButton.hide(); // "더 보기" 버튼 숨김
                }
            })
            .fail(function (error) {
                console.error('Error loading notifications:', error);
            });
    }

    // 알림 표시 함수
    function showNotifications() {
        const nextNotifications = notifications.slice(currentIndex, currentIndex + limit); // 다음 5개
        nextNotifications.forEach(function (notification) {
            addNotification(notification, notification.isRead, true); // appendToBottom을 true로 설정
        });
        currentIndex += nextNotifications.length; // 표시된 알림 인덱스 업데이트

        // 더 이상 표시할 알림이 없으면 "더 보기" 버튼 숨김
        if (currentIndex >= notifications.length) {
            loadMoreButton.hide();
        } else {
            loadMoreButton.show();
        }
    }

    /**
     * 새로운 알림을 추가하는 함수
     * @param {Object} notification - 알림 객체
     * @param {boolean} isRead - 알림 읽음 여부
     */
    function addNotification(notification, isRead = false, appendToBottom = false) {
        const formattedDate = formatDateTime(notification.createdAt); // 날짜 포맷 함수
        const iconClass = categoryIcons[notification.category] || categoryIcons.default; // 카테고리 아이콘 선택
        const newNotification = $(`
        <a class="dropdown-item d-flex align-items-center ${isRead ? 'text-muted' : ''}" href="${notification.targetUrl}" data-id="${notification.id}">
            <div class="me-3">
                <div class="icon-circle">
                    <i class="${iconClass}"></i>
                </div>
            </div>
            <div>
                <div class="small text-gray-500">${formattedDate}</div>
                <span class="font-weight-bold" style="font-size: 16px; font-weight: bold">${notification.title}</span><br>
                <span class="text-gray-600" style="font-size: 12px;">${notification.message}</span>
            </div>
        </a>
    `);

        // "새로운 알림이 없습니다." 메시지 제거
        noNotificationsMessage.hide();

        // appendToBottom이 true면 아래로 추가, 아니면 위로 추가
        if (appendToBottom) {
            notificationList.append(newNotification);
        } else {
            notificationList.prepend(newNotification);
        }

        newNotification.on('click', function (event) {
            event.preventDefault();

            const notificationElement = $(this); // 클릭된 요소를 가져옴

            // UI를 바로 업데이트
            notificationElement.addClass('text-muted');
            notificationElement.find('.icon-circle').removeClass('bg-primary').addClass('bg-secondary');
            updateNotificationBadge(-1);

            // 서버에 읽음 처리 요청
            $.post(`/api/notification/read/${notification.id}`)
                .done(function () {
                    console.log(`Notification ${notification.id} marked as read`);
                })
                .fail(function () {
                    notificationElement.removeClass('text-muted');
                    notificationElement.find('.icon-circle').removeClass('bg-secondary').addClass('bg-primary');
                    updateNotificationBadge(1);
                });

            // 알림 클릭 시 URL로 이동
            window.location.href = notification.targetUrl;
        });
    }

    // 읽음 처리
    function markAsRead(id, notificationElement) {
        $.post(`/api/notification/read/${id}`)
            .done(function () {
                notificationElement.addClass('text-muted');
                notificationElement.find('.icon-circle').removeClass('bg-primary').addClass('bg-secondary');
                updateNotificationBadge(-1);
            })
            .fail(function (error) {
                console.error('Error marking notification as read:', error);
            });
    }

    // 배지 업데이트
    function updateNotificationBadge(change) {
        let currentCount = parseInt(notificationBadge.text()) || 0;
        currentCount += change;

        if (currentCount > 0) {
            notificationBadge.text(currentCount > 99 ? '99+' : currentCount);
            notificationBadge.show();
        } else {
            notificationBadge.hide();
        }
    }

    // 날짜 포맷 함수
    function formatDateTime(dateTime) {
        const date = new Date(dateTime);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }

    function showToastNotification(notification) {
        const toastContainer = $('#toastContainer');

        // 알림 요소 생성
        const toast = $(`
        <div class="toast-notification shadow">
            ${notification.title}<br>
            ${notification.message}
        </div>
    `);

        // 알림 컨테이너에 추가
        toastContainer.append(toast);

        // 애니메이션으로 표시
        setTimeout(() => {
            toast.addClass('show');
        }, 100); // DOM 추가 후 애니메이션 적용

        // 5초 후에 사라지게 설정
        setTimeout(() => {
            toast.removeClass('show').addClass('hide');
            // 사라진 후 DOM에서 제거
            setTimeout(() => {
                toast.remove();
            }, 500); // 애니메이션 지속 시간과 일치
        }, 5000); // 표시 시간 (5초)
    }

    loadMoreButton.on('click', function (event) {
        event.stopPropagation();
        const nextNotifications = notifications.slice(currentIndex, currentIndex + limit); // 다음 5개
        nextNotifications.forEach(function (notification) {
            addNotification(notification, notification.isRead, true); // appendToBottom을 true로 설정
        });
        currentIndex += nextNotifications.length; // 표시된 알림 인덱스 업데이트

        // 더 이상 표시할 알림이 없으면 "더 보기" 버튼 숨김
        if (currentIndex >= notifications.length) {
            loadMoreButton.hide();
        }
    });

    // 초기 알림 로드
    loadNotifications();
});
