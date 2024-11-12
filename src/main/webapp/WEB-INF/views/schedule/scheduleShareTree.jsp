<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<head>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/jquery.fancytree-all-deps.min.js"></script>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/skin-win8/ui.fancytree.min.css"/>
	<style>
        .content-container {
            display: flex; /* 조직도와 직원 목록을 가로로 배치 */
            gap: 30px; /* 조직도와 직원 목록 사이의 간격 */
        }

        #treeContainer, #employeeTableContainer {
            width: 50%; /* 두 컨테이너의 너비를 같게 설정 */
        }

        /* 테이블 스타일 */
        table.table {
            width: 100%;
        }

        /* 제목 스타일 */
        .title {
            color: #334466;
            font-size: 18px;
            margin: 0; /* 기본 마진 제거 */
            font-weight: bold;
            border-bottom: 1px solid black;
            padding-bottom: 10px;
        }

        #tree_title {
            margin-bottom: 25px;
        }
        #tree_table {
            text-align: center;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <div class="content-container">
        <!-- 조직도 영역 -->
        <div id="treeContainer">
            <h5 class="title" id="tree_title">조직도</h5>
            <div id="tree"></div>
        </div>

        <!-- 직원 목록 테이블 영역 -->
        <div id="employeeTableContainer">
            <h5 class="title">직원 목록</h5>
            <table class="table table-bordered mt-4" id="tree_table">
                <thead>
                <tr>
                    <th>이름</th>
                    <th>직급</th>
                    <th>부서</th>
                    <th>ID</th>
                </tr>
                </thead>
                <tbody id="employeeTableBody">
                <!-- 직원 정보가 AJAX로 로드되어 여기에 추가됩니다. -->
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
	$(document).ready(function() {
		
		
        $("#tree").fancytree({
             source: [
                 {
                     title: "대표이사", folder: true, children: [
                         {title: "부사장", folder: true},
                         {
                             title: "경영기획본부", key: "경영기획본부", folder: true, 
                             children: [
                                 {title: "재무관리팀", key: "재무관리팀", folder: true},
                                 {title: "인사관리팀", key: "인사관리팀", folder: true}
                             ]
                         },
                         {
                             title: "SI사업본부", key: "SI사업본부", folder: true, 
                             children: [
                                 {title: "신용평가팀", key: "신용평가팀", folder: true},
                                 {title: "금융SI팀", key: "금융SI팀", folder: true},
                                 {title: "비금융SI팀", key: "비금융SI팀", folder: true},
                                 {title: "SM팀", key: "SM팀", folder: true}
                             ]
                         },
                         {
                             title: "영업본부", key: "영업본부", folder: true, 
                             children: [
                                 {title: "솔루션영업팀", key: "솔루션영업팀", folder: true},
                                 {title: "SI영업팀", key: "SI영업팀", folder: true},
                                 {title: "SM영업팀", key: "SM영업팀", folder: true}
                             ]
                         },
                         {
                             title: "R&D본부", key: "R&D본부", folder: true, 
                             children: [
                                 {title: "연구개발팀", key: "연구개발팀", folder: true}
                             ]
                         }
                     ]
                 }
             ],
             click: function(event, data) {
                 const department = data.node.key;
                 loadEmployees(department); // 부서 클릭 시 직원 정보 로드
             }
         });

         function loadEmployees(department) {
             $.ajax({
                 url: '${pageContext.request.contextPath}/schedule/ScheduleShareEmploy',
                 method: 'GET',
                 data: { 
                	 department: department
                 },
                 success: function(data) {
                     updateEmployeeTable(data); // 직원 리스트 업데이트
                 },
                 error: function() {
                     alert('직원 정보를 불러오는 데 실패했습니다.');
                 }
             });
         }

         function updateEmployeeTable(data) {
             const tableBody = $('#employeeTableBody');
             tableBody.empty(); // 기존 내용 비우기

             $.each(data, function(key, value) {
                 var html = "<tr>";
                 html += "<td><input type='checkbox' class='employee-checkbox' value='" + value + "'>" + value + "</td>";
                 html += "<td>" + value.ename + "</td>"; //이름
                 html += "<td>" + value.school + "</td>"; //job_name 직급
                 html += "<td>" + value.mobile + "</td>"; //부서
                 html += "<td>" + value.emp_id + "</td>"; //직원id
                 html += "</tr>";
                 tableBody.append(html); // 각 행을 테이블에 추가
             });
         }

         $('#btnSave').on('click', function() {
             const selectedNames = [];
             $('.employee-checkbox:checked').each(function() {
                 selectedNames.push($(this).val());
             });
             if (selectedNames.length > 0) {
                 const namesString = selectedNames.join(', ');
                 const inputId = localStorage.getItem('selectedInputId'); // 로컬 저장소에서 input ID 가져오기

                 if (inputId) {
                     const targetInput = parent.document.getElementById(inputId); // 부모 문서에서 해당 input 요소 찾기
                     if (targetInput) {
                         targetInput.value = namesString; // 해당 input에 값 설정
                         alert('선택된 직원: ' + namesString);
                         
                         // 모달 닫기
                         $('#scheduleShareModal').modal('hide'); // 현재 페이지의 모달을 닫기
                       
                     } else {
                         alert('해당 ID를 가진 input 요소를 찾을 수 없습니다.');
                     }
                 } else {
                     alert('선택된 input ID가 없습니다.');
                 }
             } else {
                 alert('선택된 직원이 없습니다.');
             }
         });
     });
</script>

</body>
</html>