package com.ljy.jwt.presentation;

import com.ljy.jwt.application.exception.AlreadySignUpedUserException;
import com.ljy.jwt.application.exception.UserNotFoundException;
import com.ljy.jwt.common.response.Response;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(AlreadySignUpedUserException.class)
    public Response<?> handle(AlreadySignUpedUserException e) {
        return Response.badRequest(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> handle(MethodArgumentNotValidException e) {
        return Response.badRequest(e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Response<?> handle(UserNotFoundException e) {
        return Response.badRequest(e.getMessage());
    }
}
