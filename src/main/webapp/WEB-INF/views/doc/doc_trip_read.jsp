<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>

    <meta charset="UTF-8">
    <title>출장명령부</title>
    <style>
        .header-cell {
            font-size: 36px;
            font-weight: bold;
            padding: 20px 0;
        }

        /* 테두리 색상을 검정색으로 설정 */
        .table-bordered, .table-bordered td, .table-bordered th {
            align-content: center;
            border-color: black !important;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <!-- 기안용지 제목 -->
    <div class="text-center mb-4">
        <h1 class="header-cell">출장명령부</h1>
    </div>

    <!-- 문서 정보 및 결재자 테이블을 좌우로 배치 -->
    <div class="row">
        <!-- 문서 정보 테이블 -->
        <div class="col-md-6 mb-3">
            <table class="table table-bordered">
                <tr>
                    <td class="table-secondary font-weight-bold text-center">문서번호</td>
                    <td>${doc.docId}</td>
                </tr>
                <tr>
                    <td class="table-secondary font-weight-bold text-center">부&nbsp;&nbsp;&nbsp;서</td>
                    <td>${dept.deptName}</td>
                </tr>
                <tr>
                    <td class="table-secondary font-weight-bold text-center">신 청 자</td>
                    <td>${writer.ename}</td>
                    <input type="hidden" name="writer" value="${writer.empId}">
                </tr>
                <tr>
                    <td class="table-secondary font-weight-bold text-center">작 성 일</td>
                    <td>${doc.docCreateDate.toLocalDate()}</td>
                </tr>
            </table>
        </div>
        <!-- 결재자 테이블 -->
        <div class="col-md-6">
            <jsp:include page="sign_read.jsp"/>
        </div>
    </div>

    <!-- 기타 정보 테이블 -->
    <table class="table table-bordered mt-4">
        <tr>
            <td class="table-secondary font-weight-bold text-center">출장 시작일</td>
            <td>${doc.tripStart}</td>
        </tr>
        <tr>
            <td class="table-secondary font-weight-bold text-center">출장 종료일</td>
            <td>${doc.tripEnd}</td>
        </tr>
        <tr>
            <td class="table-secondary font-weight-bold text-center">출장지</td>
            <td>${doc.tripLoc}</td>
        </tr>
        <tr>
            <td class="table-secondary font-weight-bold text-center">출장지 연락처</td>
            <td>${doc.tripPhone}</td>
        </tr>
        <tr>
            <td class="table-secondary font-weight-bold text-center">목적 및 내용</td>
            <td>${doc.tripInfo}</td>
        </tr>
    </table>
    <table class="table table-bordered mt-4">
        <tr>
            <td colspan="4" class="table-secondary font-weight-bold text-center">※법인카드 사용 신청(승인 후 재무팀에서 수령)</td>
        </tr>
        <tr>
            <td class="table-secondary font-weight-bold text-center">사용 시작일</td>
            <td>${doc.cardStart}</td>

            <td class="table-secondary font-weight-bold text-center">사용 종료일</td>
            <td>${doc.cardEnd}</td>
        </tr>
        <tr>
            <td class="table-secondary font-weight-bold text-center">사용 예정자</td>
            <td>${writer.ename}</td>
            <td class="table-secondary font-weight-bold text-center">반납 예정일</td>
            <td>${doc.cardReturn}</td>
        </tr>
    </table>
</div>
<!-- 버튼 영역 -->
<jsp:include page="doc_read_button.jsp"/>
</body>
</html>
