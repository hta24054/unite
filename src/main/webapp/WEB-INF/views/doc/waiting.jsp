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
<div class="container text-center">
    <table class="table table-striped table-bordered" id="waiting_table">
        <thead>
        <tr>
            <th style="width: 10%">문서번호</th>
            <th style="width: 15%">기안일</th>
            <th style="width: 15%">문서구분</th>
            <th style="width: 40%">문서제목</th>
            <th style="width: 20%">결재 대기</th>
        </tr>
        </thead>
        <tbody id="waitingTableBody">
        <tr>
            <td colspan="5">데이터를 불러오는 중입니다...</td>
        </tr>
        </tbody>
    </table>
</div>
<script>
    $(document).ready(function () {
        function loadWaitingDocs() {
            $(document).ready(function () {
                const contextPath = '${pageContext.request.contextPath}';

                $.ajax({
                    url: `\${contextPath}/doc/waiting-process`,
                    method: 'GET',
                    success: function (response) {
                        const $tbody = $('#waiting_table tbody');
                        $tbody.empty(); // 기존 데이터 제거

                        // Ensure response.list exists and is an array
                        if (response.list && Array.isArray(response.list)) {
                            response.list.forEach((item, index) => {
                                const doc = item.doc; // doc 객체 가져오기
                                const formattedDate = formatDate(doc.docCreateDate); // 날짜 포맷 함수 호출
                                const row = `
                                            <tr>
                                                <td>\${doc.docId}</td>
                                                <td>\${formattedDate}</td>
                                                <td>\${doc.docType}</td>
                                                <td><a href="\${contextPath}/doc/read?docId=\${doc.docId}">\${doc.docTitle}</a></td>
                                                <td>\${item.signerName}</td>
                                            </tr>
                                            `;
                                $tbody.append(row);
                            });
                        } else {
                            console.error('Invalid response structure:', response);
                        }
                    },
                    error: function (xhr, status, error) {
                        console.error('Error fetching waiting list:', error);
                    }
                });
            });
        }

        // 날짜 포맷 함수
        function formatDate(isoDate) {
            const date = new Date(isoDate);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            return year + '-' + month + '-' + day;
        }

        // 데이터 로드
        loadWaitingDocs();
    });
</script>
</body>
</html>