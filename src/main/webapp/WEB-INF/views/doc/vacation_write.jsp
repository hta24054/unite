<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>
    <link href="${pageContext.request.contextPath}/css/doc.css" rel="stylesheet">
    <meta charset="UTF-8">
    <title>휴가신청서 작성</title>
    <style>
        #rest_vac_count {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
<form id="doc_form" enctype="multipart/form-data">
    <div class="container mt-4">
        <h4 id="rest_vac_count" data-num="${emp.vacationCount - usedCount}">
            내 잔여 연차일 : ${emp.vacationCount - usedCount}</h4>
        <div class="document-wrapper">
            <div class="text-center mb-4">
                <h1 class="header-cell">휴가신청서</h1>
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
                <div class="col-md-6">
                    <jsp:include page="sign_write.jsp"/>
                </div>
            </div>

            <table class="table table-bordered mt-4 item_table" id="itemTable">
                <tr>
                    <th class="table-secondary font-weight-bold text-center">제목</th>
                    <td>휴가신청서(${emp.ename})</td>
                </tr>
                <tr>
                    <th class="table-secondary font-weight-bold text-center">휴가 종류</th>
                    <td>
                        <select name="type" class="form-control">
                            <option value="연차">연차</option>
                            <option value="병가">병가</option>
                            <option value="공가">공가</option>
                            <option value="경조">경조</option>
                            <option value="기타">기타</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="table-secondary font-weight-bold text-center">시작일</th>
                    <td><input type="date" class="form-control title-input" data-name="시작일" name="vacation_start"
                               required></td>
                </tr>
                <tr>
                    <th class="table-secondary font-weight-bold text-center">종료일</th>
                    <td><input type="date" class="form-control title-input" data-name="종료일" name="vacation_end"
                               required></td>
                </tr>
                <tr>
                    <th class="table-secondary font-weight-bold text-center">휴가 일수</th>
                    <td><input type="text" name="vacation_count" required readonly></td>
                </tr>
                <tr>
                    <th class="table-secondary font-weight-bold text-center">휴가 사유</th>
                    <td><input type="text" name="content" class="form-control title-input" data-name="휴가사유"
                               placeholder="휴가 사유를 입력하세요" required></td>
                </tr>
                <tr>
                    <th class="table-secondary font-weight-bold text-center">첨부 파일</th>
                    <td><input type="file" name="file"></td>
                </tr>
                <tr>
                    <th colspan="2" class="table-secondary font-weight-bold" id="info">
                        1. 연차의 사용은 근로기준법에 따라 전년도에 발생한 개인별 잔여 연차에 한하여 사용함을 원칙으로 한다.<br>
                        단, 최초 입사시에는 근로기준법에 따라 발생 예정된 연차를 사용하여 월 1회 사용할 수 있다.<br>
                        2. 경조사 휴가는 증빙서류를 제출할 수 있는 가족관계증명서 또는 청첩장 등 제출<br>
                        3. 공가(예비군/민방위)는 사전에 통지서를, 사후에 참석증을 반드시 제출
                    </th>
                </tr>
            </table>
        </div>
        <div class="button-container">
            <button type="button" form="doc_form" class="btn btn-success" id="submit-button">작성완료</button>
            <button type="reset" class="btn btn-secondary">초기화</button>
        </div>
    </div>
</form>
<script>
    $(document).ready(function () {
        // 시작일과 종료일 필드에 change 이벤트 리스너 추가
        $('input[name="vacation_start"], input[name="vacation_end"]').on('change', function () {
            const startDate = $('input[name="vacation_start"]').val();
            const endDate = $('input[name="vacation_end"]').val();

            if (startDate && endDate) {
                if (new Date(endDate) < new Date(startDate)) {
                    alert("종료일은 시작일 이후여야 합니다. 다시 선택해 주세요.");
                    $('input[name="vacation_start"]').val('');
                    $('input[name="vacation_end"]').val('');
                    $('input[name="vacation_count"]').val('');
                    return;
                }

                $.ajax({
                    url: "${pageContext.request.contextPath}/doc/countVacation",
                    type: "POST",
                    data: {startDate: startDate, endDate: endDate},
                    dataType: "json",
                    success: function (response) {
                        $('input[name="vacation_count"]').val(response.dayCount);
                    },
                    error: function () {
                        alert("연차 일수를 계산하는 중 오류가 발생했습니다.");
                    }
                });
            }
        });

        $("#file").change(function () {
            const maxSizeInBytes = 5 * 1024 * 1024;
            const file = this.files[0];
            if (file.size > maxSizeInBytes) {
                alert("업로드 파일 용량 제한 : 5MB");
                $(this).val('');
            }
        });

        // 제출 버튼 클릭 시 실행
        $('#submit-button').on('click', function (event) {
            event.preventDefault();

            // 모든 required 필드가 채워졌는지 검사
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

            const restVacCount = $('#rest_vac_count').data('num');
            if (restVacCount < $('input[name="vacation_count"]').val()
                && $('select[name=type]').val()==='연차') {
                alert("잔여 연차일수가 부족합니다.");
                $('input[name="vacation_start"]').val('');
                $('input[name="vacation_end"]').val('');
                $('input[name="vacation_count"]').val('');
                return false;
            }

            // 결재자 수 확인
            const additionalSigners = $('input[name="sign[]"]').length - 1;
            if (additionalSigners < 1) {
                alert("본인 이외에 최소 1명의 결재자를 추가해야 합니다.");
                return;
            }

            const confirmSubmission = confirm("문서를 작성하시겠습니까?");
            if (!confirmSubmission) return;

            // FormData 객체 생성
            const formElement = document.getElementById("doc_form");
            const formData = new FormData(formElement);

            // 결재자 정보 추가
            $('input[name="sign[]"]').each(function () {
                formData.append("signers[]", $(this).val());
            });

            // AJAX 요청
            $.ajax({
                url: "${pageContext.request.contextPath}/doc/vacation_write",
                type: "POST",
                data: formData,
                processData: false,   // 폼 데이터 객체 사용 시 필요
                contentType: false,   // 폼 데이터 객체 사용 시 필요
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