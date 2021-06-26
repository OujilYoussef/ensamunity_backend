package com.project.ensamunity.service;


import com.project.ensamunity.exceptions.EnsamunityException;
import com.project.ensamunity.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
      public void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator messagePreparator=mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("EnsafCommunity@gmail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try{
            javaMailSender.send(messagePreparator);
            log.info("Activation email sent");
        } catch (MailException e){
            throw new EnsamunityException("Exception occurred when sending mail to recipient:"+notificationEmail.getRecipient());
        }
    }
}