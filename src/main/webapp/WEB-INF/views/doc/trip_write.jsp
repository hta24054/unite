<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>
    <script src="${pageContext.request.contextPath }/js/doc_trip.js"></script>

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

        .table-bordered, .table-bordered td, .table-bordered th {
            align-content: center;
            border-color: black !important;
        }
    </style>
</head>
<body>
<form id="doc_form" action="${pageContext.request.contextPath}/doc/trip_write" method="POST">
    <div class="container mt-4">
        <div class="text-center mb-4">
            <h1 class="header-cell">출장명령부</h1>
        </div>

        <div class="row">
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
                        <td>${today}</td>
                    </tr>
                </table>
            </div>
            <!-- 결재자 -->
            <div class="col-md-6">
                <jsp:include page="sign_write.jsp"/>
            </div>
        </div>

        <!-- 기타 정보 테이블 -->
        <table class="table table-bordered mt-4">
            <tr>
                <td class="table-secondary font-weight-bold text-center">출장 시작일</td>
                <td><input type="date" class="form-control title-input" name="trip_start" data-name="출장 시작일" required>
                </td>
            </tr>
            <tr>
                <td class="table-secondary font-weight-bold text-center">출장 종료일</td>
                <td><input type="date" class="form-control title-input" name="trip_end" data-name="출장 종료일" required>
                </td>
            </tr>
            <tr>
                <td class="table-secondary font-weight-bold text-center">출장지</td>
                <td><input type="text" class="form-control title-input" name="trip_loc" placeholder="출장지를 입력하세요"
                           data-name="출장지" maxlength="120" required></td>
            </tr>
            <tr>
                <td class="table-secondary font-weight-bold text-center">출장지 연락처</td>
                <td><input type="text" class="form-control title-input" name="trip_phone" placeholder="출장지를 입력하세요"
                           data-name="출장지 연락처" maxlength="13" required></td>
            </tr>
            <tr>
                <td class="table-secondary font-weight-bold text-center">목적 및 내용</td>
                <td><input type="text" class="form-control title-input" name="trip_info" placeholder="내용을 입력하세요"
                           data-name="내용" maxlength="255" required></td>
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
        <button type="button" form="doc_form" class="btn btn-success" id="submit-button">결재 상신</button>
        <button type="reset" form="doc_form" class="btn btn-secondary">초기화</button>
    </div>
</form>
<script>

    $(document).ready(function () {
        // 제출 버튼 클릭 시 실행
        $('#submit-button').on('click', function (event) {
            event.preventDefault();

            // 필수 입력 필드 유효성 검사
            let isValid = true;
            $('#doc_form [required]').each(function () {
                if ($(this).val() === '') {
                    const errorMessage = $(this).data('name');
                    alert(errorMessage + '을(를) 입력해 주세요');
                    $(this).focus();
                    isValid = false;
                    return false;
                }
            });
            if (!isValid) return;

            // 결재자 수 확인
            const additionalSigners = $('input[name="sign[]"]').length - 1;
            if (additionalSigners < 1) {
                alert("본인 이외에 최소 1명의 결재자를 추가해야 합니다.");
                return;
            }

            // 제출 확인 메시지
            const confirmSubmission = confirm("문서를 작성하시겠습니까?");
            if (!confirmSubmission) return;
            // 폼 데이터 수집
            const formData = {
                writer: $('input[name="writer"]').val(),
                trip_start: $('input[name="trip_start"]').val(),
                trip_end: $('input[name="trip_end"]').val(),
                trip_loc: $('input[name="trip_loc"]').val(),
                trip_phone: $('input[name="trip_phone"]').val(),
                trip_info: $('input[name="trip_info"]').val(),
                card_start: $('input[name="card_start"]').val(),
                card_end: $('input[name="card_end"]').val(),
                card_return: $('input[name="card_return"]').val(),
                signers: []
            };

            // 결재자 정보 추가
            $('input[name="sign[]"]').each(function () {
                formData.signers.push($(this).val());
            });

            // AJAX 요청
            $.ajax({
                url: "${pageContext.request.contextPath}/doc/trip_write",
                type: "POST",
                data: JSON.stringify(formData),
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function (response) {
                    if (response.status === 'success') {
                        alert("문서 작성이 완료되었습니다.");
                        window.location.href = "${pageContext.request.contextPath}/doc/in-progress";
                    } else {
                        alert("문서 작성 중 오류가 발생했습니다.");
                    }
                },
                error: function () {
                    alert("문서 작성 중 오류가 발생했습니다.");
                }
            });
        });
    });
</script>
</body>
</html>
