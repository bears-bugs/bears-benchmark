package org.activiti.cloud.app.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.cloud.app.model.AcitivitiMetadataAttrs;
import org.activiti.cloud.app.model.Application;
import org.activiti.cloud.app.model.Service;
import org.activiti.cloud.app.model.ServiceType;
import org.activiti.cloud.app.model.Status;
import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDescriptor;
import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDirectory;
import org.activiti.cloud.app.model.deployments.ApplicationDeploymentEntry;
import org.activiti.cloud.app.model.deployments.ServiceDeploymentDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * Compares expected deployments against running services
 */
@org.springframework.stereotype.Service
public class ApplicationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationsService.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private DeploymentsService deploymentsService;

    private Map<String, Application> apps = new HashMap<>();
    private Map<String, ApplicationDeploymentDescriptor> deployments = new HashMap<>();

    public ApplicationsService() {
    }

    /*
     Steps:
     1) Fetch deployments (from config server)
     2) Clear apps map (this will be re generated)
     3) For each deployment create a new application
     4) if there are services inside the deployment create new services and add them to the application, status PENDING
     5) query service registry to check which service is up and change the status (to UP) of each application service that is found and add URI
     6) update the status of the application if all the deployed services are actually deployed
     */
    public void refresh() {

        List<ApplicationDeploymentDescriptor> applicationDeployments = fetchApplicationDeployments();
        for (ApplicationDeploymentDescriptor aad : applicationDeployments) {
            deployments.put(aad.getApplicationName(),
                            aad);
        }

        apps.clear();

        for (ApplicationDeploymentDescriptor aad : deployments.values()) {
            Application activitiApplication = apps.get(aad.getApplicationName());
            if (activitiApplication == null) {
                activitiApplication = createActivitiApplication(aad.getApplicationName());
            }
            if (activitiApplication != null) {
                List<Service> services = createApplicationServicesBasedOnDeployment(aad);
                for (Service as : services) {
                    activitiApplication.addService(as);
                }
            }
        }
        for (Application aa : apps.values()) {
            resetAppServiceStatus(aa);
        }

        // Check in the Service Registry for Activiti Cloud Building Blocks
        List<String> services = discoveryClient.getServices();
        for (String s : services) {

            List<ServiceInstance> instances = discoveryClient.getInstances(s);
            for (ServiceInstance serviceInstance : instances) {

                Map<String, String> metadata = serviceInstance.getMetadata();
                if(metadata!=null) {
                    String applicationName = metadata.get(AcitivitiMetadataAttrs.appName);
                    if (applicationName != null) {
                        final String serviceType = metadata.get(AcitivitiMetadataAttrs.serviceType);
                        LOGGER.debug("Application Name: " + applicationName);
                        LOGGER.debug("Service Type: " + serviceType);

                        Application activitiApplication = apps.get(applicationName);
                        // If the ServiceInstance belong to an Activiti cloud Application, update the status inside the App
                        if (activitiApplication != null) {

                            updateServiceStatusAndURL(activitiApplication,
                                    serviceInstance,
                                    serviceType);
                        }
                    }
                }
            }
        }

        // Check to validate if all the services inside each app are UP and set the overall App status to UP, otherwise PENDING
        updateApplicationsStatus();
    }

    public List<Application> getApplications() {
        return new ArrayList<>(apps.values());
    }

    public Application getApplicationByName(String appName) {
        return apps.get(appName);
    }

    public Status getApplicationStatus(String appName) {
        return apps.get(appName).getStatus();
    }

    public List<Service> getApplicationServices(String appName) {
        return apps.get(appName).getServices();
    }

    public List<ApplicationDeploymentDescriptor> getApplicationDeployments() {
        return new ArrayList<>(deployments.values());
    }

    private void updateApplicationsStatus() {
        for (String appName : apps.keySet()) {
            LOGGER.debug(">>> Looking at app: " + appName);
            boolean appIsUp = true;
            for (Service as : apps.get(appName).getServices()) {
                LOGGER.debug("\t>>> Service: " + as.getName() + " with status: " + as.getStatus());
                if (!as.getStatus().equals(Status.UP)) {
                    appIsUp = false;
                }
            }
            if (appIsUp) {
                apps.get(appName).setStatus(Status.UP);
            } else {
                apps.get(appName).setStatus(Status.PENDING);
            }
        }
    }

    private void updateServiceStatusAndURL(Application activitiApplication,
                                           ServiceInstance i,
                                           String serviceType) {
        if (ServiceType.AUDIT.toString().equals(serviceType)) {
            Service activitiAuditService = findServiceByName(activitiApplication,
                                                             i.getServiceId());
            if (activitiAuditService != null) {
                if (activitiAuditService.getServiceType().equals(ServiceType.AUDIT)) {
                    activitiAuditService.setUrl(i.getUri().toString());
                    activitiAuditService.setStatus(Status.UP);
                }
            }
        } else if (ServiceType.RUNTIME_BUNDLE.toString().equals(serviceType)) {
            Service activitiRuntimeBundleService = findServiceByName(activitiApplication,
                                                                     i.getServiceId());
            if (activitiRuntimeBundleService != null) {
                if (activitiRuntimeBundleService.getServiceType().equals(ServiceType.RUNTIME_BUNDLE)) {
                    activitiRuntimeBundleService.setUrl(i.getUri().toString());
                    activitiRuntimeBundleService.setStatus(Status.UP);
                }
            }
        } else if (ServiceType.QUERY.toString().equals(serviceType)) {
            Service activitiQueryService = findServiceByName(activitiApplication,
                                                             i.getServiceId());
            if (activitiQueryService != null) {
                if (activitiQueryService.getServiceType().equals(ServiceType.QUERY)) {
                    activitiQueryService.setUrl(i.getUri().toString());
                    activitiQueryService.setStatus(Status.UP);
                }
            }
        } else if (ServiceType.CONNECTOR.toString().equals(serviceType)) {
            Service activitiConnectorService = findServiceByName(activitiApplication,
                                                                 i.getServiceId());
            if (activitiConnectorService != null) {
                if (activitiConnectorService.getServiceType().equals(ServiceType.CONNECTOR)) {
                    activitiConnectorService.setUrl(i.getUri().toString());
                    activitiConnectorService.setStatus(Status.UP);
                }
            }
        }
    }

    private Application createActivitiApplication(String name) {
        Application newActivitiApplication = new Application(name);
        newActivitiApplication.setStatus(Status.PENDING);
        apps.put(name,
                 newActivitiApplication);
        return newActivitiApplication;
    }

    private List<Service> createApplicationServicesBasedOnDeployment(ApplicationDeploymentDescriptor aad) {
        List<Service> services = new ArrayList<>();
        for (ServiceDeploymentDescriptor asd : aad.getServiceDeploymentDescriptors()) {
            if (ServiceType.AUDIT.equals(asd.getServiceType())) {
                Service activitiAuditService = new Service();
                activitiAuditService.setServiceType(ServiceType.AUDIT);
                activitiAuditService.setName(asd.getName());
                activitiAuditService.setStatus(Status.PENDING);
                services.add(activitiAuditService);
            } else if (ServiceType.RUNTIME_BUNDLE.equals(asd.getServiceType())) {
                Service activitiRuntimeBundleService = new Service();
                activitiRuntimeBundleService.setServiceType(ServiceType.RUNTIME_BUNDLE);
                activitiRuntimeBundleService.setName(asd.getName());
                activitiRuntimeBundleService.setStatus(Status.PENDING);
                services.add(activitiRuntimeBundleService);
            } else if (ServiceType.QUERY.equals(asd.getServiceType())) {
                Service activitiQueryService = new Service();
                activitiQueryService.setServiceType(ServiceType.QUERY);
                activitiQueryService.setName(asd.getName());
                activitiQueryService.setStatus(Status.PENDING);
                services.add(activitiQueryService);
            } else if (ServiceType.CONNECTOR.equals(asd.getServiceType())) {
                Service activitiConnectorService = new Service();
                activitiConnectorService.setServiceType(ServiceType.CONNECTOR);
                activitiConnectorService.setName(asd.getName());
                activitiConnectorService.setStatus(Status.PENDING);
                services.add(activitiConnectorService);
            }
        }
        return services;
    }

    /*
     * Fetch Application Deployments
     * 1) Get Entries from the Applications to be Deployed Directory
     * 2) For Each Entry fetch Deployment Descriptor
     */
    private List<ApplicationDeploymentDescriptor> fetchApplicationDeployments() {
        ApplicationDeploymentDirectory directory = deploymentsService.getDirectory();
        List<ApplicationDeploymentDescriptor> applicationDeployments = new ArrayList<>();
        if (directory != null) {

            for (ApplicationDeploymentEntry ade : directory.getAppDeploymentEntries()) {

                ApplicationDeploymentDescriptor deploymentDesc = deploymentsService.getDeploymentDescriptorByAppName(ade.getName());

                applicationDeployments.add(deploymentDesc);
            }
            return applicationDeployments;
        }
        return null;
    }

    private void resetAppServiceStatus(Application activitiApplication) {
        for (Service as : activitiApplication.getServices()) {
            as.setStatus(Status.PENDING);
        }
    }

    private Service findServiceByName(Application activitiApplication,
                                      String serviceId) {
        for (Service as : activitiApplication.getServices()) {
            if (as.getName().toLowerCase().equals(serviceId.toLowerCase())) {
                return as;
            }
        }
        return null;
    }
}
