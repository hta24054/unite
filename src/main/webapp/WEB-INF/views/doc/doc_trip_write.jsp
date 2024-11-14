<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>

    <meta charset="UTF-8">
    <title>출장명령부 작성</title>
    <style>
        .header-cell {
            font-size: 36px;
            font-weight: bold;
            padding: 20px 0;
        }

        .title-input, .form-control {
            font-size: 16px;
        }

        /* 테두리 색상을 검정색으로 설정 */
        .table-bordered, .table-bordered td, .table-bordered th {
            align-content: center;
            border-color: black !important;
        }
    </style>
</head>
<body>
<form id="doc_form" action="${pageContext.request.contextPath}/doc/trip_write" method="POST">
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
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">부&nbsp;&nbsp;&nbsp;서</td>
                        <td>${dept.deptName}</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">신 청 자</td>
                        <td>${emp.ename}</td>
                        <input type="hidden" name="writer" value="${emp.empId}">
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">작 성 일</td>
                        <td>${emp.ename}</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">작 성 일</td>
                        <td>${today}</td>
                    </tr>
                </table>
            </div>
            <!-- 결재자 테이블 -->
            <div class="col-md-6">
                <jsp:include page="sign_write.jsp"/>
            </div>
        </div>

        <!-- 기타 정보 테이블 -->
        <table class="table table-bordered mt-4">
            <tr>
                <td class="table-secondary font-weight-bold text-center">출장 시작일</td>
                <td><input type="date" class="form-control title-input" name="trip_start" required></td>
            </tr>
            <tr>
                <td class="table-secondary font-weight-bold text-center">출장 종료일</td>
                <td><input type="date" class="form-control title-input" name="trip_end" required></td>
            </tr>
            <tr>
                <td class="table-secondary font-weight-bold text-center">출장지</td>
                <td><input type="text" class="form-control title-input" name="trip_loc" placeholder="출장지를 입력하세요" required></td>
            </tr>
            <tr>
                <td class="table-secondary font-weight-bold text-center">출장지 연락처</td>
                <td><input type="text" class="form-control title-input" name="trip_phone" placeholder="출장지를 입력하세요" required></td>
            </tr>
            <tr>
                <td class="table-secondary font-weight-bold text-center">목적 및 내용</td>
                <td><input type="text" class="form-control title-input" name="trip_info" placeholder="출장지를 입력하세요" required></td>
            </tr>
        </table>
        <table class="table table-bordered mt-4">
            <tr>
                <td colspan="4" class="table-secondary font-weight-bold text-center">※법인카드 사용 신청(승인 후 재무팀에서 수령)</td>
            </tr>
            <tr>
                <td class="table-secondary font-weight-bold text-center">사용 시작일</td>
                <td><input type="date" class="form-control title-input" name="card_start"></td>

                <td class="table-secondary font-weight-bold text-center">사용 종료일</td>
                <td><input type="date" class="form-control title-input" name="card_end"></td>
            </tr>
            <tr>
                <td class="table-secondary font-weight-bold text-center">사용 예정자</td>
                <td>${emp.ename}</td>
                <td class="table-secondary font-weight-bold text-center">반납 예정일</td>
                <td><input type="date" class="form-control title-input" name="card_return"></td>
            </tr>
        </table>
    </div>
    <!-- 버튼 영역 -->
    <div class="text-right mt-3">
        <button type="submit" form="doc_form" class="btn btn-success">결재 상신</button>
        <button type="reset" form="doc_form" class="btn btn-secondary">초기화</button>
    </div>
</form>
<script>
    $(document).ready(function() {
        const $tripStartInput = $("input[name='trip_start']");
        const $tripEndInput = $("input[name='trip_end']");
        const $cardStartInput = $("input[name='card_start']");
        const $cardEndInput = $("input[name='card_end']");
        const $cardReturnInput = $("input[name='card_return']");

        $tripStartInput.on("change", function() {
            $cardStartInput.val($tripStartInput.val());
        });

        // 출장 종료일이 변경되면 카드 사용 종료일을 동일하게 설정
        $tripEndInput.on("change", function() {
            $cardEndInput.val($tripEndInput.val());
            $cardReturnInput.val($tripEndInput.val());
        });

        $('#doc_form').on('submit', function (event) {
            const confirmSubmission = confirm("문서를 작성하시겠습니까?");
            if (!confirmSubmission) {
                event.preventDefault(); // "취소"를 누르면 제출 취소
            }
        });
    });
</script>
</body>
</html>
