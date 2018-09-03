package edu.harvard.h2ms.domain.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Entity
@Table(name = "notifications")
public class Notification {

  private static final Log log = LogFactory.getLog(Notification.class);

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Long id;

  @ManyToMany(
    cascade = {CascadeType.ALL},
    fetch = FetchType.EAGER
  )
  @JoinTable(
    name = "notification_user",
    joinColumns = @JoinColumn(name = "notification_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private Set<User> users = new HashSet<>();

  @Column(name = "name")
  private String name;

  @Column(name = "report_type")
  private String reportType;

  @Column(name = "notification_title")
  private String notificationTitle;

  @Column(name = "notification_body")
  private String notificationBody;

  /**
   * Keeps track of last notification time for each user email See:
   * https://stackoverflow.com/questions/19199701/how-to-jpa-mapping-a-hashmap
   */
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "last_notification")
  @MapKeyColumn(name = "user_email_col")
  @Column(name = "last_notified_time_col")
  private Map<String, Long> emailLastNotifiedTimes;

  // dummy constructor
  public Notification() {
    super();
  }

  public Notification(
      String name, String reportType, String notificationTitle, String notificationBody) {
    this.name = name;
    this.reportType = reportType;
    this.notificationTitle = notificationTitle;
    this.notificationBody = notificationBody;
  }

  public void addUser(User user) {

    log.debug("users (before) " + this.users);
    users.add(user);
    log.debug("users (after) " + this.users);
  }

  public void removeUser(User user) {
    log.info("users (before) " + this.users);
    users.remove(user);
    log.info("users (after) " + this.users);
  }

  public Set<User> getUser() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getReportType() {
    return reportType;
  }

  public void setReportType(String reportType) {
    this.reportType = reportType;
  }

  public String getNotificationTitle() {
    return notificationTitle;
  }

  public void setNotificationTitle(String notificationTitle) {
    this.notificationTitle = notificationTitle;
  }

  public String getNotificationBody() {
    return notificationBody;
  }

  public void setNotificationBody(String notificationBody) {
    this.notificationBody = notificationBody;
  }

  public Map<String, Long> getEmailLastNotifiedTimes() {
    return this.emailLastNotifiedTimes;
  }

  public void setEmailLastNotifiedTimes(Map<String, Long> emailLastNotifiedTimes) {
    this.emailLastNotifiedTimes = emailLastNotifiedTimes;
  }

  public void setEmailLastNotifiedTime(String email, Long unixtime) {
    this.emailLastNotifiedTimes.put(email, unixtime);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder
        .append("Notification [id=")
        .append(id)
        .append(", name=")
        .append(name)
        .append(", reportType=")
        .append(reportType)
        .append(", notificationTitle=")
        .append(notificationTitle)
        .append(", notificationBody=")
        .append(notificationBody);

    return builder.toString();
  }
}
