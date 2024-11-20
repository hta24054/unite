<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>근태관리</title>
</head>
<body>
<div class="sidebar">
    <h3 style="color:rgb(51, 68, 102)">근태관리</h3><br>
    <jsp:include page="attendButton.jsp"/>
    <ul class="list-group" style="list-style-type: disc; padding-left: 20px;">
        <li class="left"><a href="${pageContext.request.contextPath}/attend/my">나의 근태관리</a></li>
        <c:if test="${manager}">
            <li class="left"><a href="${pageContext.request.contextPath}/attend/empList">부서원 근태관리</a></li>
        </c:if>
        <li class="left"><a href="${pageContext.request.contextPath}/attend/vacation/my">휴가 관리</a></li>
        <c:if test="${hrDept}">
            <li class="left"><a href="${pageContext.request.contextPath}/attend/vacation/empList">휴가 관리(인사)</a></li>
        </c:if>
    </ul>
</div>
</body>
</html>