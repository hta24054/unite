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
        /* 컨테이너와 제목 스타일 */
        .container {
            max-width: 1000px;
            margin-top: 20px;
        }

        #main_title {
            color: #334466;
            font-weight: bold;
            margin-bottom: 20px;
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

        #holidayTable th, #holidayTable td {
            text-align: center;
            vertical-align: middle;
            padding: 5px;
            line-height: 1.2;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row">
        <!-- 왼쪽 영역: 제목과 버튼, 폼 -->
        <div class="col-md-4">
            <h2 id="main_title">휴일 설정</h2>

            <div class="button-group">
                <button type="button" class="btn btn-primary btn-block" id="api">공휴일 받아오기</button>
                <button type="button" class="btn btn-primary btn-block" id="weekend">주말 업데이트</button>
            </div>

            <form action="${pageContext.request.contextPath}/admin/holiday/insert" method="post" class="form-group">
                <label>
                    <input type="date" class="form-control mb-2" name="date" required>
                </label>
                <label>
                    <input type="text" class="form-control mb-2" name="holidayName" placeholder="휴일 사유를 입력하세요" required>
                </label>
                <button type="submit" class="btn btn-success btn-block">휴일등록</button>
            </form>
            <form action="${pageContext.request.contextPath}/admin/holiday/delete" method="post" class="form-group">
                <label>
                    <input type="date" class="form-control mb-2" name="date" required>
                </label>
                <button type="submit" class="btn btn-success btn-block">휴일삭제</button>
            </form>
        </div>

        <!-- 오른쪽 영역: 테이블 -->
        <div class="col-md-8">
            <table class="table table-striped table-bordered" id="holidayTable">
                <thead>
                <tr>
                    <th>날짜</th>
                    <th>사유</th>
                    <th>삭제</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="holiday" items="${holidayList}">
                    <tr>
                        <td>${holiday.holidayDate}</td>
                        <td>${holiday.holidayName}</td>
                        <td><button class="btn btn-danger">삭제</button></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
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
