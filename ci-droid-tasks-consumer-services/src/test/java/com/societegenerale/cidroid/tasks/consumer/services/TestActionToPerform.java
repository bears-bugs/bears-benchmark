package com.societegenerale.cidroid.tasks.consumer.services;

import com.societegenerale.cidroid.api.IssueProvidingContentException;
import com.societegenerale.cidroid.api.ResourceToUpdate;
import com.societegenerale.cidroid.api.actionToReplicate.ActionToReplicate;
import lombok.Setter;

import java.util.Map;

public class TestActionToPerform implements ActionToReplicate {

    @Setter
    private String contentToProvide;

    @Setter
    private boolean continueIfResourceDoesntExist;

    @Setter
    private boolean throwIssueProvidingContentException = false;

    @Override
    public String provideContent(String initialContent, ResourceToUpdate resourceToUpdate) throws IssueProvidingContentException {

        if (throwIssueProvidingContentException) {
            throw new IssueProvidingContentException("throwing exception as configured...");
        }

        return contentToProvide;
    }

    @Override
    public void init(Map<String, String> updateActionInfos) {
        //do nothing
    }

    @Override
    public boolean canContinueIfResourceDoesntExist() {
        return continueIfResourceDoesntExist;
    }

}
