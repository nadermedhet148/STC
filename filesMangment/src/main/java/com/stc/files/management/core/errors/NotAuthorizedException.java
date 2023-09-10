package com.stc.files.management.core.errors;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String errorKey) {
        super(errorKey);
    }
}
