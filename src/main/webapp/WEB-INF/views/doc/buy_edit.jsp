<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>

    <meta charset="UTF-8">
    <title>구매신청서 수정</title>
    <style>
        .header-cell {
            font-size: 36px;
            font-weight: bold;
            padding: 20px 0;
        }

        .title-input, .form-control {
            font-size: 16px;
        }

        /* 테두리 색상을 검정색으로 설정 */
        .table-bordered, .table-bordered td, .table-bordered th {
            align-content: center;
            border-color: black !important;
        }

        .item_form {
            width: 18%;
        }

        #modify {
            width: 10%;
        }

        .item_form {
            width: 18%;
            text-align: right; /* 품명, 규격을 제외한 수량, 단가, 금액 우측 정렬 */
        }

        #modify {
            width: 10%;
        }

        /* 우측 정렬을 적용하는 클래스 */
        .text-right-align {
            text-align: right;
        }
    </style>
</head>
<body>
<form id="doc_form" action="${pageContext.request.contextPath}/doc/buy_write" method="POST">
    <div class="container mt-4">
        <!-- 기안용지 제목 -->
        <div class="text-center mb-4">
            <h1 class="header-cell">구매신청서</h1>
        </div>

        <div class="row">
            <div class="col-md-6 mb-3">
                <table class="table table-bordered">
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">문서번호</td>
                        <td><input type="text" name="docId" value="${doc.docId}" readonly></td>
                        <input type="hidden" name="docBuyId" value="${doc.docBuyId}">
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

        <!-- 기타 정보 테이블 -->
        <table class="table table-bordered mt-4 item_table" id="itemTable">
            <tr>
                <th colspan="1" class="table-secondary font-weight-bold text-center">제목</th>
                <td colspan="5">
                    <input type="text" name="title" class="form-control" placeholder="제목을 입력하세요" data-name="제목"
                           value="${doc.docTitle}"
                           required>
                </td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center item_form">품명</th>
                <th class="table-secondary font-weight-bold text-center item_form">규격</th>
                <th class="table-secondary font-weight-bold text-center item_form text-right-align">수량</th>
                <th class="table-secondary font-weight-bold text-center item_form text-right-align">단가</th>
                <th class="table-secondary font-weight-bold text-center item_form text-right-align">금액</th>
                <th class="table-secondary font-weight-bold text-center" id="modify">추가/삭제</th>
            </tr>
            <c:forEach var="item" items="${doc.buyList}">
                <tr class="item-row">
                    <td><input type="text" class="form-control title-input" name="product_name" data-name="품명"
                               value="${item.productName}" required>
                    </td>
                    <td><input type="text" class="form-control title-input" name="standard" data-name="규격"
                               value="${item.standard}" required>
                    </td>
                    <td><input type="text" class="form-control title-input quantity text-right-align" name="quantity"
                               value="${item.quantity}"
                               data-name="수량" required></td>
                    <td><input type="text" class="form-control title-input price text-right-align" name="price"
                               value="${item.price}"
                               data-name="단가" required></td>
                    <td class="subTotal text-right-align"></td>
                    <td class="text-center">
                        <button type="button" class="btn btn-sm btn-light add-row">+</button>
                        <button type="button" class="btn btn-sm btn-light remove-row">-</button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="4" class="table-secondary font-weight-bold text-center">합계</td>
                <td id="total" class="text-right-align"></td>
                <td class="table-secondary font-weight-bold text-center"></td>
            </tr>
            <tr>
                <th colspan="6" class="table-secondary font-weight-bold text-center">내용</th>
            </tr>
            <tr>
                <td colspan="6">
                    <textarea name="content" class="form-control" rows="4" placeholder="내용을 입력하세요" data-name="내용"
                              required>${doc.docContent}</textarea>
                </td>
            </tr>
        </table>

    </div>
    <!-- 버튼 영역 -->
    <div class="text-right mt-3">
        <button type="button" form="doc_form" class="btn btn-success" id="submit-button">결재 상신</button>
        <button type="reset" form="doc_form" class="btn btn-secondary">초기화</button>
    </div>
</form>
<script>
    $(document).ready(function () {
        // 최대 품목 수, 수량, 단가
        const maxItems = 10;
        const maxQuantity = 1000;
        const maxPrice = 100000000;

        // 새로운 행 추가
        $('#itemTable').on('click', '.add-row', function () {
            const rowCount = $('#itemTable .item-row').length;
            if (rowCount >= maxItems) {
                alert("품목은 최대 10개까지 추가할 수 있습니다.");
                return;
            }

            const newRow = `
                <tr class="item-row">
                    <td><input type="text" class="form-control title-input" name="product_name" data-name="품명" required></td>
                    <td><input type="text" class="form-control title-input" name="standard" data-name="규격" required></td>
                    <td><input type="text" class="form-control title-input quantity text-right-align" name="quantity" data-name="수량" required></td>
                    <td><input type="text" class="form-control title-input price text-right-align" name="price" data-name="단가" required></td>
                    <td class="subTotal text-right-align"></td>
                    <td class="text-center">
                        <button type="button" class="btn btn-sm btn-light add-row">+</button>
                        <button type="button" class="btn btn-sm btn-light remove-row">-</button>
                    </td>
                </tr>
            `;
            $(this).closest('tr').after(newRow);
        });

        // 현재 행 삭제
        $('#itemTable').on('click', '.remove-row', function () {
            const rows = $('#itemTable .item-row');
            if (rows.length > 1) {
                $(this).closest('tr').remove();
                updateTotal();
            }
        });

        // 수량과 단가 입력 시 콤마 추가 및 금액 계산
        $('#itemTable').on('input', '.quantity, .price', function () {
            const row = $(this).closest('tr');
            let quantity = parseNumber(row.find('.quantity').val());
            let price = parseNumber(row.find('.price').val());

            // 수량 및 단가 제한 검사
            if (quantity > maxQuantity) {
                alert("수량은 최대 1000개까지 입력할 수 있습니다.");
                quantity = maxQuantity;
                row.find('.quantity').val(formatNumber(quantity));
            }
            if (price > maxPrice) {
                alert("단가는 최대 1억까지 입력할 수 있습니다.");
                price = maxPrice;
                row.find('.price').val(formatNumber(price));
            }

            formatAndCalculate(row);
            updateTotal();
        });

        // 금액 계산 및 콤마 포맷팅 함수
        function formatAndCalculate(row) {
            const quantity = parseNumber(row.find('.quantity').val());
            const price = parseNumber(row.find('.price').val());
            const subTotal = quantity * price;

            row.find('.quantity').val(formatNumber(quantity));
            row.find('.price').val(formatNumber(price));
            row.find('.subTotal').text(formatNumber(subTotal));
        }

        // 전체 합계 업데이트
        function updateTotal() {
            let total = 0;
            $('#itemTable .subTotal').each(function () {
                total += parseNumber($(this).text());
            });
            $('#total').text(formatNumber(total));
        }

        // 숫자를 세 자리마다 콤마로 포맷팅하는 함수
        function formatNumber(number) {
            return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }

        // 콤마가 있는 문자열을 숫자로 변환하는 함수
        function parseNumber(value) {
            return parseFloat(value.replace(/,/g, '')) || 0;
        }

        //제출버튼 눌리는 경우
        $('#submit-button').on('click', function (event) {
            // 모든 required 필드가 채워졌는지 검사
            let isValid = true;
            $('#doc_form [required]').each(function () {
                if ($(this).val() === '') {
                    const errorMessage = $(this).data('name'); // data-error 속성 값 가져오기
                    alert(errorMessage + '을(를) 입력해 주세요');
                    $(this).focus();
                    isValid = false;
                    return false;
                }
            });

            // 필수 항목이 채워지지 않은 경우 AJAX 요청 중단
            if (!isValid) return;

            event.preventDefault(); // 기본 폼 제출 동작 방지

            const additionalSigners = $('input[name="sign[]"]').length - 1;
            if (additionalSigners < 1) {
                alert("본인 이외에 최소 1명의 결재자를 추가해야 합니다.");
                return;
            }

            const confirmSubmission = confirm("문서를 작성하시겠습니까?");
            if (!confirmSubmission) return;

            // 폼 데이터 수집
            const formData = {
                docId: $('input[name="docId"]').val(),
                docBuyId: $('input[name="docBuyId"]').val(),
                title: $('input[name="title"]').val(),
                content: $('textarea[name="content"]').val(),
                writer: $('input[name="writer"]').val(),
                productDetails: [],
                signers: []
            };

            // 결재자 정보 추가
            $('input[name="sign[]"]').each(function () {
                formData.signers.push($(this).val());
            });

            //제품정보 추가
            $('#itemTable .item-row').each(function () {
                const product = {
                    product_name: $(this).find('input[name="product_name"]').val(),
                    standard: $(this).find('input[name="standard"]').val(),
                    quantity: parseNumber($(this).find('input[name="quantity"]').val()),
                    price: parseNumber($(this).find('input[name="price"]').val())
                };
                formData.productDetails.push(product);
            });

            $.ajax({
                url: "${pageContext.request.contextPath}/doc/buy_edit",
                type: "POST",
                data: JSON.stringify(formData),
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function (response) {
                    if (response.status === 'success') {
                        alert("문서 작성이 완료되었습니다.");
                        window.location.href = "${pageContext.request.contextPath}/doc/in-progress";
                    } else {
                        alert("문서 작성 중 오류가 발생했습니다.");
                    }
                },
                error: function () {
                    alert("문서 작성 중 오류가 발생했습니다.");
                }
            });
        });
    });
</script>
</body>
</html>