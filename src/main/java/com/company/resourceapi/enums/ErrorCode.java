package com.company.resourceapi.enums;

import static com.company.resourceapi.enums.ErrorCodeGroup.BAD_REQUEST;
import static com.company.resourceapi.enums.ErrorCodeGroup.CONFLICT;
import static com.company.resourceapi.enums.ErrorCodeGroup.NOT_FOUND;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    NOT_EXIST("-1", "app.general.error.not_found", NOT_FOUND),
    PARENT_SYSTEM_DOES_NOT_EXIST("-2", "app.general.error.system_does_not_exist", NOT_FOUND),
    PROJECT_ALREADY_EXIST("-3", "app.general.error.project_already_exist", CONFLICT),
    MISSING_PROJECT_EXTERNAL_ID("-4", "app.general.error.missing_project_external_id", BAD_REQUEST),
    ILLEGAL_VALUES("-5", "app.general.error.illegal_value", BAD_REQUEST),
    NEW_PROJECT_EXTERNAL_ID_CONFLICT_WITH_OTHER_EXTERNAL_ID_IN_SAME_SYSTEM("-6",
        "app.general.error.external_id_conflict_with_other_external_id_in_same_system", CONFLICT),
    MISSING_SDLC_SYSTEM_ID("-7", "app.general.error.missing_sdlc_system_id", BAD_REQUEST);

    private final String code;
    private final String message;
    private final ErrorCodeGroup ErrorCodeGroup;
}
