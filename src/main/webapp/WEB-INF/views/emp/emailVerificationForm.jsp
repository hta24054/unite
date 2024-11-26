<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>이메일 인증</title>
	<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.js"></script>
	<script src="${pageContext.request.contextPath}/js/emailVerification.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/emailVerification.css">

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
        <div class="emailVerification"><h2>이메일 인증</h2>(${email})</div>
        <p class="instruction">입력하신 이메일 주소와 같아야 이메일을 받을 수 있습니다.</p>
        <form action='${pageContext.request.contextPath}/emp/emailVerificationProcess' method='post'
			name="emailVerificationProcess">
			<div class="inputLine">
	            <label for="name">이름</label>
	            <input type="text" id="name" name="name" placeholder="이름을 입력하세요">
            </div>
            <div class="inputLine">
	            <label for="email">이메일</label>
	            <input type="email" id="email" name="email" placeholder="이메일을 입력하세요">
            </div>
            <div class="verification">
                <input type="text" id="authenCode" name="authenCode" placeholder="인증번호 8자리 입력" disabled>
                <button type="button" class="verify-btn">인증번호 받기</button>
            </div>
            <span class="authenText"></span>
            <button type="submit" class="submit-btn">다음</button>
        </form>
    </div>
</body>
</html>
