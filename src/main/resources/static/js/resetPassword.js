const contextPath = /*[[@{/}]]*/ '';
$(function () {
    $("#resetPasswordForm").submit(function (event) {
        event.preventDefault();
        $(".newPwText").text("");
        $(".confirmPwText").text("");
        let check = true;

        const pattern = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,}$/;

        const $newPassword = $("#newPassword");
        const $confirmPassword = $("#confirmPassword");

        // 새 비밀번호 입력 확인
        if ($newPassword.val().trim() === "") {
            $(".newPwText").text("새 비밀번호를 입력해 주세요");
            $newPassword.focus();
            check = false;
        }

        // 비밀번호 확인 입력 확인
        if ($confirmPassword.val().trim() === "") {
            $(".confirmPwText").text("새 비밀번호 확인을 입력해 주세요");
            $confirmPassword.focus();
            check = false;
        }

        // 비밀번호 복잡성 검사
        if (check && !pattern.test($newPassword.val())) {
            alert("비밀번호는 최소 8자 이상,\n하나 이상의 영문자,\n하나 이상의 숫자,\n하나 이상의 특수 문자를 포함해야 합니다.");
            $newPassword.focus();
            return false;
        }

        // 비밀번호와 확인 비밀번호가 동일한지 확인
        if (check && $confirmPassword.val().trim() !== $newPassword.val().trim()) {
            alert("비밀번호가 일치하지 않습니다.\n비밀번호를 다시 확인해 주세요.");
            $confirmPassword.focus();
            return false;
        }

        // 비밀번호 변경 확인 메시지
        if (!confirm('비밀번호를 변경하시겠습니까?')) {
            return false;
        }

        // AJAX 요청
        $.ajax({
            type: "post",
            url: contextPath + "/api/auth/resetPassword",
            data: {"newPassword": $newPassword.val()}, // 비밀번호 값만 전달
            success: function (data) {
                if (data == 1) {
                    alert("비밀번호를 설정하였습니다.");
                    window.location.href = contextPath + "/auth/login"; // 로그인 페이지로 이동
                } else {
                    alert("비밀번호 설정 중 오류가 발생했습니다.");
                }
            },
            error: function () {
                alert("비밀번호 설정 중 오류가 발생했습니다.");
            }
        });
    });
});
