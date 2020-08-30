package com.company.resourceapi;

import com.company.resourceapi.enums.ErrorCode;
import com.company.resourceapi.exceptions.BusinessException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class BusinessExceptionCustomMatcher extends TypeSafeMatcher<BusinessException> {


    private ErrorCode foundErrorCode;
    private final ErrorCode expectedErrorCode;

    public BusinessExceptionCustomMatcher(ErrorCode expectedErrorCode) {
        this.expectedErrorCode = expectedErrorCode;
    }

    @Override
    protected boolean matchesSafely(final BusinessException exception) {
        foundErrorCode = exception.getErrorCode();
        return foundErrorCode == expectedErrorCode;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(foundErrorCode)
            .appendText(" was not found instead of ")
            .appendValue(expectedErrorCode);
    }
}