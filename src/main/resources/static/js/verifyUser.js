const contextPath = /*[[@{/}]]*/ '';
$(function () {
    let countdownInterval;

    // 카운트다운 타이머 시작 함수
    function startCountdown(durationInSeconds, $targetElement) {
        let remainingTime = durationInSeconds;

        clearInterval(countdownInterval); // 이전 타이머 초기화

        countdownInterval = setInterval(function () {
            const minutes = Math.floor(remainingTime / 60);
            const seconds = remainingTime % 60;

            $targetElement.text(`인증번호가 ${minutes}:${seconds < 10 ? "0" : ""}${seconds} 후에 만료됩니다.`);

            if (remainingTime <= 0) {
                clearInterval(countdownInterval);
                $targetElement.text("인증번호가 만료되었습니다. 다시 요청해주세요.").css("color", "red");
                $("#authenCode").prop("disabled", true); // 인증번호 입력 필드 비활성화
                $(".verify-btn").prop("disabled", false).text("인증번호 재전송"); // 인증번호 요청 버튼 재전송으로 변경
            }

            remainingTime--;
        }, 1000);
    }

    // 이름과 이메일 유효성 검사
    function validateUserInfo() {
        const $name = $("#name");
        if ($name.val().trim() === "") {
            alert("이름을 입력하세요.");
            $name.focus();
            return false;
        }

        const $email = $("#email");
        if ($email.val().trim() === "") {
            alert("이메일을 입력하세요.");
            $email.focus();
            return false;
        }

        const emailPattern = /^\w+@\w+\.\w{2,}$/;
        if (!emailPattern.test($email.val())) {
            alert("이메일 형식이 맞지 않습니다.");
            $email.focus();
            return false;
        }

        return true; // 유효성 검사 통과
    }

    // 인증번호 유효성 검사
    function validateAuthCode() {
        const $authenCode = $("#authenCode");
        if ($authenCode.val().trim() === "") {
            alert("인증번호를 입력해주세요.");
            $authenCode.focus();
            return false;
        }

        if ($authenCode.val().trim().length !== 8) {
            alert("인증번호는 8자리여야 합니다.");
            $authenCode.focus();
            return false;
        }

        return true; // 유효성 검사 통과
    }

    // 인증번호 발송 버튼 클릭 이벤트
    $(".verify-btn").on("click", function () {
        $('html').css("cursor", "wait"); // 마우스 로딩 표시

        if (!validateUserInfo()) { // 이름과 이메일 유효성 검사
            $('html').css("cursor", "auto");
            return false;
        }

        $(".verify-btn").prop("disabled", true).text("전송 중...");

        $.ajax({
            type: "post",
            url: contextPath + "/api/auth/sendAuthCode",
            data: {
                "ename": $("#name").val(),
                "email": $("#email").val()
            },
            dataType: "json",
            success: function (rdata) {
                $('html').css("cursor", "auto");
                if (rdata === true) {
                    alert("인증번호를 발송했습니다. 메일을 확인해주세요.");

                    $("#authenCode")
                        .prop("disabled", false) // 입력 필드 활성화
                        .css({
                            "background-color": "white", // 배경색 변경
                            "cursor": "text" // 입력 가능 상태로 보이도록 변경
                        }).focus(); // 포커스 설정 // 인증번호 입력 필드 활성화
                    $(".authenText").text("인증번호가 발송되었습니다.").css({
                        "color": "green",
                        "font-size": "12px"
                    });

                    // 5분 카운트다운 시작
                    startCountdown(300, $(".authenText"));

                    $(".verify-btn").prop("disabled", false).text("재전송");
                } else {
                    alert("회원 정보가 일치하지 않습니다.");
                    $("#name").focus();
                    $(".verify-btn").prop("disabled", false).text("인증번호 전송");
                }
            },
            error: function () {
                $('html').css("cursor", "auto");
                alert("인증번호 발송에 실패했습니다. 다시 시도해주세요.");
                $(".verify-btn").prop("disabled", false).text("재전송");
            }
        });
    });

    // 인증번호 확인 및 제출
    $("form[name='emailVerificationProcess']").submit(function (e) {
        e.preventDefault(); // 폼 기본 동작 방지

        if (!validateAuthCode()) { // 인증번호 유효성 검사
            return false;
        }

        $.ajax({
            type: "post",
            url: contextPath + "/api/auth/verifyAuthCode",
            data: {inputCode: $("#authenCode").val()},
            dataType: "json",
            success: function (isValid) {
                if (isValid) {
                    alert("인증 성공! 비밀번호 변경 페이지로 이동합니다.");
                    window.location.href = contextPath + "/auth/resetPassword";
                } else {
                    alert("인증번호가 올바르지 않습니다. 다시 확인해주세요.");
                    $(".authenText").text("인증번호를 다시 발송하거나 다시 입력해주세요.").css("color", "red");
                    $("#authenCode").focus();
                }
            },
            error: function () {
                alert("서버와 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            }
        });
    });
});
