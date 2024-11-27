<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>직원 수정</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/empInfo.css">
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="admin_leftbar.jsp"/>
    <style>
        table, .table {
            width: 80%;
            border-collapse: collapse;
            margin: auto;
        }

        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }

        table {
            margin-top: 10px;
        }

        td, th {
            border: 2px solid black;
            padding: 15px;
            color: black;
            vertical-align: middle;
            text-align: center;
            width: 14%;
        }

        #tr {
            border-top: 2px solid black;
        }

        h2 {
            text-align: left;
            color: rgb(51, 68, 102);
            margin: 0;
        }

        /* h2의 기본 여백 제거 */
        caption {
            caption-side: top;
            margin-bottom: 30px;
        }

        /* 캡션과 테이블 간격 설정 */
        #main_title {
            color: #334466;
            margin-left: 500px;
            /*margin-bottom: 20px;*/
            font-weight: bold;
        }

        .main-container {
            padding-top: 0;
        }

        .cert-item, .lang-item {
            display: flex;
            align-items: center;
            margin-bottom: 10px; /* 각 행 간격 추가 */
        }

        .cert-input, .lang-input {
            flex: 1;
        }

        .cert-add, .lang-add {
            position: absolute;
            right: 10px;
            bottom: 10px;
        }

        #certifications, #languages {
            position: relative;
            padding-bottom: 40px; /* 추가 버튼과 입력 필드 간 간격 확보 */
        }

        button {
            margin-left: 10px;
        }

        .cert-item button, .lang-item button {
            margin-left: 5px;
        }

        .table, .table th, .table td {
            border: 2px solid black;
        }

        td, th {
            border: 2px solid black;
            padding: 4px; /* 기존 15px에서 줄임 */
            line-height: 1.5; /* 텍스트 줄 간격 설정 */
            color: black;
            vertical-align: middle; /* 텍스트 수직 중앙 정렬 */
            text-align: center;
            width: 14%;
            height: 40px; /* 최소 높이 설정 */
        }

        .table, .table th, .table td {
            border: 2px solid black;
        }

        .button-container {
            width: 80%; /* 테이블과 동일한 너비로 설정 */
            margin: 10px auto 0; /* 위쪽 간격 추가, 중앙 정렬 */
            text-align: right; /* 버튼을 오른쪽 정렬 */
        }
    </style>
</head>
<body>
<h2 id="main_title">직원 수정</h2>
<div class="main-container">
    <form id="emp-form" enctype="multipart/form-data">
        <div class="input-group input-group-sm mb-3">
            <table class="table">
                <tr id="tr">
                    <th rowspan="4" id="photo">
                        <span id="showImage">
                                <c:set var="src"
                                       value='${pageContext.request.contextPath}/emp/profile-image?UUID=${emp.imgUUID}'/>
                            <c:if test="${!empty emp.imgUUID}">
                                <img src="${pageContext.request.contextPath}/image/cancel.png" alt="파일삭제"
                                     width="20px" class="remove_img"/>
                            </c:if>
                            <img src="${src}" width="200px" alt="profile" id="over_view"/>
                        </span>
                        <br>
                        현재 파일 : <span id="fileName">${emp.imgOriginal}</span>
                        <br>
                        <input type="file" name="file" id="file" accept="image/*">
                    </th>
                    <th>이름</th>
                    <th>성별</th>
                    <th>이메일</th>
                    <th>내선번호</th>
                </tr>
                <tr>
                    <td><input type="text" value="${emp.ename}" name="ename" data-name="이름" placeholder="이름" required>
                    </td>
                    <td>
                        <select name="gender">
                            <option value="남" ${emp.gender == '남' ? 'selected' : ''}>남성</option>
                            <option value="여" ${emp.gender == '여' ? 'selected' : ''}>여성</option>
                        </select>
                    </td>
                    <td><input type="text" name="email" placeholder="이메일" data-name="이메일" value="${emp.email}" required>
                    </td>
                    <td><input type="text" name="tel" placeholder="02-0000-0000" maxlength="13" data-name="내선번호"
                               value="${emp.tel}" required></td>
                </tr>
                <tr>
                    <th>부서</th>
                    <th>사번</th>
                    <th>직책</th>
                    <th>휴대폰번호</th>
                </tr>
                <tr>
                    <td><select name="deptId" data-name="부서" required>
                        <c:forEach var="dept" items="${deptList}">
                            <c:choose>
                                <c:when test="${fn:endsWith(dept.deptName, '본부')}">
                                    <option value="${dept.deptId}" ${emp.deptId == dept.deptId ? 'selected' : ''}>
                                        &nbsp;&nbsp;${dept.deptName}</option>
                                </c:when>
                                <c:when test="${fn:endsWith(dept.deptName, '팀')}">
                                    <option value="${dept.deptId}" ${emp.deptId == dept.deptId ? 'selected' : ''}>
                                        &nbsp;&nbsp;&nbsp;&nbsp;${dept.deptName}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${dept.deptId}" ${emp.deptId == dept.deptId ? 'selected' : ''}>
                                            ${dept.deptName}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select></td>
                    <td><input type="text" name="empId" value="${emp.empId}" placeholder="사번" data-name="사번" readonly>
                    </td>
                    <td><select name="jobId" data-name="직책" required>
                        <c:forEach var="job" items="${jobList}">
                            <option value="${job.jobId}"
                                ${emp.jobId == job.jobId ? 'selected' : ''}>${job.jobName}</option>
                        </c:forEach>
                    </select></td>
                    <td><input type="text" name="mobile" value="${emp.mobile}" data-name="휴대전화"
                               placeholder="010-0000-0000"
                               maxlength="13" required></td>
                </tr>
            </table>

            <table>
                <tr>
                    <th>입사일</th>
                    <td><input type="date" name="hireDate" value="${emp.hireDate}" data-name="입사일" required></td>

                    <th rowspan="2">계좌번호</th>
                    <td rowspan="2"><input type="text" name="bank" value="${emp.bank}" placeholder="은행명"
                                           data-name="은행" required>
                        <input type="text" name="account" value="${emp.account}" placeholder="계좌번호"
                               data-name="계좌번호" required></td>
                    <th>긴급연락처</th>
                    <td><input type="text" name="mobile2" value="${emp.mobile2}" data-name="긴급연락처"
                               placeholder="010-0000-0000" maxlength="13" required></td>
                </tr>
                <tr>
                    <th>채용구분</th>
                    <td><select name="hireType">
                        <option value="신입"
                        ${emp.hireType == '신입' ? 'selected' : ''}>신입
                        </option>
                        <option value="경력"
                        ${emp.hireType == '경력' ? 'selected' : ''}>경력
                        </option>
                        <option value="인턴"
                        ${emp.hireType == '인턴' ? 'selected' : ''}>인턴
                        </option>
                    </select></td>
                    <th>직원구분</th>
                    <td><select name="etype">
                        <option value="정규직"
                        ${emp.etype == '정규직' ? 'selected' : ''}>정규직
                        </option>
                        <option value="계약직"
                        ${emp.etype == '계약직' ? 'selected' : ''}>계약직
                        </option>
                        <option value="퇴직"
                        ${emp.etype == '퇴직' ? 'selected' : ''}>퇴직
                        </option>
                    </select></td>
                </tr>
                <tr>
                    <th>생년월일</th>
                    <td>
                        <input type="date" name="birthday" placeholder="YYYY/MM/DD" value="${emp.birthday}"
                               data-name="생일" required>
                        <select name="birthday_type">
                            <option value="양력" ${emp.birthdayType == '양력' ? 'selected' : ''}>양력</option>
                            <option value="음력" ${emp.birthdayType == '음력' ? 'selected' : ''}>음력</option>
                        </select>
                    </td>
                    <th>주소</th>
                    <td><input type="text" name="address" value="${emp.address}" data-name="주소" placeholder="주소"
                               required>
                    </td>
                    <th>자격증</th>
                    <td id="certifications" style="position: relative;">
                        <c:forEach var="cert" items="${certList}">
                            <div class="cert-item">
                                <input type="text" name="cert" value="${cert.certName}"
                                       class="form-control cert-input" data-name="자격증" placeholder="자격증명" required>
                                <button type="button" class="btn btn-sm btn-light remove-cert">-</button>
                            </div>
                        </c:forEach>
                        <!-- 추가 버튼 -->
                        <div class="cert-add">
                            <button type="button" class="btn btn-sm btn-light add-cert">추가</button>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th>최종학력</th>
                    <td><input type="text" name="school" value="${emp.school}" data-name="최종학력" placeholder="최종학력"
                               required>
                    </td>
                    <th>결혼여부</th>
                    <td>
                        <select name="married">
                            <option value="1" ${emp.married == '1' ? 'selected' : ''}>Y</option>
                            <option value="0" ${emp.married == '0' ? 'selected' : ''}>N</option>
                        </select>
                    </td>
                    <th rowspan="2">외국어능력</th>
                    <td id="languages" rowspan="2" style="position: relative;">
                        <c:forEach var="lang" items="${langList}">
                            <div class="lang-item">
                                <input type="text" name="lang" value="${lang.langName}"
                                       class="form-control lang-input" data-name="외국어" placeholder="외국어능력" required>
                                <button type="button" class="btn btn-sm btn-light remove-lang">-</button>
                            </div>
                        </c:forEach>
                        <!-- 추가 버튼 -->
                        <div class="lang-add">
                            <button type="button" class="btn btn-sm btn-light add-lang">추가</button>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th>전공</th>
                    <td><input type="text" name="major" placeholder="전공" value="${emp.major}"></td>
                    <th>자녀</th>
                    <td><select name="child" data-name="자녀유무" required>
                        <option value="0" ${emp.child == '0' ? 'selected' : ''}>N</option>
                        <option value="1" ${emp.child == '1' ? 'selected' : ''}>Y</option>
                    </select></td>
                </tr>
            </table>
        </div>
        <div class="button-container">
            <button type="button" id="submit-button" class="btn btn-success">수정완료</button>
            <button type="button" onclick="history.back()" class="btn btn-secondary">뒤로가기</button>
        </div>
    </form>
</div>
<script>
    $(document).ready(function () {
        let fileChange = 0;

        $("#file").change(function (event) {
            fileChange++; //파일이 변경되면 fileChange 값 0 -> 1
            const maxSizeInBytes = 5 * 1024 * 1024;
            const file = this.files[0];
            if (file.size > maxSizeInBytes) {
                alert("업로드 파일 용량 제한 : 5MB");
                $(this).val('');
            }
            const fileURL = URL.createObjectURL(file);
            $('#over_view').attr('src', fileURL);
        });

        $('.remove_img').click(function () {
            if (confirm("첨부파일을 삭제하시겠습니까?")) {
                $("#fileName").text('');
                $(this).css('display', 'none');
                $("#over_view").attr('src', '${pageContext.request.contextPath}/image/profile_navy.png');
            }
        })

        $('#submit-button').on('click', function (event) {
            if (!confirm("직원을 수정하시겠습니까?")) {
                return false;
            }
            event.preventDefault();

            // 필수 입력 필드 유효성 검사
            let isValid = true;
            $('#emp-form [required]').each(function () {
                if ($(this).val() === '') {
                    const errorMessage = $(this).data('name');
                    alert(errorMessage + '을(를) 입력해 주세요');
                    $(this).focus();
                    isValid = false;
                    return false;
                }
            });
            if (!isValid) return;

            // FormData 객체 생성
            const formData = new FormData($('#emp-form')[0]);

            // 파일 정보 확인 및 추가
            const fileInput = $('input[name="file"]')[0];
            if (fileInput.files.length > 0) {
                formData.append('file', fileInput.files[0]); // 파일 추가
            }

            // 기존 파일이 유지되는 경우 처리
            if (fileChange === 0) {
                const fileName = $('#fileName').text().trim();
                if (fileName) {
                    formData.append('beforeFileName', fileName);
                }
            }
            // AJAX 요청
            $.ajax({
                url: "${pageContext.request.contextPath}/admin/emp-manage/process",
                type: "POST",
                data: formData, // FormData 객체 사용
                processData: false, // FormData를 사용할 경우 false로 설정
                contentType: false, // 기본 Content-Type 설정 해제
                success: function (response) {
                    if (response.status === 'success') {
                        alert("직원 수정이 완료되었습니다.");
                        window.location.href = "${pageContext.request.contextPath}/admin/emp-manage";
                    } else {
                        alert("직원 수정 중 오류가 발생했습니다.");
                    }
                },
                error: function () {
                    alert("직원 수정 중 오류가 발생했습니다.");
                }
            });
        });

        // 자격증 추가 버튼 클릭
        $('#certifications').on('click', '.add-cert', function () {
            const newCert = `
				<div class="cert-item">
					<input type="text" name="cert" class="form-control cert-input" placeholder="자격증명" data-name="자격증" required>
					<button type="button" class="btn btn-sm btn-light remove-cert">-</button>
				</div>`;
            $(this).closest('.cert-add').before(newCert);
        });

        // 자격증 삭제 버튼 클릭
        $('#certifications').on('click', '.remove-cert', function () {
            $(this).closest('.cert-item').remove();
        });

        // 외국어능력 추가 버튼 클릭
        $('#languages').on('click', '.add-lang', function () {
            const newLang = `
				<div class="lang-item">
					<input type="text" name="lang" class="form-control lang-input" placeholder="외국어명"  data-name="외국어" required>
					<button type="button" class="btn btn-sm btn-light remove-lang">-</button>
				</div>`;
            $(this).closest('.lang-add').before(newLang);
        });

        // 외국어능력 삭제 버튼 클릭
        $('#languages').on('click', '.remove-lang', function () {
            $(this).closest('.lang-item').remove();
        });
    });
</script>

</body>
</html>