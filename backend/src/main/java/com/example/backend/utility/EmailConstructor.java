package com.example.backend.utility;

import com.example.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Objects;
import java.util.Properties;

@Component
public class EmailConstructor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment environment;

    @Autowired
    private TemplateEngine templateEngine;

    public MimeMessagePreparator constructNewUserEmaiddl(User user, String password) {
        logger.info("constructNewUserEmail");
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("password", password);
        String text = templateEngine.process("userRegistersTemplate", context);
        return mimeMessage -> {
            logger.info("Preparing email");

            MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
            email.setPriority(1);
            email.setTo(user.getEmail());
            email.setSubject("Sign-up mail");
            email.setText(text, true);
            email.setFrom(new InternetAddress(Objects.requireNonNull(environment.getProperty("support.email"))));

        };






    }


public void constructNewUserEmail(User user, String password) {
    Properties prop = new Properties();
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true");
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "465");
    prop.put("mail.smtp.socketFactory.port", "465");
    prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    Session session = Session.getInstance(prop, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("odak.skywalker@gmail.com", "@@Dr.Green");
        }
    });

    Message message = new MimeMessage(session);
    Context context = new Context();
    logger.info("USERNAME {}", user.getFirstName());
    context.setVariable("username", user.getUsername());
    context.setVariable("firstName", user.getFirstName());
    context.setVariable("password", password);
    try {
        message.setFrom(new InternetAddress(Objects.requireNonNull(environment.getProperty("support.email"))));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        message.setSubject("Registration on sampleApp");

        String msg = templateEngine.process("userRegistersTemplate", context);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    } catch (Exception e){
        logger.error("E {} {}", e.getMessage(), e);
    }


}

}
