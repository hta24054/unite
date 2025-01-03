$(document).ready(function () {
    let originalValues = {};
    let editableFields = []; // 서버에서 가져온 수정 가능한 필드 목록
    let role;

    // 서버에서 editable-field 데이터를 가져옴
    function fetchEditableFields() {
        return $.ajax({
            url: '../emp/editable-field',
            method: 'GET',
            success: function (data) {
                editableFields = data.field; // 수정 가능한 필드 목록
                role = data.role;
            },
            error: function () {
                alert("수정 가능한 항목을 불러오는 데 실패했습니다.");
            }
        });
    }

    // 기존 값을 저장
    function saveOriginalValues() {
        editableFields.forEach(fieldName => {
            const fields = $(`[name='${fieldName}']`);

            if (fieldName === "lang" || fieldName === "cert") {
                // lang 또는 cert 배열 처리
                originalValues[fieldName] = []; // 배열로 초기화
                fields.each(function () {
                    const value = $(this).val();
                    if (value) {
                        originalValues[fieldName].push({
                            [fieldName === "lang" ? "langName" : "certName"]: value
                        });
                    }
                });
            } else {
                // 단일 필드 처리
                originalValues[fieldName] = fields.val(); // 단일 값 저장
            }
        });
    }

    // 수정 가능한 필드 활성화
    function enableEditableFields() {
        editableFields.forEach(fieldName => {
            const field = $(`[name='${fieldName}']`);
            field.removeAttr("readonly")
                .removeAttr("disabled")
                .css({
                    "border": "2px solid #007bff", // 테두리 강조
                    "background-color": "#f9f9f9" // 수정 가능 배경색
                });
        });
        $("#fileUploadSection").show(); // 파일 업로드 섹션 표시
    }

    // 수정 가능한 필드 비활성화
    function resetEditableFields() {
        console.log(originalValues);
        editableFields.forEach(fieldName => {
            const fields = $(`[name='${fieldName}']`);
            if (fieldName === "lang" || fieldName === "cert") {
                fields.each(function (index) {
                    const originalValue = originalValues[fieldName][index];
                    if (originalValue) {
                        $(this)
                            .val(originalValue[fieldName === "lang" ? "langName" : "certName"]) // 필드명 맞춤
                            .attr("readonly", "readonly")
                            .attr("disabled", "disabled")
                            .css({
                                "border": "none",
                                "background-color": "#fff"
                            });
                    } else {
                        $(this).val("")
                            .attr("readonly", "readonly")
                            .attr("disabled", "disabled")
                            .css({
                                "border": "none",
                                "background-color": "#fff"
                            });
                    }
                });
            } else {
                // 단일 필드 처리
                fields.val(originalValues[fieldName])
                    .attr("readonly", "readonly")
                    .attr("disabled", "disabled")
                    .css({
                        "border": "none", // 기본 테두리
                        "background-color": "#fff" // 기본 배경색
                    });
            }
        });
        $("#fileUploadSection").hide(); // 파일 업로드 섹션 숨기기
    }

    // 수정 버튼 클릭 이벤트
    $("#editButton").click(function () {
        if ($(this).text() === "수정") {
            saveOriginalValues(); // 기존 값 저장
            enableEditableFields(); // 수정 가능한 필드 활성화
            $('.cert-add').show();
            $('.lang-add').show();
            $('.remove-lang').show();
            $('.remove-cert').show();
            $(".remove_img").show();
            // 버튼 상태 변경
            $(this).text("취소")
                .attr("id", "cancelButton")
                .removeClass("btn-primary")
                .addClass("btn-secondary");

            $("#saveButton")
                .removeAttr("disabled")
                .removeClass("btn-secondary")
                .addClass("btn-success"); // 저장 버튼 활성화

        } else if ($(this).text() === "취소") {
            resetEditableFields(); // 기존 값 복원
            $('.cert-add').hide();
            $('.lang-add').hide();
            $('.remove-lang').hide();
            $('.remove-cert').hide();
            $(".remove_img").hide();
            // 버튼 상태 변경
            $(this).text("수정")
                .attr("id", "editButton")
                .removeClass("btn-secondary")
                .addClass("btn-primary")

            $("#saveButton").attr("disabled", "disabled")
                .removeClass("btn-success")
                .addClass("btn-secondary"); // 저장 버튼 비활성화
        }
    });

    let fileChange = 0;
    $("#file").change(function (event) {
        fileChange++; //파일이 변경되면 fileChange 값 0 -> 1
        const maxSizeInBytes = 5 * 1024 * 1024;
        const file = this.files[0];
        if (file.size > maxSizeInBytes) {
            alert("업로드 파일 용량 제한 : 5MB");
            $(this).val('');
        }
        const fileURL = URL.createObjectURL(file);
        $('#over_view').attr('src', fileURL);
    });

    $('.remove_img').click(function () {
        if (confirm("첨부파일을 삭제하시겠습니까?")) {
            $("#fileName").text('');
            $(this).css('display', 'none');
            $("#over_view").attr('src', '/image/profile_navy.png');
        }
    })

    // 저장 버튼 클릭 이벤트
    $("#infoForm").submit(function (event) {
        event.preventDefault();

        if (!confirm("수정하시겠습니까?")) {
            return false;
        }

        let isValid = true;
        $('#infoForm [required]').each(function () {
            if ($(this).val() === '') {
                const errorMessage = $(this).data('name');
                alert(errorMessage + '을(를) 입력해 주세요');
                $(this).focus();
                isValid = false;
                return false;
            }
        });
        if (!isValid) return;

        const formData = new FormData(); // FormData 객체 생성
        const dto = {}; // JSON 데이터를 저장할 객체

        editableFields.forEach(fieldName => {
            const fields = $(`[name='${fieldName}']`);
            if (fieldName === "lang" || fieldName === "cert") {
                // lang 또는 cert 배열 처리
                dto[fieldName] = [];
                fields.each(function () {
                    const value = $(this).val();
                    if (value) {
                        dto[fieldName].push({
                            empId: $("input[name='empId']").val(),
                            [fieldName === "lang" ? "langName" : "certName"]: value
                        });
                    }
                });
            } else {
                // 단일 필드 처리
                dto[fieldName] = fields.val();
            }
        });

        const fileInput = $('input[name="file"]')[0];
        if (fileInput && fileInput.files.length > 0) {
            // 파일이 선택된 경우
            formData.append('file', fileInput.files[0]); // 첫 번째 파일 추가
        }

        // 기존 파일이 유지되는 경우 처리
        if (fileChange === 0) {
            const fileName = $('#fileName').text().trim();
            if (fileName) {
                dto['beforeFileName']=(fileName);
            }
        }

        formData.append("dto", JSON.stringify(dto)); // JSON 데이터를 문자열로 추가

        $.ajax({
            url: role === "ROLE_ADMIN" ? `/emp/admin/${$("input[name='empId']").val()}` : `/emp/${$("input[name='empId']").val()}`,
            method: 'PATCH',
            processData: false, // FormData 객체 사용 시 필수
            contentType: false, // FormData 객체 사용 시 필수
            data: formData,
            success: function (data) {
                alert("수정이 완료되었습니다.");
                location.reload();
            },
            error: function () {
                alert("수정 중 오류가 발생했습니다.");
            }
        });
    });

    // 페이지 로드 시 서버에서 수정 가능한 필드 가져오기
    fetchEditableFields().then(function () {
        console.log("Editable fields loaded:", editableFields);
    });
    // 자격증 추가 버튼 클릭
    $('#certifications').on('click', '.add-cert', function () {
        const newCert = `
				<div class="cert-item">
					<input type="text" name="cert" class="form-control cert-input" placeholder="자격증명" data-name="자격증" required>
					<button type="button" class="btn btn-sm btn-light remove-cert">-</button>
				</div>`;
        $(this).closest('.cert-add').before(newCert);
    });

    // 자격증 삭제 버튼 클릭
    $('#certifications').on('click', '.remove-cert', function () {
        $(this).closest('.cert-item').remove();
    });

    // 외국어능력 추가 버튼 클릭
    $('#languages').on('click', '.add-lang', function () {
        const newLang = `
				<div class="lang-item">
					<input type="text" name="lang" class="form-control lang-input" placeholder="외국어명"  data-name="외국어" required>
					<button type="button" class="btn btn-sm btn-light remove-lang">-</button>
				</div>`;
        $(this).closest('.lang-add').before(newLang);
    });

    // 외국어능력 삭제 버튼 클릭
    $('#languages').on('click', '.remove-lang', function () {
        $(this).closest('.lang-item').remove();
    });
});
