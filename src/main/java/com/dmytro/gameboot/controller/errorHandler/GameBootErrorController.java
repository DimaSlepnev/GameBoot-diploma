package com.dmytro.gameboot.controller.errorHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameBootErrorController implements ErrorController {

    private static Logger logger = LoggerFactory.getLogger(GameBootErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                log(statusCode, request);
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                log(statusCode, request);
                return "error/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                log(statusCode, request);
                return "error/403";
            }
        }
        log(500, request);
        return "error/500";
    }
    private void log(Integer statusCode, HttpServletRequest request) {
        logger.warn("User get error {} from path {}", statusCode, request.getRequestURI());
    }
}
