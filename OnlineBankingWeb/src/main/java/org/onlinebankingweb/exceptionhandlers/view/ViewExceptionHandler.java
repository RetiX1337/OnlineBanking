package org.onlinebankingweb.exceptionhandlers.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlinebankingweb.exceptionhandlers.rest.RestExceptionHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "org.onlinebankingweb.controllers.view")
public class ViewExceptionHandler {
    private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    public String handleAnyException(Model model, Exception e) {
        logger.error(e);
        model.addAttribute("exceptionMessage", "Some unexpected error has occurred...");
        return "error-page";
    }
}
