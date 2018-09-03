package edu.harvard.h2ms.service;

import edu.harvard.h2ms.domain.admin.Email;
import edu.harvard.h2ms.domain.core.Event;

/**
 * The ManagementDashboardService...
 */
public interface ManagementDashboardService {

    // Sends Email Notifications
    void sendEmail(Email email);

}
