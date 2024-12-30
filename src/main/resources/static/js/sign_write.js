$(document).ready(function () {//결재자관련
    let signCount = 0;
    const maxSignCount = 3; // 최대 결재자 수
    const initialWidth = 120; // 초기 폭 설정

// 직원 목록에서 더블클릭하여 결재자 추가
    $(document).on("dblclick", "#employeeTableBody tr", function () {
        const empId = $(this).find("td:eq(0)").text(); // 사번 가져오기
        const name = $(this).find("td:eq(1)").text(); // 이름 가져오기

        // 결재자 칸 추가
        addSigner(name, empId);
        $('#employeeModal').modal('hide');
    });

// 결재자 칸 추가 함수
    function addSigner(name, empId) {
        //이미 추가된 결재자면 등록 불가
        const exists = $('input[name="sign[]"]').filter(function () {
            return $(this).val() === empId;
        }).length > 0;

        if (exists) {
            alert("이미 추가된 결재자입니다.");
            return;
        }

        if (signCount >= maxSignCount) {
            alert("최대 3명까지 결재자를 추가할 수 있습니다.");
            return;
        }

        signCount++;

        // 새로운 결재자 칸 추가
        $('#approvalTable tr:first').append('<th class="label-cell">결재자 ' + signCount + '</th>');
        $('#approvalTable tr:nth-child(2)').append('<td class="name">' + name + '</td>');

        // 숨겨진 필드로 empId 추가
        $('#approvalTable').append('<input type="hidden" name="sign[]" value="' + empId + '">');

        // 테이블의 부모 컨테이너 너비 조정 (좌측으로 확장되도록)
        $('.approval-table').css('width', initialWidth + signCount * 100 + 'px');
    }

// 결재자 삭제 버튼
    $("#deleteButton").click(function () {
        signCount = 0;

        // 결재자 칸을 모두 삭제하고, 기안자만 남기기
        $('#approvalTable tr:first th:gt(0)').remove();
        $('#approvalTable tr:nth-child(2) td:gt(0)').remove();

        $('input[name="sign[]"]').not(':first').remove();

        $('.approval-table').css('width', initialWidth + 'px'); // 초기 폭으로 설정
    });
});