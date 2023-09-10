package com.stc.files.management.web;

import com.stc.files.management.core.errors.NotAuthorizedException;
import com.stc.files.management.core.errors.NotFoundEntityException;
import com.stc.files.management.web.dto.responses.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;


@ControllerAdvice
public class ExceptionTranslator  {



    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleNotFoundEntityException(NotFoundEntityException ex, NativeWebRequest request) {
        return new ResponseEntity<>(new ErrorDTO("An error occurred: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleNotAuthorizedException(NotAuthorizedException ex, NativeWebRequest request) {
        return new ResponseEntity<>(new ErrorDTO("An error occurred: " + ex.getMessage()), HttpStatus.FORBIDDEN);
    }




}
