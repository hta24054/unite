<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css">
    <jsp:include page="common/header.jsp"/>
</head>
<script>
	//게시판 데이터를 갱신하는 함수
	function loadBoardData() {
	    $.ajax({
	        url: "home",  // 서블릿 URL
	        type: "GET",
	        dataType: "json",
	        success: function(data) {
	            let tableBody = $(".styled-table tbody");
	            tableBody.empty();  // 테이블 내용 초기화
	
	            // 데이터 갱신
	            data.forEach(function(item) {
	                let row = `<tr>
	                             <td>${item.boardName1}</td>
	                             <td>${item.postSubject} ${(item.postUpdateDate)}</td>
	                          </tr>`;
	                tableBody.append(row);
	            });
	        },
	        error: function(xhr, status, error) {
	            console.error("게시판 데이터를 불러오는 데 실패했습니다: " + error);
	        }
	    });
	}
	
	// 페이지 로드 후 3초마다 게시판 갱신
	$(document).ready(function() {
	    loadBoardData();  // 초기 데이터 로드
	    setInterval(loadBoardData, 3000);  // 3초마다 게시판 갱신
	});

</script>
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
