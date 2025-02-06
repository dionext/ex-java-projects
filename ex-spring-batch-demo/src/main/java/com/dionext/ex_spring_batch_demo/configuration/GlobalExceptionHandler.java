package com.dionext.ex_spring_batch_demo.configuration;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler()
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, HttpServletRequest request) {
        log.error("handleAllExceptions", ex);
        return processException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity<Object> processException(Exception ex, HttpServletRequest request, HttpStatusCode status) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + "; charset=utf-8");
        return new ResponseEntity<>(makeSimpleErrorPageBody(ex.toString(), printStackTrace(ex)), responseHeaders, status);
    }
    private String printStackTrace(Exception e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    protected String makeSimpleErrorPageBody(String errorMessage, String stackTrace) {
        return MessageFormat.format("""
                        <html xmlns="http://www.w3.org/1999/xhtml" lang="en">
                            <head>
                                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                                <meta charset="utf-8"/>
                                <meta http-equiv="Content-Language" content="en"/>
                                <title>Error</title>
                            </head>
                            <body>
                            <h3 align="center">{0}</h3>
                            <pre/>{1} </pre>
                            </body>
                        </html>
                        """,
                errorMessage, stackTrace);
    }

}
