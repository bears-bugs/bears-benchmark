package edu.harvard.h2ms.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.service.ManagementDashboardService;

import java.sql.Time;

/**
 * Event Rest Controller
 * URL's start with /demo (after Application path)
 */
@RestController
@RequestMapping(path="/managementDashboard")
public class ManagementDashboardController {

    final Logger logger = LoggerFactory.getLogger(ManagementDashboardController.class);

    private ManagementDashboardService managementDashboardService;

    @Autowired
    public void setManagementDashboardService(ManagementDashboardService managementDashboardService) {
        this.managementDashboardService = managementDashboardService;
    }

}
