$(document).ready(function () {
    loadResourceTable();

    //자원목록 가져오기
    function loadResourceTable() {
        $.ajax({
            url: "../admin/resource/list",
            type: "GET",
            success: function (data) {
                const $tbody = $("#resource_table tbody");
                $tbody.empty(); // 기존 테이블 내용 제거

                data.forEach(resource => {
                    const usableText = resource.resourceUsable ? "가능" : "불가";
                    const row = `
                        <tr>
                            <td>
                                <input type="checkbox" class="checkbox" name="resourceId" value="${resource.resourceId}">
                            </td>
                            <td style="text-align: left;">${resource.resourceType}</td>
                            <td style="text-align: left;">${resource.resourceName}</td>
                            <td style="text-align: left;">${resource.resourceInfo}</td>
                            <td>${usableText}</td>
                        </tr>
                    `;
                    $tbody.append(row);
                });
            },
            error: function () {
                alert("자원 목록을 불러오는 데 실패했습니다.");
            }
        });
    }
    // 자원등록
    $("#insertResource").submit(function (event) {
        event.preventDefault();
        $.ajax({
            url: "../admin/resource",
            type: "POST",
            data: {
                resourceType: $('#resourceType').val(),
                resourceName: $('#resourceName').val(),
                resourceInfo: $('#resourceInfo').val(),
                resourceUsable: $('#resourceUsable').val(),
            },
            success: function (data) {
                alert(data);
                $('#addResourceModal').modal('hide'); // Bootstrap 메서드 사용
                loadResourceTable();
            },
            error: function () {
                alert("등록에 실패했습니다.");
            }
        });
    });
    // 자원수정
    $("#updateResource").submit(function (event) {
        event.preventDefault();
        $.ajax({
            url: "../admin/resource",
            type: "PATCH",
            data: {
                resourceId: $('#editResourceId').val(),
                resourceType: $('#editResourceType').val(),
                resourceName: $('#editResourceName').val(),
                resourceInfo: $('#editResourceInfo').val(),
                resourceUsable: $('#editResourceUsable').val(),
            },
            success: function (data) {
                alert(data);
                $('#editResourceModal').modal('hide'); // Bootstrap 메서드 사용
                loadResourceTable();
            },
            error: function () {
                alert("등록에 실패했습니다.");
            }
        });
    });

    //자원삭제
    $("#deleteButton").click(function () {
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
            $('#deleteResourceIds').val(selectedIds.join(','));
            $.ajax({
                url: "../admin/resource",
                type: "DELETE",
                data: {selectedIds},
                success: function (data) {
                    alert(data);
                    loadResourceTable();
                },
                error: function () {
                    alert("등록에 실패했습니다.");
                }
            });
        }
    });
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