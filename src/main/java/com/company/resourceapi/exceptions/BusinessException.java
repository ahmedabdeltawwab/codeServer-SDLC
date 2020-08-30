package com.company.resourceapi.exceptions;

import com.company.resourceapi.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BusinessException extends Exception {

    private final ErrorCode errorCode;
}
