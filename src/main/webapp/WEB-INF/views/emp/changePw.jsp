<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>비밀번호 재설정</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/changePw.css">

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
        <label class="id">아이디:&nbsp;</label>
        <form action="changePwProcess" method="post">
            <input type="password" name="newPassword" placeholder="새 비밀번호" required>
            <input type="password" name="confirmPassword" placeholder="새 비밀번호 확인" required>
            <button type="submit" class="submit-btn">확인</button>
        </form>
    </div>
</body>
</html>
