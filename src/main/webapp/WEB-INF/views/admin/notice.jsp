<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="admin_leftbar.jsp"/>
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-bs4.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-bs4.min.js"></script>

    <meta charset="UTF-8">
    <title>공지사항</title>
    <script>
        let result = '${message}';
        if (result !== '') {
            alert(result);
            <%session.removeAttribute("message");%>
        }
    </script>
    <style>
        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .custom-modal-width {
            max-width: 40%; /* 원하는 비율로 조정 */
            max-height: 90%; /* 원하는 비율로 조정 */
        }
        #notice_table th{
            text-align: center;
        }
    </style>
</head>
<body>
<h2 id="main_title">공지사항</h2>
<div class="container text-center">
    <div class="text-right mb-2">
        <button type="button" class="btn btn-success mr-2" data-toggle="modal" data-target="#noticeWriteModal"
                id="insertButton" onclick="openInsertModal()">등록
        </button>
    </div>

    <table class="table table-striped table-bordered" id="notice_table">
        <thead>
        <tr>
            <th>공지사항 제목</th>
            <th>게시 종료일</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty noticeList}">
            <td colspan="2" style="text-align: center">등록된 공지사항이 없습니다.</td>
        </c:if>
        <c:if test="${!empty noticeList}">
            <c:forEach var="notice" items="${noticeList}">
                <tr>
                    <td>
                        <a href="#"
                           data-id="${notice.noticeId}"
                           data-subject="${notice.noticeSubject}"
                           data-content="${fn:escapeXml(notice.noticeContent)}"
                           data-enddate="${notice.noticeEndDate}"
                           onclick="openDetailModal(this)">
                                ${notice.noticeSubject}
                        </a>
                    </td>
                    <td>${notice.noticeEndDate}</td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
</div>
<!-- 공지글 작성 및 수정 모달 -->
<div class="modal fade" id="noticeWriteModal" tabindex="-1" aria-labelledby="noticeWriteModalLabel" aria-hidden="true">
    <div class="modal-dialog custom-modal-width">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="noticeWriteModalLabel">공지사항</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="noticeAddForm" method="post" action="${pageContext.request.contextPath}/admin/notice/insert">
                    <input type="hidden" id="noticeId">

                    <!-- 제목과 날짜 필드를 나란히 배치 -->
                    <div class="form-group row">
                        <!-- 제목 입력 필드 -->
                        <div class="col-md-8">
                            <label for="subject" class="form-label" style="font-weight: bold">제목(관리 차원의 제목이며, 팝업엔 노출되지 않습니다.)</label>
                            <input type="text" class="form-control" id="subject" name="subject" placeholder="제목을 작성해주세요" required>
                        </div>

                        <!-- 게시 종료일 입력 필드 -->
                        <div class="col-md-4">
                            <label for="endDate" class="form-label" style="font-weight: bold">게시종료일</label>
                            <div class="input-group">
                                <input type="date" class="form-control" id="endDate" name="endDate" required>
                            </div>
                        </div>
                    </div>

                    <!-- Summernote 에디터 -->
                    <div class="form-group">
                        <label for="content" class="form-label" style="font-weight: bold">내용</label>
                        <textarea class="summernote form-control" id="content" name="content" required></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-success" id="insertNotice" style="display: none;"
                        onclick="insertNotice()">등록
                </button>
                <button type="button" class="btn btn-primary" id="updateNotice" style="display: none;"
                        onclick="updateNotice()">수정
                </button>
                <button type="button" class="btn btn-danger" id="deleteNotice" style="display: none;"
                        onclick="deleteNotice()">삭제
                </button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('.summernote').summernote({
            height: 500, // set editor height
            minHeight: 500, // set minimum height of editor
            maxHeight: null, // set maximum height of editor
            focus: true // set focus to editable area after initializing summernote
        });

        // 모달이 닫힐 때 에디터와 날짜 필드를 초기화하고 등록 버튼 표시
        $('#noticeWriteModal').on('hidden.bs.modal', function () {
            $('#endDate').val(''); // 날짜 필드 초기화
            $('#subject').val(''); // 날짜 필드 초기화
            $('#content').summernote('code', ''); // 에디터 초기화
        });
    });

    // '등록' 버튼 클릭 시 '등록' 버튼만 보이도록 설정하고 필드 초기화
    function openInsertModal() {
        $('#insertNotice').show(); // 등록 버튼 보이기
        $('#subject').val(''); // 날짜 필드 초기화
        $('#content').summernote('code', ''); // 에디터 초기화
        $('#endDate').val(''); // 날짜 필드 초기화

        $('#updateNotice, #deleteNotice').hide(); // 수정/삭제 버튼 숨기기
        $('#noticeWriteModal').modal('show'); // 모달 열기
    }


    //등록 함수
    function insertNotice() {
        const content = $('#content').val();
        const subject = $('#subject').val();
        const endDate = $('#endDate').val();
        if(!endDate){
            alert("게시 종료일을 입력해 주세요");
            return false;
        }
        if(!subject){
            alert("제목을 입력해 주세요");
            return false;
        }
        if(!content){
            alert("내용을 입력해 주세요");
            return false;
        }
        if(subject.length>150){
            alert("제목은 50자 이하로 작성해 주세요");
            return false;
        }

        $.ajax({
            url: `${pageContext.request.contextPath}/admin/notice/insert`,
            method: 'POST',
            data: {
                subject: subject,
                content: content,
                endDate: endDate
            },
            success: function (data) {
                alert(data.message);
                location.reload();
            },
            error: function () {
                alert("공지사항 등록 중 오류가 발생했습니다.");
            }
        });
    }

    // 게시글 내용 클릭 시 수정/삭제 버튼을 보이도록 설정하는 함수
    function openDetailModal(element) {
        // data-* 속성에서 데이터를 가져옵니다.
        const noticeId = element.dataset.id;
        const noticeSubject = element.dataset.subject;
        const noticeContent = element.dataset.content;
        const noticeEndDate = element.dataset.enddate;

        $('#noticeId').val(noticeId);
        $('#subject').val(noticeSubject);
        $('#content').summernote('code', noticeContent); // Summernote에 HTML 콘텐츠 설정
        $('#endDate').val(noticeEndDate);

        $('#insertNotice').hide(); // 등록 버튼 숨기기
        $('#updateNotice, #deleteNotice').show(); // 수정/삭제 버튼 보이기

        $('#noticeWriteModal').modal('show'); // 모달 열기
    }

    // 삭제 함수
    function deleteNotice() {
        const noticeId = $('#noticeId').val();
        if (confirm("정말로 삭제하시겠습니까?")) {
            $.ajax({
                url: `${pageContext.request.contextPath}/admin/notice/delete`,
                method: 'POST',
                data: {noticeId: noticeId},
                success: function (data) {
                    alert(data.message);
                    location.reload();
                },
                error: function () {
                    alert("삭제 중 오류가 발생했습니다.");
                }
            });
        }
    }

    function updateNotice() {
        const content = $('#content').val();
        const subject = $('#subject').val();
        const endDate = $('#endDate').val();
        if(!endDate){
            alert("게시 종료일을 입력해 주세요");
            return false;
        }
        if(!subject){
            alert("제목을 입력해 주세요");
            return false;
        }
        if(!content){
            alert("내용을 입력해 주세요");
            return false;
        }
        if(subject.length>150){
            alert("제목은 50자 이하로 작성해 주세요");
            return false;
        }

        if (confirm("정말로 수정하시겠습니까?")) {
            $.ajax({
                url: `${pageContext.request.contextPath}/admin/notice/update`,
                method: 'POST',
                data: {
                    noticeId: $('#noticeId').val(),
                    subject: $('#subject').val(),
                    content: $('#content').val(),
                    endDate: $('#endDate').val()
                },
                success: function (data) {
                    alert(data.message);
                    location.reload();
                },
                error: function () {
                    alert("수정 중 오류가 발생했습니다.");
                }
            });
        }
    }
</script>
</body>
</html>