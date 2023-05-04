package com.ljy.jwt.application.exception;

public class AlreadySignUpedUserException extends RuntimeException {
    public AlreadySignUpedUserException(String message) {
        super(message);
    }
}
