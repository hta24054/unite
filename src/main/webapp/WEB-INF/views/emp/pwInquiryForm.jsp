<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>비밀번호 찾기</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
  <style>
    .description {
      margin: 10px 0px 20%;
      font-size: 14px;
      color: #555;
    }

    button {
      padding: 12px;
      margin : 10% 0px 10%;
    }

    /* 하단 링크 스타일 */
    .footer {
      margin-top: auto;
      font-size: 12px;
      color: #888;
      text-align: center;
      display: flex;
      justify-content: space-around;
    }
    .footer a {
      color: #2e3b63;
      text-decoration: none;
      margin-left: 5px;
    }
    .footer a:hover {
      text-decoration: underline;
    }
    
    .login-box{
      display: flex;
      flex-direction: column; /* 세로 방향으로 요소 배치 */
      justify-content: space-between; /* 요소 간의 공간을 고르게 분배 */
    }
    
    .logo{
      margin:0px;
    }
  </style>
</head>
<body>

  <div class="login-container">
   <div class="login-box">
    <div class="logo">
        <h1>Unite</h1>
        <img src="${pageContext.request.contextPath}/image/logo_black.png" alt="logo">
    </div>
    <form action='${pageContext.request.contextPath}/emp/pwInquiryProcess' method='post' 
			name="pwInquiryProcess">
	    <div class="description">
	      비밀번호를 찾고자하는 아이디를 입력해주세요.
	    </div>
	    <input type="text" id="id" name="id" placeholder="아이디를 입력해주세요" required>
	    <button type="submit">다음</button>
    </form>
    <div class="footer">
      아이디가 기억나지 않는다면 관리자 문의 <a href="login">로그인</a>
    </div>
   </div>
  </div>

</body>
</html>