<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>이메일 인증</title>
<style>
* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
    font-family: Arial, sans-serif;
}

body {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    background-color: #f7f7f7;
}

.container {
    width: 400px;
    height: 420px;
    padding: 20px;
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    text-align: center;
    position: relative;
}

.logo {
    font-size: 1em;
    font-weight: bold;
    margin-bottom: 10px;
    display: flex;
    justify-content: space-between;
    gap: 5px;
}

.login-link {
    position: absolute;
    top: 15px;
    right: 15px;
    font-size: 0.9em;
    color: #666;
}

h2 {
    font-size: 1.2em;
    margin-bottom: 5px;
}

.instruction {
    font-size: 0.9em;
    color: #666;
    margin-bottom: 15px;
}

form label {
    display: none;
}

form input[type="text"],
form input[type="email"] {
    width: 100%;
    padding: 10px;
    margin: 10px 0;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 0.9em;
}

.verification {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.verification input[type="text"] {
    width: 65%;
}

.verify-btn {
    width: 30%;
    padding: 10px;
    border: none;
    background-color: #34495e;
    color: #fff;
    border-radius: 5px;
    font-size: 0.8em;
    cursor: pointer;
}

.submit-btn {
    padding: 6px 12px;
    margin: 8% 0px 8%;
    border: 1px #aaaaaa solid;
    background-color: white;
    color: #666666;
    border-radius: 5px;
    font-size: 14px;
    cursor: pointer;
}

img{
	width:20px;
}

a{
	font-size: 14px;
	text-decoration: none;
	color:#666666;
}

a:hover {
    text-decoration: underline;
}

span{
	display: flex;
    align-items: center;
    justify-content: center;
    gap:2px;
}

</style>
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
        <h2>이메일 인증</h2>
        <p class="instruction">입력하신 이메일 주소와 같아야 이메일을 받을 수 있습니다.</p>
        <form>
            <label for="name">이름</label>
            <input type="text" id="name" placeholder="이름" required>
            
            <label for="email">이메일</label>
            <input type="email" id="email" placeholder="이메일" required>
            
            <div class="verification">
                <input type="text" placeholder="인증번호 입력" required>
                <button type="button" class="verify-btn">인증번호 받기</button>
            </div>
            
            <button type="submit" class="submit-btn">다음</button>
        </form>
    </div>
</body>
</html>