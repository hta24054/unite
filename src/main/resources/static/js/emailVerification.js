function validation(submit) {
    const $name = $("#name");
    if ($name.val().trim() == "") {
        alert("이름을 입력하세요");
        $name.focus();
        return false;
    }

    const $email = $("#email");
    if ($email.val().trim() == "") {
        alert("이메일을 입력하세요");
        $email.focus();
        return false;
    } else {
        const pattern = /^\w+@\w+[.]\w{2,}$/;
        if (!pattern.test($email.val())) {
            alert("이메일형식이 맞지 않습니다.");
            $email.focus();
            return false;
        }
    }

    if (submit) {
        const $authenCode = $("#authenCode");
        if ($authenCode.val().trim() == "") {
            alert("이메일 인증을 받고 다시 시도해 주세요.");
            return false;
        }
    }

    return true;
}

$(function () {
    let authenCode = "";

    $(".verify-btn").on('click', function () {
        $('html').css("cursor", "wait");//마우스 로딩중 표시

        //유효성 검사(매개변수로 submit하는지 boolean값으로 넣음)
        if (!validation(false)) {
            $('html').css("cursor", "auto"); //마우스 원래대로 표시
            return false;
        }

        $(".verify-btn").prop("disabled", true);//인증번호 받기 버튼 비활성화

        $.ajax({
            type: "post",
            url: "../emp/sendAuthenCode",
            data: {
                "name": $("#name").val(),
                "email": $("#email").val()
            },
            dataType: "json",
            success: function (rdata) {
                $('html').css("cursor", "auto"); //마우스 원래대로 표시
                console.log("AJAX 요청 성공:", rdata);
                alert(rdata.message);
                if (rdata.authenCode != null) {//회원정보가 일치할 경우
                    $("#authenCode").prop("disabled", false) // 인증번호 필드를 활성화
                        .css("background", "white")
                        .focus();

                    $(".authenText").text("인증번호를 발송했습니다.").css({
                        "color": "green",
                        "font-size": "12px",
                        "display": "flex",
                        "justify-content": "flex-end"
                    })

                    authenCode = rdata.authenCode;
                } else {//회원정보가 다를 경우
                    $("#name").focus();
                }

                $(".verify-btn").prop("disabled", false);//인증번호 받기 버튼 활성화
            },
            error: function (request, status, error) {
                $('html').css("cursor", "auto"); //마우스 원래대로 표시
                console.error("AJAX 요청 실패:", status, error);
                alert("인증번호 발송에 실패했습니다. 잠시 후 다시 시도해주세요.");
            }
        });


    });


    $("form[name='emailVerificationProcess']").submit(function () {

        //유효성 검사(매개변수로 submit하는지 boolean값으로 넣음)
        if (!validation(true)) {
            return false;
        }

        const $authencode = $("#authenCode");

        console.log($authencode.val())
        console.log(authenCode)
        if ($authencode.val().length != 8) {
            alert("인증번호 8자리를 입력해 주세요");
            $(".authenText").text("인증번호를 다시 발송하거나 다시 입력해주세요.").css("color", "red");
            $authencode.focus();
            return false;
        }

        if ($authencode.val() != authenCode) {
            alert("인증번호가 올바르지 않습니다. 확인 후 다시 입력해 주세요.");
            $(".authenText").text("인증번호를 다시 발송하거나 다시 입력해주세요.").css("color", "red");
            $authencode.focus();
            return false;
        }


    })
});