<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Unite</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/popup.css">
    <jsp:include page="common/header.jsp"/>
    <script>
        const contextPath = "${pageContext.request.contextPath}";
        const notices = [
            <c:forEach items="${noticeList}" var="notice">
            {
                id: '${notice.noticeId}',
                content: '${notice.noticeContent}'
            },
            </c:forEach>
        ];
        console.log(notices);
    </script>
    <script src="${pageContext.request.contextPath }/js/home.js"></script>
    <script src="${pageContext.request.contextPath}/js/boardHome.js"></script>
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
            <h3><a href="${contextPath }"></a>게시판</h3>
            <table class="styled-table">
                <tbody>
                <tr><td>게시글이 없습니다</td><td></td></tr>
                <tr><td>게시글이 없습니다</td><td></td></tr>
                <tr><td>게시글이 없습니다</td><td></td></tr>
                <tr><td>게시글이 없습니다</td><td></td></tr>
                </tbody>
            </table>
        </div>
        <div class="c_table">
            <h3>결재 대기 문서</h3><br>
            <jsp:include page="doc/waiting_box.jsp"/>
        </div>
    </div>

    <div class="right">
        <h2>최근 알림</h2>
    </div>
</div>
<!-- 팝업 컨테이너 -->
<div id="popup-container">
    <p id="popup-content"></p>
    <div class="button-container">
        <button id="popup-close" class="btn btn-sm btn-light" style="font-size: 10px">닫기</button>
        <button id="popup-dont-show" class="btn btn-sm btn-secondary" style="font-size: 10px">모든 공지사항 하루 보지 않기</button>
    </div>
</div>
</body>
</html>


