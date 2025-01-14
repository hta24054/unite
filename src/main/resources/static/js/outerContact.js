$(document).ready(function () {
    const contextPath = /*[[@{}]]*/ '';
    const lang_kor = {
        "decimal": "",
        "emptyTable": "데이터가 없습니다.",
        "info": "_START_-_END_ (총 _TOTAL_건)",
        "infoEmpty": "0건",
        "infoFiltered": "(전체 _MAX_건 중 검색 결과)",
        "infoPostFix": "",
        "thousands": ",",
        "lengthMenu": "_MENU_개씩 보기",
        "loadingRecords": "로딩중...",
        "processing": "처리중...",
        "search": "검색:",
        "zeroRecords": "검색된 데이터가 없습니다.",
        "paginate": {"first": "첫 페이지", "last": "마지막 페이지", "next": "다음", "previous": "이전"},
        "aria": {"sortAscending": ":오름차순 정렬", "sortDescending": ":내림차순 정렬"}
    };

    // DataTable 초기화
    const contactTable = $('#contactTable').DataTable({
        language: lang_kor,
        ajax: {
            url: contextPath + "/contact/outer/list",
            dataSrc: 'data',
        },
        columns: [
            {
                data: null,
                orderable: false,
                render: function (data, type, row) {
                    return `
                        <input type="checkbox" width="30px" id="checkbox-${row.id}" class="checkbox" name="id" value="${row.id}">
                    `;
                }
            },
            {data: 'name'},
            {data: 'position'},
            {data: 'company'},
            {data: 'mobile'},
            {data: 'tel'},
            {data: 'email'},
            {data: 'info'}
        ]
    });

    // 등록 처리
    $("#insertContact").submit(function (event) {
        event.preventDefault();
        $.post({
            url: contextPath + "/contact/outer",
            data: $(this).serialize(),
            success: function () {
                alert("등록 성공");
                $('#addContactModal').modal('hide');
                contactTable.ajax.reload();
            },
            error: function () {
                alert("등록 실패");
            }
        });
    });

    // 수정 처리
    $("#updateContact").submit(function (event) {
        event.preventDefault();
        $.ajax({
            url: contextPath + "/contact/outer",
            type: "PATCH",
            data: $(this).serialize(),
            success: function () {
                alert("수정 성공");
                $('#editContactModal').modal('hide');
                contactTable.ajax.reload();
            },
            error: function () {
                alert("수정 실패");
            }
        });
    });

    // 삭제 처리
    $("#deleteButton").click(function () {
        const selectedIds = $('.checkbox:checked').map(function () {
            return $(this).val();
        }).get();

        if (selectedIds.length === 0) {
            alert("삭제할 자원을 선택해주세요.");
            return;
        }

        if (confirm("선택한 자원을 삭제하시겠습니까?")) {
            $('#deleteContactIds').val(selectedIds.join(','));
            $.ajax({
                url: contextPath + "/contact/outer",
                type: "DELETE",
                data: {selectedIds},
                success: function (data) {
                    alert(data);
                    contactTable.ajax.reload();
                },
                error: function () {
                    alert("삭제에 실패했습니다.");
                }
            });
        }
    });

    $('#editContactModal').attr('aria-hidden', 'true');
    document.querySelectorAll('button, input, select, textarea').forEach(element => {
        element.blur();
    });
    $('#addContactModal').attr('aria-hidden', 'true');
    document.querySelectorAll('button, input, select, textarea').forEach(element => {
        element.blur();
    });
});

// 수정 모달 열기
function openEditModal() {
    const selectedCheckbox = $('#contactTable .checkbox:checked');

    if (selectedCheckbox.length !== 1) {
        alert("수정할 주소록을 하나 선택해주세요.");
        return;
    }

    // 선택한 자원의 ID를 가져옴
    const contactId = selectedCheckbox.val();

    // 선택한 자원의 행(row) 가져오기
    const $row = selectedCheckbox.closest("tr");

    // 행에서 각 정보 추출
    const contactName = $row.find("td:nth-child(2)").text().trim();
    const contactPosition = $row.find("td:nth-child(3)").text().trim();
    const contactCompany = $row.find("td:nth-child(4)").text().trim();
    const contactMobile = $row.find("td:nth-child(5)").text().trim();
    const contactTel = $row.find("td:nth-child(6)").text().trim();
    const contactEmail = $row.find("td:nth-child(7)").text().trim();
    const contactInfo = $row.find("td:nth-child(8)").text().trim();

    // 수정 모달에 데이터를 설정
    $('#editContactId').val(contactId);
    $('#editPosition').val(contactPosition);
    $('#editName').val(contactName);
    $('#editCompany').val(contactCompany);
    $('#editMobile').val(contactMobile);
    $('#editTel').val(contactTel);
    $('#editEmail').val(contactEmail);
    $('#editInfo').val(contactInfo);

    // 모달 표시
    $('#editContactModal').modal('show');
}