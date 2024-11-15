<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>

    <meta charset="UTF-8">
    <title>구매신청서</title>
    <style>
        .header-cell {
            font-size: 36px;
            font-weight: bold;
            padding: 20px 0;
        }

        /* 테두리 색상을 검정색으로 설정 */
        .table-bordered, .table-bordered td, .table-bordered th {
            align-content: center;
            border-color: black !important;
        }

        .item_form {
            width: 18%;
        }

        .item_form {
            width: 20%;
            text-align: right; /* 품명, 규격을 제외한 수량, 단가, 금액 우측 정렬 */
        }

        /* 우측 정렬을 적용하는 클래스 */
        .text-right-align {
            text-align: right;
        }
    </style>
</head>
<body>
<form id="doc_form" action="${pageContext.request.contextPath}/doc/buy_process" method="POST">
    <div class="container mt-4">
        <!-- 기안용지 제목 -->
        <div class="text-center mb-4">
            <h1 class="header-cell">구매신청서</h1>
        </div>

        <!-- 문서 정보 및 결재자 테이블을 좌우로 배치 -->
        <div class="row">
            <!-- 문서 정보 테이블 -->
            <div class="col-md-6 mb-3">
                <table class="table table-bordered">
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">문서번호</td>
                        <td>${doc.docId}</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">부&nbsp;&nbsp;&nbsp;서</td>
                        <td>${dept.deptName}</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">신 청 자</td>
                        <td>${writer.ename}</td>
                        <input type="hidden" name="writer" value="${writer.empId}">
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">작 성 일</td>
                        <td>${doc.docCreateDate.toLocalDate()}</td>
                    </tr>
                </table>
            </div>
            <!-- 결재자 테이블 -->
            <div class="col-md-6">
                <jsp:include page="sign_read.jsp"/>
            </div>
        </div>

        <!-- 기타 정보 테이블 -->
        <table class="table table-bordered mt-4 item_table" id="itemTable">
            <tr>
                <th colspan="1" class="table-secondary font-weight-bold text-center">제목</th>
                <td colspan="4">${doc.docTitle}</td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center item_form">품명</th>
                <th class="table-secondary font-weight-bold text-center item_form">규격</th>
                <th class="table-secondary font-weight-bold text-center item_form text-right-align">수량</th>
                <th class="table-secondary font-weight-bold text-center item_form text-right-align">단가</th>
                <th class="table-secondary font-weight-bold text-center item_form text-right-align">금액</th>
            </tr>
            <c:forEach var="item" items="${doc.buyList}">
                <tr class="item-row">
                    <td>${item.productName}</td>
                    <td>${item.standard}</td>
                    <td class="text-right-align quantity">${item.quantity}</td>
                    <td class="text-right-align price">${item.price} </td>
                    <td class="subTotal text-right-align">${item.quantity * item.price}</td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="4" class="table-secondary font-weight-bold text-center">합계</td>
                <td id="total" class="text-right-align"></td>
            </tr>
            <tr>
                <th colspan="5" class="table-secondary font-weight-bold text-center">내용</th>
            </tr>
            <tr>
                <td colspan="5">
                    ${doc.docContent}
                </td>
            </tr>
        </table>

    </div>
    <jsp:include page="doc_read_button.jsp"/>
</form>

<script>
    $(document).ready(function () {
        calculateTotal(); // 페이지 로드 시 초기 합계 계산

        function calculateTotal() {
            let total = 0;

            // 모든 item-row의 금액을 계산하여 총합 계산
            $(".item-row").each(function () {
                // 수량과 단가를 정수로 변환하며 콤마 제거
                const quantity = parseInt($(this).find(".text-right-align").eq(0).text().replace(/,/g, '')) || 0;
                const price = parseInt($(this).find(".text-right-align").eq(1).text().replace(/,/g, '')) || 0;
                const subTotal = quantity * price;

                // 각 행의 금액 셀에 계산된 금액을 세 자리 콤마 형식으로 표시
                $(this).find(".quantity").text(quantity.toLocaleString());
                $(this).find(".price").text(price.toLocaleString());
                $(this).find(".subTotal").text(subTotal.toLocaleString());
                total += subTotal; // 총합에 더함
            });

            // 합계 셀에 총합을 세 자리 콤마 형식으로 표시
            $("#total").text(total.toLocaleString());
        }
    });
</script>
</body>
</html>
