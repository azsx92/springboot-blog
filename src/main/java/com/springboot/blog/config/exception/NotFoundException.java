package com.springboot.blog.config.exception;

import com.springboot.blog.config.ErrorCode;

public class NotFoundException extends BusinessBaseException{
    public NotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
