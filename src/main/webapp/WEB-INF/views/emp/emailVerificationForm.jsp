<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>이메일 인증</title>
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.js"></script>
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
    font-size: 0.8em;
    color: #666;
    margin-bottom: 25px;
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

span,.emailVerification{
	display: flex;
    align-items: center;
    justify-content: center;
    gap:2px;
}

label[for="emailCheck"]{
	color:#666666;
	font-size: 11px;
}
</style>
<script>
$(function(){
	$(".verify-btn").on('click', function(){
		
		const $name = $("#name");
		if($name.val().trim() == ""){
			alert("이름을 입력하세요");
			$name.focus();
			return false;
		}
		
		const $email = $("#email");
		if($email.val().trim() == ""){
			alert("이메일을 입력하세요");
			$email.focus();
			return false;
		}
		
		$.ajax({
			type:"post",
			url:"../emp/emailVerificationProcess",
			data:{
				"name":$("#name").val(),
				"email":$("#email").val()
			},
			dataType:"json",
			success:function(rdata){
				alert("이메일로 인증번호가 전송되었습니다.");
				
			}
		})
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
        <div class="emailVerification"><h2>이메일 인증</h2><label for="emailCheck">(${email})</label></div>
        <p class="instruction">입력하신 이메일 주소와 같아야 이메일을 받을 수 있습니다.</p>
        <form action='${pageContext.request.contextPath}/emp/emailVerificationProcess' method='post' 
			name="emailVerificationProcess">
            <label for="name">이름</label>
            <input type="text" id="name" name="name" placeholder="이름" required>
            
            <label for="email">이메일</label>
            <input type="email" id="email" name="email" placeholder="이메일" required>
            
            <div class="verification">
                <input type="text" name="authenCode" placeholder="인증번호 입력" required>
                <button type="button" class="verify-btn">인증번호 받기</button>
            </div>
            
            <button type="submit" class="submit-btn">다음</button>
        </form>
    </div>
</body>
</html>
