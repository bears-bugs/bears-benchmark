package com.societegenerale.cidroid.tasks.consumer.services;

public class MonitoringEvents {

    private MonitoringEvents(){

    }

    public final static String PUSH_EVENT_TO_PROCESS="pushEventToProcess";

    public final static String PULL_REQUEST_EVENT_TO_PROCESS="pullRequestEventToProcess";

    public final static String PR_REBASED="pullRequestRebasedSuccessFully";

    public final static String PR_NOT_REBASED="pullRequestNotRebasedAsExpected";

    public final static String NOTIFICATION_FOR_NON_MERGEABLE_PR="notificationForPrWithConflicts";


}
