package com.societegenerale.cidroid.tasks.consumer.infrastructure.mocks;

import com.societegenerale.cidroid.tasks.consumer.services.model.Message;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotifierMock implements Notifier {

    @Getter
    private List<Pair<User,Message>> notifications=new ArrayList<>();

    @Override
    public void notify(User user, Message message,Map additionalInfos) {
        notifications.add(new ImmutablePair(user, message));
    }

    @Override
    public String getNotificationMode() {
        return "inMemoryMock";
    }
}
