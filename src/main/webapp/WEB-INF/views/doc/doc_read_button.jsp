<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="text-right mt-3">
    <c:choose>
        <c:when test="${role == 'preSignedWriter'}">
            <button type="button" class="btn btn-success" id="sign">결재</button>
            <button type="button" class="btn btn-danger">삭제</button>
        </c:when>
        <c:when test="${role == 'preSigner'}">
            <button type="button" class="btn btn-success" id="sign">결재</button>
            <button type="button" class="btn btn-danger">반려</button>
        </c:when>
        <c:when test="${role == 'postSignedWriter'|| role == 'postSigner'}">
            <button type="button" class="btn btn-warning">회수</button>
        </c:when>
    </c:choose>
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

    $('#sign').click(function () {
        if (confirm('문서를 결재하시겠습니까?')) {
            $.ajax({
                url: '${pageContext.request.contextPath}/doc/sign',
                data: {docId: ${param.docId}},
                dataType: "json",
                success: function (response) {
                    if (response.status === 'success') {
                        alert("문서를 결재하였습니다.");
                        window.location.href = "${pageContext.request.contextPath}/doc/waiting";
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
</script>