package org.activiti.cloud.app.services;

import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDescriptor;
import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves deployments from config server
 */
@Service
public class DeploymentsService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${activiti.cloud.config.url}")
    private String configServerURL;
    @Value("${activiti.cloud.config.apps.namespace:/activiti-cloud-apps/dev/master/}")
    private String activitiCloudAppsNamespace;
    @Value("${activiti.cloud.config.apps.entrypoint:apps}")
    private String appsEntryPoint;
    @Value("${activiti.cloud.config.apps.descriptor.extension:.json}")
    private String descriptorExtension;

    public ApplicationDeploymentDescriptor getDeploymentDescriptorByAppName(String app) {

        ResponseEntity<ApplicationDeploymentDescriptor> appDeploymentDesc = restTemplate.exchange(configServerURL +
                activitiCloudAppsNamespace +
                app +
                descriptorExtension, HttpMethod.GET,getHeaders(),ApplicationDeploymentDescriptor.class);
        return appDeploymentDesc.getBody();
    }

    public ApplicationDeploymentDirectory getDirectory() {

        ResponseEntity<ApplicationDeploymentDirectory> appDeploymentDirectory = restTemplate.exchange(configServerURL +
                activitiCloudAppsNamespace +
                appsEntryPoint +
                descriptorExtension, HttpMethod.GET,getHeaders(),ApplicationDeploymentDirectory.class);


        return appDeploymentDirectory.getBody();
    }

    private HttpEntity<?> getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptableMediaTypes);
        HttpEntity<?> entity = new HttpEntity<Object>(headers);
        return entity;
    }
}
