package com.stock.exchange.controller;

import com.stock.exchange.model.ErrorObject;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(value = {SignatureException.class})
    public ResponseEntity<ErrorObject> handleSignatureException(SignatureException signatureException) {
        ErrorObject errorObject = new ErrorObject(String.valueOf(HttpStatus.UNAUTHORIZED.value()), signatureException.getMessage());
        return new ResponseEntity<>(errorObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {MalformedJwtException.class})
    public ResponseEntity<ErrorObject> handleMalformedJwtException(MalformedJwtException malformedJwtException) {
        ErrorObject errorObject = new ErrorObject(String.valueOf(HttpStatus.UNAUTHORIZED.value()), malformedJwtException.getMessage());
        return new ResponseEntity<>(errorObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<ErrorObject> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        ErrorObject errorObject = new ErrorObject(String.valueOf(HttpStatus.UNAUTHORIZED.value()), badCredentialsException.getMessage());
        return new ResponseEntity<>(errorObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity<ErrorObject> handleExpiredJwtException(ExpiredJwtException expiredJwtException) {
        ErrorObject errorObject = new ErrorObject(String.valueOf(HttpStatus.FORBIDDEN.value()), expiredJwtException.getMessage());
        return new ResponseEntity<>(errorObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ErrorObject> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        ErrorObject errorObject = new ErrorObject(String.valueOf(HttpStatus.FORBIDDEN.value()), accessDeniedException.getMessage());
        return new ResponseEntity<>(errorObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ErrorObject> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        ErrorObject errorObject = new ErrorObject(String.valueOf(HttpStatus.NOT_FOUND.value()), entityNotFoundException.getMessage());
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }
}
