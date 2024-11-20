<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/leftbar.css">
    <script src="${pageContext.request.contextPath}/js/leftbar.js"></script>
    <title>Insert title here</title>
</head>
<body>
<div class="sidebar">
    <h3 style="color:rgb(51, 68, 102)">관리자</h3><br>
    <ul class="list-group" style="list-style-type: disc; padding-left: 20px;">
        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/admin/emp-manage">직원 관리</a>
        </li>
        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/admin/holiday">휴일 설정</a></li>
        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/admin/notice">공지사항</a></li>
        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/admin/resource">자원 관리</a>
        </li>
    </ul>
</div>
</body>
</html>