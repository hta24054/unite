<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Unite Login</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
  <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.js"></script>
  <script>
    let result='${message}';
    
	if(result != ''){
		alert(result);
		<%session.removeAttribute("message"); %>
	}
  
	$(function(){
		const id = '${cookieId}';
		if(id){
			$("#id").val(id);
			$("#remember").prop('checked',true);
		}
		
		$("button").click(function(){
			$(".idText").text("");
			$(".pwText").text("");
			let check = true;
			
			const $id = $("#id");
			if ($id.val().trim() == "") {
				$(".idText").text("아이디를 입력해 주세요");
				$id.focus();
				check =  false;
			}
			
			const $pass = $("#pass");
			if ($pass.val().trim() == "") {
				$(".pwText").text("비밀번호를 입력해 주세요");
				$pass.focus();
				check =  false;
			}
			
			if(!check){
				$("input").css("margin", "10px 0 5px");
				return false;
			}

			$("input").css("margin-top", "25px");
		})
	})
  </script>
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
        <input type="text" placeholder="아이디를 입력해주세요" name='id' id="id" required autocomplete="username">
        <span class="idText"></span>
        <input type="password" placeholder="비밀번호를 입력해주세요" name='pass' id="pass" required autocomplete="current-password">
        <span class="pwText"></span>
        <div class="options">
          <label class="checkbox-label">
          	<input type="checkbox" name='remember' id="remember" value="store">
          	아이디 저장
          </label>
          <a href="${pageContext.request.contextPath}/emp/pwInquiry">비밀번호 찾기</a>
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
  </div>
</body>
</html>


