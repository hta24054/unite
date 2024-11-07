<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 변경</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="mypage_leftbar.jsp"/>
    <style>
        /* 컨테이너와 제목 스타일 */
        .container {
            max-width: 500px;
            margin-top: 20px;
            text-align: center;
        }

        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .form-group {
            text-align: center;
        }
    </style>
</head>
<body>
<h2 id="main_title">비밀번호 변경</h2>
<div class="container">
    <form id = "passwordForm" action="${pageContext.request.contextPath}/mypage/password/process" method="post" class="form-group mx-auto">
        <input type="password" class="form-control mb-2" id="currentPassword" name="currentPassword" placeholder="현재 비밀번호" required>
        <input type="password" class="form-control mb-2" id="newPassword" name="newPassword" placeholder="새 비밀번호"
               required>
        <input type="password" class="form-control mb-2" id="newPasswordVerify" name="newPasswordVerify"
               placeholder="새 비밀번호 확인" required>
        <button type="submit" class="btn btn-primary btn-block">확인</button>
        <button type="reset" class="btn btn-secondary btn-block">취소</button>
    </form>
</div>
<script>
    $(document).ready(function () {
        $('#passwordForm').submit(function (e) {
            const currentPassword = $('#currentPassword').val();
            const newPassword = $('#newPassword').val();
            const newPasswordVerify = $('#newPasswordVerify').val();

            if (newPassword !== newPasswordVerify) {
                e.preventDefault();
                alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
                $('#newPassword').val('').focus();
                $('#newPasswordVerify').val('');
                return false;
            }

            if (currentPassword === newPassword) {
                e.preventDefault();
                alert("새 비밀번호와 기존 비밀번호가 동일합니다.");
                $('#currentPassword').val('').focus();
                $('#newPassword').val('');
                $('#newPasswordVerify').val('');
                return false;
            }
        });
    });
</script>
</body>
</html>
