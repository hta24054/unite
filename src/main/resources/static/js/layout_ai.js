$(function () {
    const contextPath = /*[[@{/}]]*/ '';
    const $chatWindow = $("#aiChat");
    const $closeChat = $("#closeChat");
    const $chatInput = $("#chatInput");
    const $chatBody = $(".chat-body");
    const $aiBadge = $("#aiBadge");

    $chatBody.append(`<div class="chat-message chat-response">현재 지원 기능은 아래와 같습니다.
                                                            <br>1. 일정확인
                                                            <br>2. 일정추가
                                                            <br>3. 사용자 연락처 조회
                                                            <br>4. 텍스트 요약
                                                            <br>5. 휴가 신청</div>`);

    // 뱃지 클릭 시 채팅창 표시/숨김
    $aiBadge.on("click", function () {
        $chatWindow.toggleClass("showAi");
    });

    // 닫기 버튼 클릭 시 채팅창 숨김
    $closeChat.on("click", function () {
        $chatWindow.removeClass("showAi");
    });

    // 로딩 애니메이션 추가
    function showLoading() {
        $chatBody.append(`
            <div class="chat-message loading">
                <div class="loader"></div>
            </div>
        `);
        scrollToBottom();
    }

    // 로딩 애니메이션 제거
    function hideLoading() {
        $chatBody.find(".loading").remove();
    }

    // 사용자 요청 메시지 추가
    function addChatRequest(message) {
        // 사용자 요청 메시지 추가
        $chatBody.append(`<div class="chat-message chat-request">${message}</div>`);
        scrollToBottom();

        // 로딩 애니메이션 표시
        showLoading();

        // AJAX 요청
        $.ajax({
            url: contextPath + '/api/ai/chat', // 서버 API 엔드포인트
            method: 'POST', // HTTP 메서드
            contentType: 'application/json', // 요청 데이터 형식
            data: JSON.stringify({message: message}), // JSON 형식으로 데이터 전송
            success: function (response) {
                // 로딩 애니메이션 제거
                hideLoading();
                // 서버 응답 메시지 추가
                $chatBody.append(`<div class="chat-message chat-response">${response.resultMessage}</div>`);
                scrollToBottom();
            },
            error: function (xhr, status, error) {
                // 로딩 애니메이션 제거
                hideLoading();
                // 에러 처리
                console.error('AJAX Error:', status, error);
                $chatBody.append(`<div class="chat-message chat-response">오류입니다.<br>잠시 후에 다시 시도해주세요.</div>`);
                scrollToBottom();
            }
        });
    }

    // 메시지 전송
    function sendMessage() {
        const message = $chatInput.val().trim();
        if (message) {
            // 사용자 요청 메시지 추가
            addChatRequest(message);

            // 입력 필드 초기화
            $chatInput.val("");
        }
    }

    // 전송 버튼 클릭 이벤트
    $("#aiSend").on("click", sendMessage);

    // Enter 키로 메시지 전송
    $chatInput.on("keydown", function (e) {
        if (e.key === "Enter") {
            sendMessage();
        }
    });

    // 채팅창 스크롤을 최신 메시지로 이동
    function scrollToBottom() {
        $chatBody.scrollTop($chatBody.prop("scrollHeight"));
    }
});
