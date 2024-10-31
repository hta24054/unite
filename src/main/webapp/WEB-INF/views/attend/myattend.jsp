<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/myattend.js"></script>
    <jsp:include page="../common/header.jsp"/>
    <style>
        select.form-control {
            width: auto;
            margin-bottom: 2em;
            display: inline-block;
        }

        .rows {
            text-align: right;
        }

        .gray {
            color: gray;
        }

        body > div > table > thead > tr:nth-child(2) > th:nth-child(1) {
            width: 8%
        }

        body > div > table > thead > tr:nth-child(2) > th:nth-child(2) {
            width: 50%
        }

        body > div > table > thead > tr:nth-child(2) > th:nth-child(3) {
            width: 14%
        }

        body > div > table > thead > tr:nth-child(2) > th:nth-child(4) {
            width: 17%
        }

        body > div > table > thead > tr:nth-child(2) > th:nth-child(5) {
            width: 11%
        }
    </style>
</head>
<body>
<div class="container">
    <h1>나의 근태 관리</h1>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>당월 근무일</th>
            <th>근무</th>
            <th>휴가</th>
            <th>결근</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>#</td>
            <td>#</td>
            <td>#</td>
            <td>#</td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>
