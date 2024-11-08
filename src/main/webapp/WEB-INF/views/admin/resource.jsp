<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>자원 관리</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="admin_leftbar.jsp"/>

    <script>
        $(document).ready(function () {
            let result = '${message}';
            if (result && result.trim() !== '') {
                alert(result);
                <% session.removeAttribute("message"); %>
            }
        });

        function openEditModal() {
            // 체크된 체크박스의 개수를 확인
            const checkedCount = $('.checkbox:checked').length;

            if (checkedCount === 0) {
                alert("수정할 자원을 선택해주세요.");
                return;
            }
            if (checkedCount > 1) {
                alert("수정할 자원을 한 개만 선택해주세요.");
                return;
            }

            // 선택한 자원의 ID를 가져옴
            const resourceId = $('.checkbox:checked').val();

            // 선택한 자원의 행(row) 가져오기
            const $row = $('.checkbox:checked').closest("tr");

            // 행에서 각 정보 추출
            const resourceType = $row.find("td:nth-child(2)").text().trim();
            const resourceName = $row.find("td:nth-child(3)").text().trim();
            const resourceInfo = $row.find("td:nth-child(4)").text().trim();
            const resourceUsable = $row.find("td:nth-child(5)").text().trim() === '가능';

            // 수정 모달에 데이터를 설정
            $('#editResourceId').val(resourceId);
            $('#editResourceType').val(resourceType);
            $('#editResourceName').val(resourceName);
            $('#editResourceInfo').val(resourceInfo);
            $('#editResourceUsable').val(resourceUsable ? 'true' : 'false');

            // 모달 표시
            $('#editResourceModal').modal('show');
        }

        function deleteSelectedResources() {
            const selectedIds = [];

            // 체크된 체크박스의 값을 배열에 추가
            $('.checkbox:checked').each(function () {
                selectedIds.push($(this).val());
            });

            if (selectedIds.length === 0) {
                alert("삭제할 자원을 선택해주세요.");
                return;
            }

            if (confirm("선택한 자원을 삭제하시겠습니까?")) {
                // 선택된 ID들을 콤마로 구분하여 숨겨진 필드에 설정
                $('#deleteResourceIds').val(selectedIds.join(','));
                $('#deleteResourceForm').submit();
            }
        }
    </script>
    <style>
        .container {
            max-width: 800px;
            margin-top: 20px;
        }

        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        #resource_table {
            margin-top: 20px;
        }

        #resource_table th, td {
            text-align: center;
            vertical-align: middle;
            padding: 5px;
            line-height: 1.2;
        }
    </style>
</head>
<body>
<h2 id="main_title">자원 관리</h2>
<div class="container text-center">
    <div class="text-right mb-2">
        <button type="button" class="btn btn-success mr-2" data-toggle="modal" data-target="#addResourceModal">등록
        </button>
        <button type="button" class="btn btn-secondary mr-2" onclick="openEditModal()">수정</button>
        <button type="button" class="btn btn-danger" onclick="deleteSelectedResources()">삭제</button>
    </div>

    <table class="table table-striped table-bordered" id="resource_table">
        <thead>
        <tr>
            <th style="width: 80px; text-align: center;">
                선택<br>
                <input type="checkbox" onclick="$('input[name=resourceId]').prop('checked', this.checked);">
            </th>
            <th>분류명</th>
            <th>자원명</th>
            <th>자원정보</th>
            <th>가용상태</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="resource" items="${resourceList}">
            <tr>
                <td><input type="checkbox" class="checkbox" name="resourceId" value="${resource.resourceId}"></td>
                <td style="text-align: left;">${resource.resourceType}</td>
                <td style="text-align: left;">${resource.resourceName}</td>
                <td style="text-align: left;">${resource.resourceInfo}</td>
                <c:choose>
                    <c:when test="${resource.resourceUsable == true}">
                        <td>가능</td>
                    </c:when>
                    <c:otherwise>
                        <td>불가</td>
                    </c:otherwise>
                </c:choose>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- 자원 등록 모달 -->
<div class="modal fade" id="addResourceModal" tabindex="-1" aria-labelledby="addResourceModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addResourceModalLabel">자원 등록</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/admin/resource/add" method="post">
                    <div class="form-group">
                        <label for="resourceType">분류명</label>
                        <input type="text" class="form-control" id="resourceType" name="resourceType" required>
                    </div>
                    <div class="form-group">
                        <label for="resourceName">자원명</label>
                        <input type="text" class="form-control" id="resourceName" name="resourceName" required>
                    </div>
                    <div class="form-group">
                        <label for="resourceInfo">자원정보</label>
                        <textarea class="form-control" id="resourceInfo" name="resourceInfo" rows="3"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="resourceUsable">가용상태</label>
                        <select class="form-control" id="resourceUsable" name="resourceUsable" required>
                            <option value="true">가능</option>
                            <option value="false">불가</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">저장</button>
                </form>
            </div>
        </div>
    </div>
</div>

<%--수정모달--%>
<div class="modal fade" id="editResourceModal" tabindex="-1" aria-labelledby="editResourceModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editResourceModalLabel">자원 수정</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/admin/resource/edit" method="post">
                    <input type="hidden" id="editResourceId" name="resourceId">
                    <div class="form-group">
                        <label for="editResourceType">분류명</label>
                        <input type="text" class="form-control" id="editResourceType" name="resourceType" required>
                    </div>
                    <div class="form-group">
                        <label for="editResourceName">자원명</label>
                        <input type="text" class="form-control" id="editResourceName" name="resourceName" required>
                    </div>
                    <div class="form-group">
                        <label for="editResourceInfo">자원정보</label>
                        <textarea class="form-control" id="editResourceInfo" name="resourceInfo" rows="3"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="editResourceUsable">가용상태</label>
                        <select class="form-control" id="editResourceUsable" name="resourceUsable" required>
                            <option value="true">가능</option>
                            <option value="false">불가</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">저장</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 자원 삭제 폼 (선택한 자원 삭제 시 사용) -->
<form id="deleteResourceForm" action="${pageContext.request.contextPath}/admin/resource/delete" method="post"
      style="display: none;">
    <input type="hidden" id="deleteResourceIds" name="resourceIds">
</form>

</body>
</html>
