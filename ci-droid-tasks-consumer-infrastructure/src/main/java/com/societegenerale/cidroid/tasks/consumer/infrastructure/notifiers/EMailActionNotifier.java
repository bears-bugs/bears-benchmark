package com.societegenerale.cidroid.tasks.consumer.infrastructure.notifiers;

import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.ActionNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Slf4j
public class EMailActionNotifier implements ActionNotifier {

    private final MailSender javaMailSender;

    private final String mailSender;

    @Autowired
    public EMailActionNotifier(MailSender javaMailSender, String mailSender) {
        this.javaMailSender = javaMailSender;
        this.mailSender = mailSender;
    }

    @Override
    public void notify(User recipientUser, String subject, String content) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(mailSender);
        email.setTo(recipientUser.getEmail());
        email.setSubject(subject);

        email.setText(content);

        this.javaMailSender.send(email);
    }
}
