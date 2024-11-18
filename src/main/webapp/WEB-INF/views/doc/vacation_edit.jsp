<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>
    <meta charset="UTF-8">
    <title>휴가신청서 작성</title>
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

        #info {
            text-align: left;
        }
    </style>
</head>
<body>
<form id="doc_form" enctype="multipart/form-data">
    <div class="container mt-4">
        <div class="text-center mb-4">
            <h1 class="header-cell">휴가신청서</h1>
        </div>

        <div class="row">
            <!-- 문서 정보 테이블 -->
            <div class="col-md-6 mb-3">
                <table class="table table-bordered">
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">문서번호</td>
                        <td><input type="text" name="docId" value="${doc.docId}" readonly></td>
                        <input type="hidden" name="docVacationId" value="${doc.docVacationId}">
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">부&nbsp;&nbsp;&nbsp;서</td>
                        <td>${dept.deptName}</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">기 안 자</td>
                        <td>${writer.ename}</td>
                        <input type="hidden" name="writer" value="${writer.empId}">
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">작 성 일</td>
                        <td>${doc.docCreateDate}</td>
                    </tr>
                </table>
            </div>
            <!-- 결재자 테이블 -->
            <div class="col-md-6">
                <jsp:include page="sign_edit.jsp"/>
            </div>
        </div>

        <table class="table table-bordered mt-4 item_table" id="itemTable">
            <tr>
                <th class="table-secondary font-weight-bold text-center">제목</th>
                <td>휴가신청서(${writer.ename})</td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">휴가 종류</th>
                <td>
                    <select name="type" class="form-control" id="typeSelect" data-selected="${doc.vacationType}">
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
                           value="${doc.vacationStart}" required></td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">종료일</th>
                <td><input type="date" class="form-control title-input" data-name="종료일" name="vacation_end"
                           value="${doc.vacationEnd}" required></td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">휴가 일수</th>
                <td><input type="text" name="vacation_count" value="${doc.vacationCount}" required readonly></td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">휴가 사유</th>
                <td><input type="text" name="content" class="form-control title-input" data-name="휴가사유"
                           placeholder="휴가 사유를 입력하세요" value="${doc.docContent}" required></td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">첨부 파일</th>
                <td>현재 파일 : <c:if test="${!empty doc.vacationFileOriginal}">
                    <span style="font-weight: bold; color: blue" id="fileName">${doc.vacationFileOriginal}</span>
                    <img src="${pageContext.request.contextPath}/image/cancel.png" alt="파일삭제"
                         width="20px" class="remove"/>
                </c:if>
                    <c:if test="${empty doc.vacationFileOriginal}">
                        첨부파일 없음
                    </c:if>
                    <br>파일 변경 : <input type="file" name="file" id="file"></td>
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
    <div class="text-right mt-3">
        <button type="button" form="doc_form" class="btn btn-success" id="submit-button">결재 상신</button>
        <button type="reset" class="btn btn-secondary">초기화</button>
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

        //휴가유형 초기화
        const $selectElement = $("#typeSelect");
        const selectedValue = $selectElement.data("selected");
        if (selectedValue) {
            $selectElement.val(selectedValue);
        }

        let fileChange = 0;

        $("#file").change(function () {
            fileChange++; //파일이 변경되면 fileChange 값 0 -> 1
            const maxSizeInBytes = 5 * 1024 * 1024;
            const file = this.files[0];
            if (file.size > maxSizeInBytes) {
                alert("업로드 파일 용량 제한 : 5MB");
                $(this).val('');
            }
        });

        $('.remove').click(function () {
            if (confirm("첨부파일을 삭제하시겠습니까?")) {
                $("#fileName").text('');
                $(this).css('display', 'none');
            }
        })

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

            // 결재자 수 확인
            const additionalSigners = $('input[name="sign[]"]').length - 1;
            if (additionalSigners < 1) {
                alert("본인 이외에 최소 1명의 결재자를 추가해야 합니다.");
                return;
            }

            const confirmSubmission = confirm("문서를 작성하시겠습니까?");
            if (!confirmSubmission) return;

            //파일이 변경 안되었으면 -> 기존 파일로 보내줘야됨
            if (fileChange === 0) {
                const fileName = $('#fileName').text();
                const html = '<input type = "hidden" value=' + fileName + ' name="beforeFileName">';
                $(this).append(html);
            }

            const formData = new FormData($('#doc_form')[0]);

            // 결재자 정보 추가
            $('input[name="sign[]"]').each(function () {
                formData.append("signers[]", $(this).val());
            });

            // AJAX 요청
            $.ajax({
                url: "${pageContext.request.contextPath}/doc/vacation_edit",
                type: "POST",
                data: formData,
                processData: false,   // 폼 데이터 객체 사용 시 필요
                contentType: false,   // 폼 데이터 객체 사용 시 필요
                success: function (response) {
                    if (response.status === 'success') {
                        alert("문서 수정이 완료되었습니다.");
                        window.location.href = "${pageContext.request.contextPath}/doc/in-progress";
                    } else {
                        alert("문서 수정 중 오류가 발생했습니다.");
                    }
                },
                error: function () {
                    alert("문서 수정 중 오류가 발생했습니다.");
                }
            });
        });
    });
</script>
</body>
</html>