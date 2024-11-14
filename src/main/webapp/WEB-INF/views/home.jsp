<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css">
    <jsp:include page="common/header.jsp"/>
</head>
<body>
    <div class="container" style=" max-width: 1500px;">
        <div class="left">
        	<div class="user">
	           	<%--<img src="${pageContext.request.contextPath}/image/${profile.imgPath }${profile.imgType}" style="width:150px; height:150px; border-radius:50%; border: 1px solid gray;" alt="프로필"><br> --%>
	           	<img src="${pageContext.request.contextPath}/image/profile_navy.png" class="user_img" alt="프로필"><br><br>
    	    	<c:out value="${profile.ename }"/> / <c:out value="${job}"/><br>
	        	<c:out value="${profile.email }"/>
	         </div>
             
             <br>
            <jsp:include page="attend/attendButton.jsp"/>
		</div>
            
            
        <!-- 가운데 위 -->
        <div class="center">
        	<div class="c_table">
			    <h3>게시판</h3>
			    <table class="styled-table">
			        <tbody>
			            <tr>
			                <td>[]</td>
			                <td>이미지 홍길동 날짜</td>
			            </tr>
			            <tr>
			                <td>[]</td>
			                <td>이미지 홍길동 날짜</td>
			            </tr>
						<tr>
			                <td>[]</td>
			                <td>이미지 홍길동 날짜</td>
			            </tr>
			            <tr>
			                <td>[]</td>
			                <td>이미지 홍길동 날짜</td>
			            </tr>
			            <tr>
			                <td>[]</td>
			                <td>이미지 홍길동 날짜</td>
			            </tr>
			        </tbody>
			    </table>
			</div>
			<div class="c_table">
	            <h2>결재 대기 문서</h2>
	            include
			</div>
        </div>

        <div class="right">
            <h2>최근 알림</h2>
        </div>
    </div>
</body>
</html>
