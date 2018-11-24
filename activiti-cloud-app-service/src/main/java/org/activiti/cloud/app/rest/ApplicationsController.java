package org.activiti.cloud.app.rest;

import java.util.List;

import org.activiti.cloud.app.model.Application;
import org.activiti.cloud.app.model.Service;
import org.activiti.cloud.app.model.Status;
import org.activiti.cloud.app.services.ApplicationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Report status of apps
 */
@RequestMapping(value = "/v1/apps")
@RestController
public class ApplicationsController {

    @Autowired
    private ApplicationsService applicationsService;

    @RequestMapping(method = RequestMethod.POST, path = "/refresh")
    public void refresh() {
        applicationsService.refresh();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public List<Application> getApplications() {
        return applicationsService.getApplications();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{appName}")
    public Application getApplicationById(@PathVariable("appName") String appName) {
        return applicationsService.getApplicationByName(appName);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{appName}/status")
    public Status getApplicationStatus(@PathVariable("appName") String appName) {
        return applicationsService.getApplicationStatus(appName);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{appId}/services")
    public List<Service> getApplicationServices(@PathVariable("appName") String appName) {
        return applicationsService.getApplicationServices(appName);
    }
}
