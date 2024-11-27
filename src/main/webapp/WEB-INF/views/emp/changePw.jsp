<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>비밀번호 재설정</title>
    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.js"></script>
    <script src="${pageContext.request.contextPath}/js/changePw.js"></script>
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
        <label class="id">아이디:&nbsp;${checkId}</label>
        <form action="${pageContext.request.contextPath}/emp/changePwProcess" method="post" name="changePwProcess">
            <input type="password" name="newPassword" id="newPassword" placeholder="새 비밀번호">
            <span class="newPwText"></span>
            <input type="password" name="confirmPassword" id="confirmPassword" placeholder="새 비밀번호 확인">
            <span class="confirmPwText"></span>
            <button type="submit" class="submit-btn">확인</button>
        </form>
    </div>
</body>
</html>
