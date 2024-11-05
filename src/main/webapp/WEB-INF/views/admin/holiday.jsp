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

        /* h2의 기본 여백 제거 */
        caption {
            caption-side: top;
            margin-bottom: 30px;
        }

        #report {
            width: 500px;
            text-align: center;
        }

        #report th {
            width: 25%;
        }

        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
        }
        #specify{
            text-align: center;
        }
        #specify th{
            width: 20%;
        }

        /* 캡션과 테이블 간격 설정 */
    </style>
</head>
<body>
<h2 id="main_title">휴일 관리</h2>
<div class="container">
    <button id="api">공휴일 받아오기</button>
    <button id="weekend">주말 업데이트</button>
    <form action="${pageContext.request.contextPath}/holiday/insert">
        <input type="date">휴일 설정
    </form>
</div>
<script>
    $(document).ready(function (){
        $('#api').click(function (){
            window.href="${pageContext.request.contextPath}/holiday/api"
        })
        $('#weekend').click(function (){
            window.href="${pageContext.request.contextPath}/holiday/weekend"
        })
    })
</script>
</body>
</html>
