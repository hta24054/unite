<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="text-right mt-3">
    <c:choose>
        <c:when test="${role == 'signer'}">
            <button type="button" class="btn btn-success">결재</button>
            <button type="button" class="btn btn-danger">반려</button>
        </c:when>
        <c:when test="${role == 'writer'}">
            <button type="button" class="btn btn-warning">회수</button>
            <button type="button" class="btn btn-danger">삭제</button>
        </c:when>
    </c:choose>
    <button type="button" class="btn btn-secondary">목록으로</button>
</div>