package org.project.multilogin.exception;
import org.project.multilogin.model.BaseResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<BaseResponseModel> handleApplicationException(ApplicationException globalException) {
        BaseResponseModel errorResponse;
        HttpStatus status;
        switch (globalException.getEApplicationExceptionCode()) {
            case USER_ALREADY_EXISTS -> {
                errorResponse = new BaseResponseModel(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(), false, globalException.getMessage(), globalException.getData(), LocalDateTime.now());
                status = HttpStatus.CONFLICT;
            }
            case USER_NOT_FOUND -> {
                errorResponse = new BaseResponseModel(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), false, globalException.getMessage(), globalException.getData(), LocalDateTime.now());
                status = HttpStatus.NOT_FOUND;
            }
            default -> {
                errorResponse = new BaseResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(), false, globalException.getMessage(), globalException.getData(), LocalDateTime.now());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponseModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        Map<String, String> errorMessages = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(fieldError -> errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ResponseEntity<>(new BaseResponseModel(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), false, methodArgumentNotValidException.getMessage(), errorMessages, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponseModel> handleException(Exception exception){
        Map<String, String> errorMessages = new HashMap<>();
        return new ResponseEntity<>(new BaseResponseModel(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), false, exception.getMessage(), errorMessages, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
