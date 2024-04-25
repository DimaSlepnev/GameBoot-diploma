package com.dmytro.gameboot.email;


import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;

public interface EmailSender {
    @Async
    void send(String to, String subject, String templateName, Context context);
}
