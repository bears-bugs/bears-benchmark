package edu.harvard.h2ms.service;

import edu.harvard.h2ms.domain.admin.Email;

/** The ManagementDashboardService... */
public interface ManagementDashboardService {

  // Sends Email Notifications
  void sendEmail(Email email);
}
