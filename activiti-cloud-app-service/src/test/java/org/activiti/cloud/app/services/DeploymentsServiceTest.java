package org.activiti.cloud.app.services;

import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDescriptor;
import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDirectory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class DeploymentsServiceTest {

    @InjectMocks
    private DeploymentsService deploymentsService;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGetAppDescriptorFromConfigServer(){
        ApplicationDeploymentDescriptor descriptor = new ApplicationDeploymentDescriptor();
        ResponseEntity<ApplicationDeploymentDescriptor> appDeploymentDescriptor = new ResponseEntity(descriptor,HttpStatus.OK);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class),any(HttpEntity.class),(Class) any())).thenReturn(appDeploymentDescriptor);

        assertThat(deploymentsService.getDeploymentDescriptorByAppName("default-app")).isEqualTo(descriptor);
    }

    @Test
    public void shouldGetAppsListFromConfigServer(){
        ApplicationDeploymentDirectory listing = new ApplicationDeploymentDirectory();
        ResponseEntity<ApplicationDeploymentDescriptor> appDeploymentDescriptor = new ResponseEntity(listing,HttpStatus.OK);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class),any(HttpEntity.class),(Class) any())).thenReturn(appDeploymentDescriptor);

        assertThat(deploymentsService.getDirectory()).isEqualTo(listing);
    }

}
