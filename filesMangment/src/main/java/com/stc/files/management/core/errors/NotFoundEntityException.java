package com.stc.files.management.core.errors;

public class NotFoundEntityException extends RuntimeException {
    public NotFoundEntityException(String errorKey) {
        super(errorKey);
    }
}
