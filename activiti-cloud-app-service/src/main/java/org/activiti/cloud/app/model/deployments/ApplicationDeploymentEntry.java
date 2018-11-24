package org.activiti.cloud.app.model.deployments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationDeploymentEntry {

    private String name;
    private String version;

    public ApplicationDeploymentEntry() {
    }

    public ApplicationDeploymentEntry(String name,
                                      String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "ApplicationDeploymentEntry{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
