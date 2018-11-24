package org.activiti.cloud.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {

    private String id;
    private String name;
    private String version;
    private String realm;
    private List<Service> services = new ArrayList<>();
    private Status status;
    private ProjectRelease projectRelease;


    public Application() {
        this.id = UUID.randomUUID().toString();
    }

    public Application(String name) {
        this();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getRealm() {
        return realm;
    }

    public List<Service> getServices() {
        return services;
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addService(Service service) {
        this.services.add(service);
    }

    public ProjectRelease getProjectRelease() {
        return projectRelease;
    }

    public void setProjectRelease(ProjectRelease projectRelease) {
        this.projectRelease = projectRelease;
    }
}
