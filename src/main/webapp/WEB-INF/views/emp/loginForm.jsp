<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Unite Login</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
  <div class="login-container">
    <div class="login-box">
      <div class="logo">
        <h1>Unite</h1>
        <img src="${pageContext.request.contextPath}/image/logo_black.png" alt="logo">
      </div>
      <form action='${pageContext.request.contextPath}/emp/loginProcess' method='post' 
			name="loginProcess">
        <input type="text" placeholder="아이디를 입력해주세요" name='id' id="id" required>
        <input type="password" placeholder="비밀번호를 입력해주세요" name='pass' required>
        <div class="options">
          <label class="checkbox-label">
          	<input type="checkbox" name='remember' id="remember" value="store">
          	아이디 저장
          </label>
          <a href="#">비밀번호 찾기</a>
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
  </div>
</body>
</html>


