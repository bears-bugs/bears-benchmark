package com.societegenerale.cidroid.tasks.consumer.infrastructure.notifiers;

import com.societegenerale.cidroid.tasks.consumer.services.model.Message;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.Map;

@Slf4j
public class EMailNotifier implements Notifier {

    private final MailSender javaMailSender;

    private final String mailSender;

    @Autowired
    public EMailNotifier(MailSender javaMailSender,String mailSender) {
        this.javaMailSender=javaMailSender;
        this.mailSender=mailSender;
    }

    @Override
    public void notify(User user, Message message, Map<String,Object> additionalInfos) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(mailSender);
        email.setTo(user.getEmail());
        email.setSubject("Pull Request Not Mergeable");

        PullRequest pr=(PullRequest)additionalInfos.get(PULL_REQUEST);

        if(pr==null){
            log.warn("can't notify, as we don't have access to the PR");
            return;
        }

        String additionalContent=String.format("\n\n link : %s",pr.getHtmlUrl());
        email.setText(message.getContent()+additionalContent);

        this.javaMailSender.send(email);
    }



    @Override
    public String getNotificationMode() {
        return "EMAIL";
    }


}
