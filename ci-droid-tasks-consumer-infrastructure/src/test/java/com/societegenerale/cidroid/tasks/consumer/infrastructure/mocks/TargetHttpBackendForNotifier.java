package com.societegenerale.cidroid.tasks.consumer.infrastructure.mocks;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.codestory.http.WebServer;
import net.codestory.http.payload.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TargetHttpBackendForNotifier {

    public static final int TARGET_HTTP_BACKEND_MOCK_PORT=9901;

    private static boolean hasStarted = false;

    public static boolean hasStarted() {
        return hasStarted;
    }

    @Getter
    private List<String> notificationsReceived=new ArrayList();

    public boolean start() {

        WebServer gitHubWebServer = new WebServer();
        gitHubWebServer.configure(
                routes -> {
                    routes.post("/notify", context -> {
                                                        saveNotif(context.extract(String.class));
                                                        return Payload.created();
                    });


                }
        ).start(TARGET_HTTP_BACKEND_MOCK_PORT);

        hasStarted = true;

        return true;
    }

    private void saveNotif(String notif) {
        notificationsReceived.add(notif);
    }

    public void reset() {
        notificationsReceived.clear();
    }

}
