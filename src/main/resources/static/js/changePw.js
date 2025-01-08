$(function () {
    $(".submit-btn").click(function () {
        $(".newPwText").text("");
        $(".confirmPwText").text("");
        let check = true;

        const pattern = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,}$/;

        const $newPassword = $("#newPassword");
        if ($newPassword.val().trim() == "") {
            $(".newPwText").text("새 비밀번호을 입력해 주세요");
            $newPassword.focus();
            check = false;
        }

        const $confirmPassword = $("#confirmPassword");
        if ($confirmPassword.val().trim() == "") {
            $(".confirmPwText").text("새 비밀번호 확인을 입력해 주세요");
            $confirmPassword.focus();
            check = false;
        }

        if (!pattern.test($newPassword.val())) {
            alert("비밀번호가 최소 8자 이상,\n하나 이상의 영문자,\n하나 이상의 숫자,\n하나 이상의 특수 문자를 포함해야합니다.");
            return false;
        }

        if (!check) {
            return false;
        }

        if ($confirmPassword.val().trim() != $newPassword.val().trim()) {
            alert("비밀번호가 일치하지 않습니다.\n비밀번호를 다시 확인을 해주세요");
            $newPassword.focus();
            return false;
        }

        if (!confirm('비밀번호 변경하시겠습니까?')) {//변경 선택하지 않을 경우 submit 되지 않음
            return false;
        }
    })
})