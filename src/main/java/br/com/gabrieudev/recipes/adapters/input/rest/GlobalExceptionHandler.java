package br.com.gabrieudev.recipes.adapters.input.rest;

import java.util.Objects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.gabrieudev.recipes.adapters.input.rest.dtos.ApiResponseDTO;
import br.com.gabrieudev.recipes.application.exceptions.AlreadyExistsException;
import br.com.gabrieudev.recipes.application.exceptions.BadCredentialsException;
import br.com.gabrieudev.recipes.application.exceptions.BusinessRuleException;
import br.com.gabrieudev.recipes.application.exceptions.EmailException;
import br.com.gabrieudev.recipes.application.exceptions.InternalErrorException;
import br.com.gabrieudev.recipes.application.exceptions.InvalidTokenException;
import br.com.gabrieudev.recipes.application.exceptions.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleUserNotFoundException(NotFoundException e) {
        ApiResponseDTO<String> apiResponse = ApiResponseDTO.error(e.getMessage(), 404);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleBadCredentialsException(BadCredentialsException e) {
        ApiResponseDTO<String> apiResponse = ApiResponseDTO.error(e.getMessage(), 400);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleUserAlreadyExistsException(AlreadyExistsException e) {
        ApiResponseDTO<String> apiResponse = ApiResponseDTO.error(e.getMessage(), 409);
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleInvalidTokenException(InvalidTokenException e) {
        ApiResponseDTO<String> apiResponse = ApiResponseDTO.error(e.getMessage(), 401);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        ApiResponseDTO<String> apiResponse = ApiResponseDTO.error(e.getMessage(), 500);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponseDTO<String> apiResponse = ApiResponseDTO.error(Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), 422);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleBusinessRuleException(BusinessRuleException e) {
        ApiResponseDTO<String> apiResponse = ApiResponseDTO.error(e.getMessage(), 409);
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleEmailException(EmailException e) {
        ApiResponseDTO<String> apiResponse = ApiResponseDTO.error(e.getMessage(), 500);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleInternalErrorException(InternalErrorException e) {
        ApiResponseDTO<String> apiResponse = ApiResponseDTO.error(e.getMessage(), 500);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
