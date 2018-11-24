package org.activiti.cloud.app.services;

import org.activiti.cloud.app.model.AcitivitiMetadataAttrs;
import org.activiti.cloud.app.model.ServiceType;
import org.activiti.cloud.app.model.Status;
import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDescriptor;
import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDirectory;
import org.activiti.cloud.app.model.deployments.ApplicationDeploymentEntry;
import org.activiti.cloud.app.model.deployments.ServiceDeploymentDescriptor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationsServiceTest {

    @InjectMocks
    private ApplicationsService applicationsService;

    @Mock
    private DiscoveryClient discoveryClient;

    @Mock
    private DeploymentsService deploymentsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void appShouldBeUpWhenServicesUp(){

        when(discoveryClient.getServices()).thenReturn(Collections.singletonList("rb-my-app"));

        List<ServiceInstance> instances = new ArrayList<>();
        ServiceInstance serviceInstance = mock(ServiceInstance.class);
        Map<String, String> metadata = new HashMap<>();
        metadata.put(AcitivitiMetadataAttrs.appName,"default-app");
        metadata.put(AcitivitiMetadataAttrs.serviceType, ServiceType.RUNTIME_BUNDLE.toString());
        when(serviceInstance.getMetadata()).thenReturn(metadata);
        when(serviceInstance.getServiceId()).thenReturn("rb-my-app");
        when(serviceInstance.getUri()).thenReturn(URI.create("dummyuri"));
        instances.add(serviceInstance);

        when(discoveryClient.getInstances("rb-my-app")).thenReturn(instances);

        List<ApplicationDeploymentEntry> applicationDeployments = new ArrayList<>();
        ApplicationDeploymentEntry appDescriptor = new ApplicationDeploymentEntry("default-app","0.1");
        applicationDeployments.add(appDescriptor);
        ApplicationDeploymentDirectory directory = new ApplicationDeploymentDirectory(applicationDeployments);

        when(deploymentsService.getDirectory()).thenReturn(directory);

        List<ServiceDeploymentDescriptor> serviceDeploymentDescriptors = new ArrayList<>();
        ServiceDeploymentDescriptor serviceDeploymentDescriptor = new ServiceDeploymentDescriptor("rb-my-app","0.1",ServiceType.RUNTIME_BUNDLE);
        serviceDeploymentDescriptors.add(serviceDeploymentDescriptor);
        ApplicationDeploymentDescriptor appDepDescriptor = new ApplicationDeploymentDescriptor("1","default-app","0.1",serviceDeploymentDescriptors);

        when(deploymentsService.getDeploymentDescriptorByAppName("default-app")).thenReturn(appDepDescriptor);

        applicationsService.refresh();
        assertThat(applicationsService.getApplicationStatus("default-app")).isEqualTo(Status.UP);

    }
}
