<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>직원 관리</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="admin_leftbar.jsp"/>
    <script>
        let result = '${message}';
        if (result !== '') {
            alert(result);
            <%session.removeAttribute("message");%>
        }
    </script>
    <style>
        .container {
            max-width: 1000px;
            margin-top: 20px;
        }

        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        /* 버튼 그룹 스타일 */
        .button-group {
            display: flex;
            flex-direction: column;
            gap: 10px;
            margin-bottom: 20px;
        }

        /* 테이블 스타일 */
        #holidayTable {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<h2 id="main_title">직원 관리</h2>
<div class="container">
구현 예정
</div>
</body>
</html>
