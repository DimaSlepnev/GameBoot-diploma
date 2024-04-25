package com.dmytro.gameboot.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailSender{
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void send(String to, String subject, String templateName, Context context) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("admin_game@boot.com");
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            LOGGER.error("Failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }
}
