package com.societegenerale.cidroid.tasks.consumer.services.model.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.societegenerale.cidroid.tasks.consumer.services.model.github.UpdatedResource.UpdateStatus.UPDATE_KO_NO_REASON;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedResource {

    private Content content;

    private Commit commit;

    @JsonIgnore
    @Builder.Default
    private UpdateStatus updateStatus = UPDATE_KO_NO_REASON;

    public static UpdatedResource notUpdatedResource(UpdateStatus updateStatus, String htmlUrl) {

        Content contentThatWasntUpdated = new Content();
        contentThatWasntUpdated.setHtmlUrl(htmlUrl);

        UpdatedResource notUpdatedResource = UpdatedResource.builder()
                .updateStatus(updateStatus)
                .content(contentThatWasntUpdated)
                .build();

        return notUpdatedResource;
    }

    public static UpdatedResource notUpdatedResource(UpdateStatus updateStatus) {

        return notUpdatedResource(updateStatus,null);
    }

    public boolean hasBeenUpdated() {
        return updateStatus.hasBeenUpdated;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Content {

        private String name;

        private String path;

        @JsonProperty("html_url")
        private String htmlUrl;

    }

    public enum UpdateStatus {

        UPDATE_OK(true),
        UPDATE_OK_WITH_PR_CREATED(true),
        UPDATE_OK_BUT_PR_CREATION_KO(true),

        UPDATE_KO_FILE_DOESNT_EXIST(false),
        UPDATE_KO_FILE_CONTENT_IS_SAME(false),
        UPDATE_KO_BRANCH_CREATION_ISSUE(false),
        UPDATE_KO_CANT_PROVIDE_CONTENT_ISSUE(false),
        UPDATE_KO_NO_REASON(false),
        UPDATE_KO_AUTHENTICATION_ISSUE(false);

        private final boolean hasBeenUpdated;

        UpdateStatus(boolean hasBeenUpdated) {
            this.hasBeenUpdated = hasBeenUpdated;

        }
    }
}


