$(function () {
    const contextPath = /*[[@{/}]]*/ '';
    const $chatWindow = $("#aiChat");
    const $closeChat = $("#closeChat");
    const $chatInput = $("#chatInput");
    const $chatBody = $(".chat-body");
    const $aiBadge = $("#aiBadge");

    $chatBody.append(`<div class="chat-message chat-response">현재 지원 기능은 아래와 같습니다.
                                                            <br>- 일정확인
                                                            <br>- 일정추가
                                                            <br>- 사용자 연락처 조회
                                                            <br>- 휴가 신청</div>`);

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
                const markedResp = marked.parse(response);
                // 로딩 애니메이션 제거
                hideLoading();
                // 서버 응답 메시지 추가
                $chatBody.append(`<div class="chat-message chat-response">${markedResp}</div>`);
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

    // Enter 키로 메시지 전송 및 Shift/Alt + Enter로 줄바꿈
    $chatInput.on("keydown", function (e) {
        if (e.key === "Enter") {
            if (e.shiftKey || e.altKey) {
                // Shift + Enter 또는 Alt + Enter로 줄바꿈
                const cursorPos = this.selectionStart;
                const textBefore = $chatInput.val().substring(0, cursorPos);
                const textAfter = $chatInput.val().substring(cursorPos);
                $chatInput.val(textBefore + "\n" + textAfter);
                this.selectionStart = this.selectionEnd = cursorPos + 1; // 커서 위치를 줄바꿈 다음으로 이동
                e.preventDefault(); // 기본 Enter 동작(Submit) 방지
            } else {
                // Enter로 메시지 전송
                sendMessage();
                e.preventDefault(); // 기본 Enter 동작(Submit) 방지
            }
        }
    });

    // textarea 높이 자동 조정
    $chatInput.on("input", function () {
        this.style.height = "auto"; // 높이를 초기화
        this.style.height = this.scrollHeight + "px"; // 스크롤 높이에 맞게 조정
    });

    // 채팅창 스크롤을 최신 메시지로 이동
    function scrollToBottom() {
        $chatBody.scrollTop($chatBody.prop("scrollHeight"));
    }
});
