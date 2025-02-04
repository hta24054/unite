const contextPath = /*[[@{/}]]*/ '';
$(document).ready(function () {
    let originalValues = {};
    let editableFields = [];
    let role;
    let fileChange = 0;

    /** 🟢 서버에서 수정 가능한 필드 가져오기 */
    function fetchEditableFields() {
        return $.ajax({
            url: contextPath + '/api/emp/editable-field',
            method: 'GET',
            success: function (data) {
                editableFields = data.field;
                role = data.role;
            },
            error: function () {
                alert("수정 가능한 항목을 불러오는 데 실패했습니다.");
            }
        });
    }

    /** 🟢 기존 값 저장 */
    function saveOriginalValues() {
        originalValues = {}; // 기존 값 초기화
        editableFields.forEach(fieldName => {
            const fields = $(`[name='${fieldName}']`);
            if (fieldName === "lang" || fieldName === "cert") {
                originalValues[fieldName] = fields.map(function () {
                    return { name: $(this).val() };
                }).get();
            } else {
                originalValues[fieldName] = fields.val();
            }
        });
    }

    /** 🟢 수정 가능한 필드만 활성화 */
    function enableEditableFields() {
        editableFields.forEach(fieldName => {
            const field = $(`[name='${fieldName}']`);
            if (field.length) {
                field.removeAttr("readonly").removeAttr("disabled").addClass("editing");
            }
        });
        $("#fileUploadSection").show();
        // 🔥 lang 또는 cert가 있는 경우에만 추가 버튼 표시
        if (editableFields.includes("lang")) {
            $(".lang-add").show();
            $(".remove-lang").show();
        }
        if (editableFields.includes("cert")) {
            $(".cert-add").show();
            $(".remove-cert").show();
        }

        $(".remove_img").show();
    }

    /** 🟢 취소 시 이전 상태 복구 */
    function resetEditableFields() {
        editableFields.forEach(fieldName => {
            const fields = $(`[name='${fieldName}']`);
            fields.each(function (index) {
                const originalValue = originalValues[fieldName]?.[index]?.name || originalValues[fieldName] || "";
                $(this).val(originalValue).attr("readonly", true).attr("disabled", true).removeClass("editing");
            });
        });
        $("#fileUploadSection").hide();
        $(".cert-add, .lang-add, .remove-lang, .remove-cert, .remove_img").hide();
    }

    /** 🟢 수정 버튼 클릭 이벤트 */
    $("#editButton").click(function () {
        if ($(this).text() === "수정") {
            saveOriginalValues();
            enableEditableFields();
            $(this).text("취소").attr("id", "cancelButton").removeClass("btn-primary").addClass("btn-secondary");
            $("#saveButton").removeAttr("disabled").removeClass("btn-secondary").addClass("btn-success");
        } else {
            resetEditableFields();
            $(this).text("수정").attr("id", "editButton").removeClass("btn-secondary").addClass("btn-primary");
            $("#saveButton").attr("disabled", "disabled").removeClass("btn-success").addClass("btn-secondary");
        }
    });
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
                dto['beforeFileName'] = (fileName);
            }
        }

        formData.append("dto", JSON.stringify(dto)); // JSON 데이터를 문자열로 추가

        $.ajax({
            url: role === "ROLE_ADMIN" ? `/api/emp/admin/${$("input[name='empId']").val()}` : `/api/emp/${$("input[name='empId']").val()}`,
            method: 'PATCH',
            processData: false, // FormData 객체 사용 시 필수
            contentType: false, // FormData 객체 사용 시 필수
            data: formData,
            success: function (data) {
                if (data === 1) {
                    alert("수정이 완료되었습니다.");
                } else {
                    alert("수정 중 오류가 발생했습니다.");
                }
                location.reload();
            },
            error: function () {
                alert("수정 중 오류가 발생했습니다.");
            }
        });
    });
    /** 🟢 서버에서 수정 가능한 필드 가져오기 */
    fetchEditableFields().then(() => console.log("Editable fields loaded:", editableFields));

    /** 🟢 자격증 추가 */
    $('#certifications').on('click', '.add-cert', function () {
        $(this).closest('.cert-add').before(`
            <div class="cert-item">
                <input type="text" name="cert" class="form-control cert-input" placeholder="자격증명" required>
                <button type="button" class="btn btn-sm btn-light remove-cert">-</button>
            </div>
        `);
    });

    /** 🟢 자격증 삭제 */
    $('#certifications').on('click', '.remove-cert', function () {
        $(this).closest('.cert-item').remove();
    });

    /** 🟢 외국어 추가 */
    $('#languages').on('click', '.add-lang', function () {
        $(this).closest('.lang-add').before(`
            <div class="lang-item">
                <input type="text" name="lang" class="form-control lang-input" placeholder="외국어명" required>
                <button type="button" class="btn btn-sm btn-light remove-lang">-</button>
            </div>
        `);
    });

    /** 🟢 외국어 삭제 */
    $('#languages').on('click', '.remove-lang', function () {
        $(this).closest('.lang-item').remove();
    });
});