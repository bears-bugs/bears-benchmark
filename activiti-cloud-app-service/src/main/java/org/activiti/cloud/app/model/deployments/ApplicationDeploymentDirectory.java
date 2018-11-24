package org.activiti.cloud.app.model.deployments;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationDeploymentDirectory {

    private List<ApplicationDeploymentEntry> appDeploymentEntries;

    public ApplicationDeploymentDirectory() {
    }

    public ApplicationDeploymentDirectory(List<ApplicationDeploymentEntry> appDeploymentEntries) {
        this.appDeploymentEntries = appDeploymentEntries;
    }

    public List<ApplicationDeploymentEntry> getAppDeploymentEntries() {
        return appDeploymentEntries;
    }

    @Override
    public String toString() {
        return "ApplicationDeploymentDirectory{" +
                "appDeploymentEntries=" + appDeploymentEntries +
                '}';
    }
}
