$(document).ready(function () {
    const contextPath = /*[[@{}]]*/ '';

    // DataTable 초기화
    const resourceTable = $('#resourceTable').DataTable({
        ajax: {
            url: contextPath + "/admin/resource/list",
            dataSrc: 'data'
        },
        columns: [
            {
                data: null,
                orderable: false,
                render: function (data, type, row) {
                    return `
                        <input type="checkbox" width="30px" id="checkbox-${row.resourceId}" class="checkbox" name="resourceId" value="${row.resourceId}">
                    `;
                }
            },
            {data: 'resourceType'},
            {data: 'resourceName'},
            {data: 'resourceInfo'},
            {
                data: 'resourceUsable',
                render: function (data) {
                    return data ? '가능' : '불가';
                }
            }
        ]
    });

    // 등록 처리
    $("#insertResource").submit(function (event) {
        event.preventDefault();
        $.post({
            url: contextPath + "/admin/resource",
            data: $(this).serialize(),
            success: function () {
                alert("등록 성공");
                $('#addResourceModal').modal('hide');
                resourceTable.ajax.reload();
            },
            error: function () {
                alert("등록 실패");
            }
        });
    });

    // 수정 처리
    $("#updateResource").submit(function (event) {
        event.preventDefault();
        $.ajax({
            url: contextPath + "/admin/resource",
            type: "PATCH",
            data: $(this).serialize(),
            success: function () {
                alert("수정 성공");
                $('#editResourceModal').modal('hide');
                resourceTable.ajax.reload();
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
            $('#deleteResourceIds').val(selectedIds.join(','));
            $.ajax({
                url: "../admin/resource",
                type: "DELETE",
                data: {selectedIds},
                success: function (data) {
                    alert(data);
                    resourceTable.ajax.reload();
                },
                error: function () {
                    alert("삭제에 실패했습니다.");
                }
            });
        }
    });

    $('#editResourceModal').attr('aria-hidden', 'true');
    document.querySelectorAll('button, input, select, textarea').forEach(element => {
        element.blur();
    });
    $('#addResourceModal').attr('aria-hidden', 'true');
    document.querySelectorAll('button, input, select, textarea').forEach(element => {
        element.blur();
    });
    $('#editClose').click(function (){
        console.log("눌렸다")
    })
});
// 수정 모달 열기
function openEditModal() {
    const selectedCheckbox = $('.checkbox:checked');

    if (selectedCheckbox.length !== 1) {
        alert("수정할 자원을 하나 선택해주세요.");
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