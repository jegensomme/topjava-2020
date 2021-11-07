package ru.javawebinar.topjava.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class WebUtils {
    private WebUtils() {
    }

    public static ResponseEntity<String> getBindingErrorResponse(BindingResult errors) {
        if (!errors.hasErrors()) {
            throw new IllegalArgumentException("binding result has no errors!");
        }
        String errorFieldsMsg = errors.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("<br>"));
        return ResponseEntity.unprocessableEntity().body(errorFieldsMsg);
    }
}
