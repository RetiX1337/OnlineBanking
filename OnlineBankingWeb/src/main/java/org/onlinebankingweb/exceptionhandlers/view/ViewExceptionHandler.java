package org.onlinebankingweb.exceptionhandlers.view;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "org.onlinebankingweb.controllers.view")
public class ViewExceptionHandler {
    //TODO add logging
    @ExceptionHandler(Exception.class)
    public String handleAnyException(Model model) {
        model.addAttribute("exceptionMessage", "Some unexpected error has occurred...");
        return "error-page";
    }
}
