$(document).ready(function () {
    let alluserid = [];

    // 모달 열기 전 상태 초기화
    function resetModal(targetId) {
        // 모달 내부의 모든 체크박스를 초기화
        $('#orgChartModal input[type="checkbox"]').prop('checked', false);
        // div 내부의 값들 초기화
        $('#selectedEmployees').empty();

        let inputValue = '';
        // 폼의 값이 있을 때 해당 값을 처리
        if (targetId === 'manager') {
            inputValue = $('#manager').val();
        } else if (targetId === 'participants') {
            inputValue = $('#participants').val();
        } else if (targetId === 'viewers') {
            inputValue = $('#viewers').val();
        }
        let empId;
        if (inputValue) {
            // 쉼표로 구분된 값을 배열로 나누어 반복 처리
            inputValue.split(', ').forEach(function (empName) {
                // 직원 이름을 찾아서 해당 체크박스를 선택
                $('#orgChartModal input[name="selectedEmp"]').each(function () {
                    const employeeNameInModal = $(this).closest('tr').find('td:eq(2)').text().trim();
                    if (employeeNameInModal === empName) {
                        $(this).prop('checked', true);  // 체크박스 체크
                    }
                    empId = $(this).val(); // 선택된 empId
                    if (!alluserid.includes(empId)) {
                        alluserid.push(empId); // empId를 alluserid 배열에 추가
                    }
                });

                // 선택된 직원 이름을 div에 추가
                const selectedEmployeesDiv = $('#selectedEmployees');
                const empLine = `
	                <div class="emp-line" data-empid="${empId}" data-name="${empName}" style="display: flex; align-items: center; margin-bottom: 5px;">
	                    <span style="flex-grow: 1;">${empName}</span>
	                    <img src="/image/delete.png" class="remove-icon" 
	                         style="cursor: pointer; width: 16px; height: 16px;" alt="삭제">
	                </div>
	            `;
                // 중복 추가 방지
                if (selectedEmployeesDiv.find(`[data-name="${empName}"]`).length === 0) {
                    selectedEmployeesDiv.append(empLine);
                }
            });
        }
    }

    $('.orgChartIcon').on('click', function (event) {
        event.preventDefault();

        const targetId = $(this).data('target');  // 클릭된 아이콘의 data-target 값을 가져옵니다.
        const targetText = getTargetText(targetId);  // 타겟에 맞는 텍스트 가져오기

        localStorage.setItem('selectedInputId', targetId);
        resetModal(targetId); // 모달 상태 초기화

        // 모달 제목을 업데이트합니다.
        $('#orgChartModalLabel').text(targetText + ' 조직도');

        // 모달을 보여줍니다.
        $('#orgChartModal').modal('show');
    });

    function getTargetText(targetId) {
        switch(targetId) {
            case 'managerId':
                return '책임자';
            case 'participants':
                return '참여자';
            case 'viewers':
                return '열람자';
            default:
                return '조직도';
        }
    }
    // 조직도 아이콘 클릭 이벤트 처리 (책임자, 참여자, 열람자)
    $('.orgChartIcon').on('click', function (event) {
        event.preventDefault();
        const targetId = $(this).data('target');
        localStorage.setItem('selectedInputId', targetId);
        resetModal(targetId); // 모달 상태 초기화
        $('#orgChartModal').modal('show');
    });

    // "선택" 열 추가
    $('#employeeTableContainer thead tr').prepend('<th>선택</th>');

    // 직원 목록 행에 체크박스를 추가하는 함수
    function addCheckboxes() {
        $('#employeeTableBody tr').each(function () {
            const empId = $(this).find('td:eq(0)').text().trim();
            $(this).prepend('<td><input type="checkbox" name="selectedEmp" value="' + empId + '"></td>');
            $(this).find('td:eq(1)').hide();  // empId 열 숨기기
        });
    }

    // 최초 로딩 시 체크박스 추가
    addCheckboxes();

    // AJAX 후 체크박스를 다시 추가
    $(document).on('ajaxSuccess', function () {
        addCheckboxes();
    });

    // 추가 버튼 클릭 시 선택된 직원의 이름을 쉼표로 구분해 textarea에 추가
    $('#addEmpBtn').on('click', function () {
        console.log("alluserfirst", alluserid); // alluserid 배열 상태 출력
        const selectedEmpNames = $('input[name="selectedEmp"]:checked')
            .map(function () {
                return $(this).closest('tr').find('td:eq(2)').text().trim(); // 직원 이름
            })
            .get();

        if (selectedEmpNames.length > 0) {
            const selectedEmployeesDiv = $('#selectedEmployees');

            selectedEmpNames.forEach(function (empName) {
                // 해당 직원의 empId를 찾기
                const empId = $('input[name="selectedEmp"]:checked')
                    .map(function () {
                        const nameInRow = $(this).closest('tr').find('td:eq(2)').text().trim();
                        if (nameInRow === empName) {
                            return $(this).val();  // 해당 직원의 empId 반환
                        }
                    })
                    .get()[0];  // 배열의 첫 번째 값(첫 번째 일치하는 empId)

                // 이미 해당 직원이 alluserid 배열에 포함되어 있으면 중복 경고
                if (alluserid.includes(empId)) {
                    alert('이 직원은 이미 다른 역할에 소속되어 있습니다.');
                    // 중복된 직원을 #selectedEmployees div에서 제거
                    selectedEmployeesDiv.find(`[data-name="${empName}"]`).remove();
                    return;  // 중복 방지
                }

                // 중복되지 않은 경우에만 추가
                alluserid.push(empId);  // alluserid에 empId 추가

                const empLine = `
                    <div class="emp-line" data-name="${empName}" data-emp-id="${empId}" style="display: flex; align-items: center; margin-bottom: 5px;">
                        <span style="flex-grow: 1;">${empName}</span>
                        <input type="hidden" id="empid" name="empid" value="${empId}">
                        <img src="/image/delete.png" class="remove-icon" 
                             style="cursor: pointer; width: 16px; height: 16px;" alt="삭제">
                    </div>
                `;
                selectedEmployeesDiv.append(empLine);
            });

            console.log("alluserlast", alluserid); // alluserid 배열 상태 출력

            // 선택된 직원 후 체크박스 모두 해제
            $('input[name="selectedEmp"]').prop('checked', false);
        } else {
            alert('직원을 선택해 주세요.');
        }
    });

    // 삭제 아이콘 클릭 이벤트 처리
    $(document).on('click', '.remove-icon', function () {
        const empLine = $(this).parent('.emp-line'); // 삭제할 직원 줄
        const empName = empLine.data('name'); // 삭제할 직원 이름
        const empId = empLine.data('emp-id'); // empId를 가져옵니다. (emp-line에 저장된 emp-id 값)
        console.log(empId);
        // checkbox 체크 해제
        $('input[name="selectedEmp"]').each(function () {
            const checkboxRow = $(this).closest('tr'); // 현재 체크박스가 있는 행
            const employeeNameInRow = checkboxRow.find('td:eq(2)').text().trim(); // 직원 이름 가져오기
            if (employeeNameInRow === empName) {
                $(this).prop('checked', false); // 체크 해제
            }
        });

        // div에서 삭제
        empLine.remove();

        // alluserid 배열에서 해당 empId 제거
        alluserid = alluserid.filter(id => id !== String(empId)); //갑제거
        console.log("empid" + empId);
        console.log("alluserid" + alluserid); // 삭제 후 배열 상태 출력
    });


    // 등록 버튼 클릭 시 textarea에 있는 직원 정보를 실제 폼에 등록
    $('#insertEmpBtn').on('click', function () {
        const targetInputId = localStorage.getItem('selectedInputId');
        if (!targetInputId) {
            alert('유효한 입력 필드가 선택되지 않았습니다.');
            return;
        }

        const selectedEmployees = $('#selectedEmployees .emp-line');
        const selectedEmpNames = [];
        console.log(selectedEmpNames);
        selectedEmployees.each(function () {
            const empName = $(this).data('name');
            const empId = $(this).data('emp-id');
            selectedEmpNames.push(empName);
        });
        console.log("selectEmpNames", selectedEmpNames);
        if (selectedEmpNames.length === 0) {
            $('#' + targetInputId).val('');
            $('#orgChartModal').modal('hide');
        }

        if (targetInputId === 'managerId') {
            const empNamesArray = selectedEmpNames.filter(name => name.trim() !== '');
            if (empNamesArray.length > 1) {
                alert('책임자는 한 명만 지정할 수 있습니다.');
                return;
            }
        }

        const targetInput = $('#' + targetInputId);
        targetInput.prop('readonly', false);

        const formattedNamesAndIds = selectedEmpNames.map((name, index) => {
            return `${name}`;
        }).join(', ');

        targetInput.val(formattedNamesAndIds);
        targetInput.prop('readonly', true);
        console.log('formattedNamesAndIds', formattedNamesAndIds);
        alert('등록된 직원: ' + formattedNamesAndIds);
        $('#orgChartModal').modal('hide');
    });


    $('#projectForm').on('submit', function (e) {
        const manager = $('#managerId').val().trim();
        if (!manager) {
            alert('책임자를 지정해 주세요.');
            e.preventDefault();  // 제출 취소
        }
        if (!alluserid.includes(userid)) {
            console.log("all", alluserid);
            console.log("user", userid);
            alert("프로젝트에 본인이 포함되야 합니다");
            e.preventDefault();
        }

    });
});
