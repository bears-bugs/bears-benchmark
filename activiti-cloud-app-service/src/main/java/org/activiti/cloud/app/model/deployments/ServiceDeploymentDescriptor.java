package org.activiti.cloud.app.model.deployments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.activiti.cloud.app.model.ServiceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDeploymentDescriptor {

    private String name;
    private String version;
    private ServiceType serviceType;

    public ServiceDeploymentDescriptor() {
    }

    public ServiceDeploymentDescriptor(String name,
                                       String version,
                                       ServiceType serviceType) {
        this.name = name;
        this.version = version;
        this.serviceType = serviceType;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
}
