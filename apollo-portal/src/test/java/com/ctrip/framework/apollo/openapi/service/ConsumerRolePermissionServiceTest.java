package com.ctrip.framework.apollo.openapi.service;

import com.ctrip.framework.apollo.portal.AbstractIntegrationTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class ConsumerRolePermissionServiceTest extends AbstractIntegrationTest {
  @Autowired
  private ConsumerRolePermissionService consumerRolePermissionService;

  @Before
  public void setUp() throws Exception {

  }

  @Test
  @Sql(scripts = "/sql/permission/insert-test-roles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/sql/permission/insert-test-permissions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/sql/permission/insert-test-consumerroles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/sql/permission/insert-test-rolepermissions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testConsumerHasPermission() throws Exception {
    String someTargetId = "someTargetId";
    String anotherTargetId = "anotherTargetId";
    String somePermissionType = "somePermissionType";
    String anotherPermissionType = "anotherPermissionType";
    long someConsumerId = 1;
    long anotherConsumerId = 2;
    long someConsumerWithNoPermission = 3;

    assertTrue(consumerRolePermissionService.consumerHasPermission(someConsumerId, somePermissionType, someTargetId));
    assertTrue(consumerRolePermissionService.consumerHasPermission(someConsumerId, anotherPermissionType, anotherTargetId));
    assertTrue(consumerRolePermissionService.consumerHasPermission(anotherConsumerId, somePermissionType, someTargetId));
    assertTrue(consumerRolePermissionService.consumerHasPermission(anotherConsumerId, anotherPermissionType, anotherTargetId));

    assertFalse(consumerRolePermissionService.consumerHasPermission(someConsumerWithNoPermission, somePermissionType, someTargetId));
    assertFalse(consumerRolePermissionService.consumerHasPermission(someConsumerWithNoPermission, anotherPermissionType, anotherTargetId));

  }

}