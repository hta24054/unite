<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/leftbar.css">
    <script src="${pageContext.request.contextPath}/js/leftbar.js"></script>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>

<body>
<div class="sidebar">
    <br> <br>
    <h3 style="color: rgb(51, 68, 102)">주소록</h3>
    <br>
    <ul class="list-group"
        style="list-style-type: disc; padding-left: 20px;">
        <li class="left" style="border: none;">
            <a href="${pageContext.request.contextPath}/contact/addressbook">공용 주소록</a>
        </li>
    </ul>
</div>
</body>
</html>
