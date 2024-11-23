<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>결재 대기 문서</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>

    <script>
        $(document).ready(function () {
            let result = '${message}';
            if (result && result.trim() !== '') {
                alert(result);
                <% session.removeAttribute("message"); %>
            }
        });
    </script>
    <style>
        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        #waiting_table td, th {
            text-align: center;
        }
    </style>
<body>
<h2 id="main_title">결재 대기 문서</h2>
<jsp:include page="waiting_box.jsp"/>
</body>
</html>