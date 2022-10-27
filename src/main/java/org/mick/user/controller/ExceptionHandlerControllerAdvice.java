package org.mick.user.controller;

import org.mick.user.Dto.ErrorMessage;
import org.mick.user.configuration.exception.ApplicationException;
import org.mick.user.configuration.exception.EntityNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.sql.SQLException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {
    public ExceptionHandlerControllerAdvice() {
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApplicationException.class)
    protected ErrorMessage handleApplicationException(HttpServletRequest req, ApplicationException ex) {
        return errorMessageException("EX08",ex);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    protected ErrorMessage handleDatabaseException(HttpServletRequest req, Exception ex) {
        return errorMessageException("EX07",ex);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    protected ErrorMessage handleEntityNotFoundException(HttpServletRequest req, EntityNotFoundException ex) {
        return errorMessageException("EX03",ex);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConstraintViolationException.class)
    protected ErrorMessage handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException ex) {
        return errorMessageException("EX02",ex);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ValidationException.class)
    protected ErrorMessage handleValidationException(HttpServletRequest req, ValidationException ex) {
        return errorMessageException("EX01",ex);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    protected ErrorMessage handleNullableException(HttpServletRequest req, NullPointerException ex) {
        return errorMessageException("EX01",ex);
    }

    public ErrorMessage errorMessageException(String cod, Exception ex){
        return new ErrorMessage(cod, ex.getMessage());
    }

}
