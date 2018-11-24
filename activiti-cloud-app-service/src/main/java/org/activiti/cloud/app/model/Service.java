package org.activiti.cloud.app.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {



    private String id;
    private ServiceType serviceType;
    private String name;
    private String version;
    private String url;
    private String artifactName;
    private Status status;

    public Service() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public String getName() {
        return name;
    }

    public String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    public String getVersion() {
        return version;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
