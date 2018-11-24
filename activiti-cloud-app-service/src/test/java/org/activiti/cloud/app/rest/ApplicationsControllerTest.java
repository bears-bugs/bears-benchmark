package org.activiti.cloud.app.rest;

import org.activiti.cloud.app.services.ApplicationsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class ApplicationsControllerTest {

    @InjectMocks
    private ApplicationsController applicationsController;

    @Mock
    private ApplicationsService applicationsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void refreshShouldCallService(){
        applicationsController.refresh();
        verify(applicationsService).refresh();
    }

    @Test
    public void getApplicationsShouldCallService(){
        applicationsController.getApplications();
        verify(applicationsService).getApplications();
    }

    @Test
    public void getApplicationByIdShouldCallService(){
        applicationsController.getApplicationById("default-app");
        verify(applicationsService).getApplicationByName("default-app");
    }

    @Test
    public void getApplicationStatusShouldCallService(){
        applicationsController.getApplicationStatus("default-app");
        verify(applicationsService).getApplicationStatus("default-app");
    }

    @Test
    public void getApplicationServicesShouldCallService(){
        applicationsController.getApplicationServices("default-app");
        verify(applicationsService).getApplicationServices("default-app");
    }
}
