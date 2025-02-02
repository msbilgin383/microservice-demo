package com.example.book.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GlobalExceptionHandler implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", request.getAttribute("jakarta.servlet.error.status_code"));
        errorDetails.put("error", request.getAttribute("jakarta.servlet.error.message"));
        errorDetails.put("path", request.getAttribute("jakarta.servlet.error.request_uri"));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
