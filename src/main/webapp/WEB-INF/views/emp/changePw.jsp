<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>비밀번호 재설정</title>
    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/changePw.css">
<script>
$(function(){
	$(".submit-btn").click(function(){
		$(".newPwText").text("");
		$(".confirmPwText").text("");
		let check = true;
		
		const $newPassword = $("#newPassword");
		if ($newPassword.val().trim() == "") {
			alert("새 비밀번호을 입력해 주세요");
			$newPassword.focus();
			return false;
		}
		
		const $confirmPassword = $("#confirmPassword");
		if ($confirmPassword.val().trim() == "") {
			alert("새 비밀번호 확인을 입력해 주세요");
			$confirmPassword.focus();
			return false;
		}
		
		if ($confirmPassword.val().trim() != $newPassword.val().trim()) {
			alert("비밀번호가 일치하지 않습니다.\n 다시 확인을 입력해 주세요");
			$newPassword.focus();
			return false;
		}
		
		if(!confirm('비밀번호 변경하시겠습니까?')){//변경 선택하지 않을 경우 submit 되지 않음
			return false;
		}else{
			alert("비밀번호가 변경 되었습니다.");
		}
		
	})
})
</script>
</head>
<body>
    <div class="container">
        <div class="logo">
        	<span>
        	Unite
        	<img src="${pageContext.request.contextPath}/image/logo_black.png" alt="logo">
        	</span>
        	<a href="login">로그인</a>
        </div>
        <h2>비밀번호 재설정</h2>
        <label class="id">아이디:&nbsp;${checkId}</label>
        <form action="changePwProcess" method="post" name="changePwProcess">
            <input type="password" name="newPassword" id="newPassword" placeholder="새 비밀번호">
            <span class="newPwText"></span>
            <input type="password" name="confirmPassword" id="confirmPassword" placeholder="새 비밀번호 확인">
            <span class="confirmPwText"></span>
            <button type="submit" class="submit-btn">확인</button>
        </form>
    </div>
</body>
</html>
