package com.ctrip.framework.apollo.openapi.entity;

import com.ctrip.framework.apollo.common.entity.BaseEntity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Consumer")
@SQLDelete(sql = "Update Consumer set isDeleted = 1 where id = ?")
@Where(clause = "isDeleted = 0")
public class Consumer extends BaseEntity {

  @Column(name = "Name", nullable = false)
  private String name;

  @Column(name = "AppId", nullable = false)
  private String appId;

  @Column(name = "OrgId", nullable = false)
  private String orgId;

  @Column(name = "OrgName", nullable = false)
  private String orgName;

  @Column(name = "OwnerName", nullable = false)
  private String ownerName;

  @Column(name = "OwnerEmail", nullable = false)
  private String ownerEmail;

  public String getAppId() {
    return appId;
  }

  public String getName() {
    return name;
  }

  public String getOrgId() {
    return orgId;
  }

  public String getOrgName() {
    return orgName;
  }

  public String getOwnerEmail() {
    return ownerEmail;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public void setOwnerEmail(String ownerEmail) {
    this.ownerEmail = ownerEmail;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  @Override
  public String toString() {
    return toStringHelper().add("name", name).add("appId", appId)
        .add("orgId", orgId)
        .add("orgName", orgName)
        .add("ownerName", ownerName)
        .add("ownerEmail", ownerEmail).toString();
  }
}
