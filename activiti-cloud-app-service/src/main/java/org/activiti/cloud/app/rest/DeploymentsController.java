package org.activiti.cloud.app.rest;

import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDescriptor;
import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDirectory;
import org.activiti.cloud.app.services.DeploymentsService;
import org.activiti.cloud.app.services.ApplicationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Reports expected apps as per the 'deployment descriptor'
 */
@RequestMapping(value = "/v1/deployments")
@RestController
public class DeploymentsController {

    @Autowired
    private ApplicationsService applicationsService;

    @Autowired
    private DeploymentsService deploymentsService;


    @RequestMapping(method = RequestMethod.GET, path = "/{appName}")
    public ApplicationDeploymentDescriptor getDeploymentsByAppName(@PathVariable("appName") String appName) {
        return deploymentsService.getDeploymentDescriptorByAppName(appName);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/directory")
    public ApplicationDeploymentDirectory getDirectory() {
        return deploymentsService.getDirectory();
    }
}
