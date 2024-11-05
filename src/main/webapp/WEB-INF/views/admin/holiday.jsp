<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>휴일 관리</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="admin_leftbar.jsp"/>
    <style>
        .container {
            max-width: 600px;
            margin-top: 20px;
        }

        h2 {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
        }

        .btn-block {
            width: 100%;
            margin-bottom: 10px;
        }

        .form-group {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<h2 id="main_title">휴일 설정</h2>
<div class="container">
    <div class="row mb-3">
        <div class="col">
            <button type="button" class="btn btn-primary btn-block" id="api">공휴일 받아오기</button>
        </div>
    </div>
    <div class="row mb-3">
        <div class="col">
            <button type="button" class="btn btn-primary btn-block" id="weekend">주말 업데이트</button>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <form action="${pageContext.request.contextPath}/admin/holiday/insert" class="form-group" method="post">
                <input type="date" class="form-control" name="date" required>
                <input type="text" class="form-control" name="holidayName" placeholder="휴일 사유를 입력하세요" required>
                <button type="submit" class="btn btn-primary">사용자 휴일 등록</button>

            </form>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('#api').click(function () {
            window.location.href = "${pageContext.request.contextPath}/admin/holiday/api";
        });
        $('#weekend').click(function () {
            window.location.href = "${pageContext.request.contextPath}/admin/holiday/weekend";
        });
    });
</script>
</body>
</html>
