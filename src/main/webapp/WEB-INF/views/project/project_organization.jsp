<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>프로젝트 생성</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .modal-lg {
            max-width: 100%;
        }
        .modal-body {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
        }
        .employee-table {
            flex: 1;
            height: 400px;
            overflow-y: auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd;
        }
        .container {
            background-color: #fff;
            border: 1px solid #e1e4e8;
            border-radius: 8px;
            padding: 40px;
            width: 100%;
            max-width: 800px;
            margin: 40px auto;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
        }
    </style>
    <script>	
    	const contextPath = "${pageContext.request.contextPath}";  
    	const userid = "${sessionScope.id}";
   	</script>
	<script src="${pageContext.request.contextPath }/js/project_organ.js"></script> 
</head>
<body>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="project_leftbar.jsp"/>
    <div class="container mt-4">
        <h2>프로젝트 생성</h2>
        <form id="projectForm" action="doCreate" method="post">
            <div class="form-group row">
                <label for="name" class="col-sm-3 col-form-label">프로젝트명</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="name" name="name" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="startDate" class="col-sm-3 col-form-label">시작일</label>
                <div class="col-sm-9">
                    <input type="date" class="form-control" id="startDate" name="startDate" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="endDate" class="col-sm-3 col-form-label">종료일</label>
                <div class="col-sm-9">
                    <input type="date" class="form-control" id="endDate" name="endDate" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="manager" class="col-sm-3 col-form-label">책임자</label>
                <div class="col-sm-9 d-flex align-items-center">
                    <input type="text" class="form-control" id="manager" name="manager" required readOnly>
                    <a href="#" class="mr-2 orgChartIcon" data-target="manager">
                        <img src="${pageContext.request.contextPath}/image/profile_navy.png" alt="조직도" width="20" height="20">
                    </a>
                </div>
            </div>
            <div class="form-group row">
                <label for="participants" class="col-sm-3 col-form-label">참여자</label>
                <div class="col-sm-9 d-flex align-items-center">
                    <input type="text" class="form-control" id="participants" name="participants" required readOnly>
                    <a href="#" class="mr-2 orgChartIcon" data-target="participants">
                        <img src="${pageContext.request.contextPath}/image/profile_navy.png" alt="조직도" width="20" height="20">
                    </a>
                </div>
            </div>
            <div class="form-group row">
                <label for="viewers" class="col-sm-3 col-form-label">열람자</label>
                <div class="col-sm-9 d-flex align-items-center">
                    <input type="text" class="form-control" id="viewers" name="viewers" required readOnly>
                    <a href="#" class="mr-2 orgChartIcon" data-target="viewers">
                        <img src="${pageContext.request.contextPath}/image/profile_navy.png" alt="조직도" width="20" height="20">
                    </a>
                </div>
            </div>
            <div class="form-group row">
                <label for="description" class="col-sm-3 col-form-label">내용</label>
                <div class="col-sm-9">
                    <textarea class="form-control" id="description" name="description" rows="4" required></textarea>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-12">
                    <button type="submit" class="btn btn-primary w-100">프로젝트 생성</button>
                </div>
            </div>
        </form>
    </div>

    <!-- 조직도 모달 -->
    <div class="modal fade" id="orgChartModal" tabindex="-1" role="dialog" aria-labelledby="orgChartModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="orgChartModalLabel">조직도</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-footer">
                <jsp:include page="../common/empTree.jsp"/>
            </div>
            <div class="modal-body">
                <div class="employee-table">
                    <table id="employeeTable">
                        <tbody id="employeeTableBody">
                            
                        </tbody>
                    </table>
                </div>
                <div class="text-area-container mt-3">
                   <div id="selectedEmployees" style="border: 1px solid #ccc; padding: 10px; width: 750px; min-height: 50px;"></div>
                    <button type="button" id="addEmpBtn" class="btn btn-primary mt-2">추가</button>
	                <button type="button" class="btn btn-success mt-2" id="insertEmpBtn">등록</button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
