package com.dionext.exoauth2demo.configuration;




import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler()
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, HttpServletRequest request) {
        log.error("Error ", ex);
        return new ResponseEntity<>(ex.toString(),  HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
