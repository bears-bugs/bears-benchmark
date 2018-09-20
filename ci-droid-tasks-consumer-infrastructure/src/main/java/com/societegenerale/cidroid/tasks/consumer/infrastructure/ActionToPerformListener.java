package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.societegenerale.cidroid.api.actionToReplicate.ActionToReplicate;
import com.societegenerale.cidroid.tasks.consumer.services.ActionToPerformService;
import com.societegenerale.cidroid.tasks.consumer.services.model.BulkActionToPerform;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.stream.annotation.StreamListener;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ActionToPerformListener {

    private ActionToPerformService actionToPerformService;

    private List<ActionToReplicate> actionsToReplicate;

    private Map<String, Class<? extends ActionToReplicate>> registeredActionsToReplicate;

    public ActionToPerformListener(ActionToPerformService actionToPerformService, List<ActionToReplicate> actionsToReplicate) {
        this.actionToPerformService = actionToPerformService;
        this.actionsToReplicate = actionsToReplicate;
    }

    @PostConstruct
    protected void registerActionsToReplicate(){

        //since we use class name as key, it's necessarily unique -> there can't be duplicates

        registeredActionsToReplicate = actionsToReplicate.stream().collect(Collectors.toMap(action -> action.getClass().getName(), ActionToReplicate::getClass));

    }


    @StreamListener("actions-to-perform")
    public void onActionToPerform(ActionToPerformCommand actionToPerformCommand) {

        log.info("received an action to perform {}", actionToPerformCommand);

        try {

            Map<String, String> updateActionInfos = (Map) actionToPerformCommand.getUpdateAction();

            String actionToReplicateClass = updateActionInfos.get("@class").trim();

            Class<? extends ActionToReplicate> actionToReplicateToInstantiate = findActualActionToPerform(actionToReplicateClass);

            if (actionToReplicateToInstantiate == null) {
                throw new UnknownActionTypeException(
                        "unknown action type " + actionToReplicateClass + ": please check it has been registered correctly");
            }

            ActionToReplicate actionToReplicate = actionToReplicateToInstantiate.newInstance();
            actionToReplicate.init(updateActionInfos);

            BulkActionToPerform actionToPerform = BulkActionToPerform.builder()
                    .gitLogin(actionToPerformCommand.getGitLogin())
                    .gitHubOauthToken(actionToPerformCommand.getGitHubOauthToken())
                    .email(actionToPerformCommand.getEmail())
                    .commitMessage(actionToPerformCommand.getCommitMessage())
                    .gitHubInteraction(actionToPerformCommand.getGitHubInteractionType())
                    .resourcesToUpdate(actionToPerformCommand.getResourcesToUpdate())
                    .actionToReplicate(actionToReplicate)
                    .build();

            actionToPerformService.perform(actionToPerform);

        } catch (UnknownActionTypeException e) {
            log.error("can't map the received command to a known action type", e);
        } catch (Exception e) {
            log.warn("some unexpected error happened", e);
        }

    }

    private Class<? extends ActionToReplicate> findActualActionToPerform(String actionToReplicateClass) {

        for( String registeredAction : registeredActionsToReplicate.keySet()){

            if(StringUtils.containsIgnoreCase(actionToReplicateClass,registeredAction)){
                return registeredActionsToReplicate.get(registeredAction);
            }

        }

        return null;

    }

}
