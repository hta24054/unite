<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="text-right mt-3">
    <c:if test="${role == 'preSignedWriter' || role == 'preSigner'}">
        <button type="button" class="btn btn-success" id="sign">결재</button>
    </c:if>
    <c:if test="${role == 'preSignedWriter'}">
        <button type="button" class="btn btn-success" id="edit">수정</button>
    </c:if>
    <c:if test="${role == 'postSignedWriter'|| role == 'postSigner'}">
        <button type="button" class="btn btn-warning" id="revoke">회수</button>
    </c:if>
    <c:if test="${role == 'preSignedWriter'}">
        <button type="button" class="btn btn-danger" id="delete">삭제</button>
    </c:if>
    <button type="button" class="btn btn-secondary" id="back">목록으로</button>
</div>
<script>
    $('#back').click(function () {
        const previousUrl = document.referrer; //이전 페이지 URL
        if (previousUrl) {
            window.location.href = previousUrl;
        } else {
            history.back();
            window.location.reload(); //새로고침
        }
    });

    $("#edit").click(function () {
        window.location.href = '${pageContext.request.contextPath}/doc/edit?docId=${doc.docId}';
    });

    $('#sign').click(function () {
        if (confirm('문서를 결재하시겠습니까?')) {
            $.ajax({
                url: '${pageContext.request.contextPath}/doc/sign',
                data: {docId: ${param.docId}},
                dataType: "json",
                success: function (response) {
                    if (response.status === 'success') {
                        alert("문서를 결재하였습니다.");
                        location.reload();
                    } else {
                        alert("문서 결재 중 오류가 발생했습니다.");
                    }
                },
                error: function () {
                    alert("문서 결재 중 오류가 발생했습니다.");
                }
            });
        }
    });

    $('#revoke').click(function () {
        if (confirm('결재한 문서를 회수하시겠습니까?')) {
            $.ajax({
                url: '${pageContext.request.contextPath}/doc/revoke',
                data: {docId: ${param.docId}},
                dataType: "json",
                success: function (response) {
                    if (response.status === 'success') {
                        alert("문서를 회수하였습니다.");
                        location.reload();
                    } else {
                        alert("문서 회수 중 오류가 발생했습니다.");
                    }
                },
                error: function () {
                    alert("문서 회수 중 오류가 발생했습니다.");
                }
            });
        }
    });
    $('#delete').click(function () {
        if (confirm('문서를 삭제하시겠습니까?')) {
            $.ajax({
                url: '${pageContext.request.contextPath}/doc/delete',
                data: {docId: ${param.docId}},
                dataType: "json",
                success: function (response) {
                    if (response.status === 'success') {
                        alert("문서를 삭제하였습니다.");
                        window.location.href = "${pageContext.request.contextPath}/doc/waiting"
                    } else {
                        alert("문서 삭제 중 오류가 발생했습니다.");
                    }
                },
                error: function () {
                    alert("문서 삭제 중 오류가 발생했습니다.");
                }
            });
        }
    });
</script>