<%@ page import="java.time.LocalDate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>근태 관리</title>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="attend_leftbar.jsp"/>
    <style>
        .table {
            width: 70%;
            margin: auto;
        }

        table, td, th {
            border-collapse: collapse;
        }

        h2 {
            text-align: left;
            color: black;
            margin: 0;
        }

        caption {
            caption-side: top;
            margin-bottom: 30px;
        }

        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<h2 id="main_title">전 직원 휴가 관리(인사부서) - 직원 목록</h2>
<div class="container">
    <table class="table table-striped table-bordered shadow-sm p-3 mb-5 bg-body rounded">
        <thead>
        <tr>
            <th>성명</th>
            <th>부서</th>
            <th>직위</th>
            <th>전화번호</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="emp" items="${empList}">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/attend/vacation/emp?empId=${emp.empId}&year=<%=LocalDate.now().getYear()%>">${emp.ename}</a>
                </td>
                <td>${deptMap[emp.deptId]}</td>
                <td>${jobMap[emp.jobId]}</td>
                <td>${emp.tel}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
