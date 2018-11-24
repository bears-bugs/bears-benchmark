package org.activiti.cloud.app.rest;

import org.activiti.cloud.app.services.DeploymentsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class DeploymentsControllerTest {

    @InjectMocks
    private DeploymentsController deploymentsController;

    @Mock
    private DeploymentsService deploymentsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getDeploymentsByNameCallService(){
        deploymentsController.getDeploymentsByAppName("default-app");
        verify(deploymentsService).getDeploymentDescriptorByAppName("default-app");
    }

    @Test
    public void getDirectoryCallService(){
        deploymentsController.getDirectory();
        verify(deploymentsService).getDirectory();
    }

}
