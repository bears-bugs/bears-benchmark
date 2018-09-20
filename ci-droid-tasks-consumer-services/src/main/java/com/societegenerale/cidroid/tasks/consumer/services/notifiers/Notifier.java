package com.societegenerale.cidroid.tasks.consumer.services.notifiers;

import com.societegenerale.cidroid.tasks.consumer.services.model.Message;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;

import java.util.Map;

public interface Notifier {

    String PULL_REQUEST = "pullRequest";

    //TODO put user in the additionalInfos Map
    void notify(User user, Message message, Map<String,Object> additionalInfos);

    String getNotificationMode();
}
