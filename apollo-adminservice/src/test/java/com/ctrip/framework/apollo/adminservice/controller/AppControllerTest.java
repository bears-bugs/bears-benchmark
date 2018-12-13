package com.ctrip.framework.apollo.adminservice.controller;

import com.ctrip.framework.apollo.biz.repository.AppRepository;
import com.ctrip.framework.apollo.common.dto.AppDTO;
import com.ctrip.framework.apollo.common.entity.App;
import com.ctrip.framework.apollo.common.utils.BeanUtils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.web.client.HttpClientErrorException;

public class AppControllerTest extends AbstractControllerTest {

  @Autowired
  AppRepository appRepository;

  private String getBaseAppUrl() {
    return "http://localhost:" + port + "/apps/";
  }

  @Test
  @Sql(scripts = "/controller/cleanup.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testCheckIfAppIdUnique() {
    AppDTO dto = generateSampleDTOData();
    ResponseEntity<AppDTO> response =
        restTemplate.postForEntity(getBaseAppUrl(), dto, AppDTO.class);
    AppDTO result = response.getBody();
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals(dto.getAppId(), result.getAppId());
    Assert.assertTrue(result.getId() > 0);

    Boolean falseUnique =
        restTemplate.getForObject(getBaseAppUrl() + dto.getAppId() + "/unique", Boolean.class);
    Assert.assertFalse(falseUnique);
    Boolean trueUnique = restTemplate
        .getForObject(getBaseAppUrl() + dto.getAppId() + "true" + "/unique", Boolean.class);
    Assert.assertTrue(trueUnique);
  }

  @Test
  @Sql(scripts = "/controller/cleanup.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testCreate() {
    AppDTO dto = generateSampleDTOData();
    ResponseEntity<AppDTO> response =
        restTemplate.postForEntity(getBaseAppUrl(), dto, AppDTO.class);
    AppDTO result = response.getBody();
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals(dto.getAppId(), result.getAppId());
    Assert.assertTrue(result.getId() > 0);

    App savedApp = appRepository.findById(result.getId()).orElse(null);
    Assert.assertEquals(dto.getAppId(), savedApp.getAppId());
    Assert.assertNotNull(savedApp.getDataChangeCreatedTime());
  }

  @Test
  @Sql(scripts = "/controller/cleanup.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testCreateTwice() {
    AppDTO dto = generateSampleDTOData();
    ResponseEntity<AppDTO> response =
        restTemplate.postForEntity(getBaseAppUrl(), dto, AppDTO.class);
    AppDTO first = response.getBody();
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals(dto.getAppId(), first.getAppId());
    Assert.assertTrue(first.getId() > 0);

    App savedApp = appRepository.findById(first.getId()).orElse(null);
    Assert.assertEquals(dto.getAppId(), savedApp.getAppId());
    Assert.assertNotNull(savedApp.getDataChangeCreatedTime());

    try {
      restTemplate.postForEntity(getBaseAppUrl(), dto, AppDTO.class);
    }catch (HttpClientErrorException e){
      Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }

  }

  @Test
  @Sql(scripts = "/controller/cleanup.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testFind() {
    AppDTO dto = generateSampleDTOData();
    App app = BeanUtils.transfrom(App.class, dto);
    app = appRepository.save(app);

    AppDTO result = restTemplate.getForObject(getBaseAppUrl() + dto.getAppId(), AppDTO.class);
    Assert.assertEquals(dto.getAppId(), result.getAppId());
    Assert.assertEquals(dto.getName(), result.getName());
  }

  @Test(expected = HttpClientErrorException.class)
  @Sql(scripts = "/controller/cleanup.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testFindNotExist() {
    restTemplate.getForEntity(getBaseAppUrl() + "notExists", AppDTO.class);
  }

  @Test
  @Sql(scripts = "/controller/cleanup.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testDelete() {
    AppDTO dto = generateSampleDTOData();
    App app = BeanUtils.transfrom(App.class, dto);
    app = appRepository.save(app);

    restTemplate.delete("http://localhost:{port}/apps/{appId}?operator={operator}", port, app.getAppId(), "test");

    App deletedApp = appRepository.findById(app.getId()).orElse(null);
    Assert.assertNull(deletedApp);
  }

  private AppDTO generateSampleDTOData() {
    AppDTO dto = new AppDTO();
    dto.setAppId("someAppId");
    dto.setName("someName");
    dto.setOwnerName("someOwner");
    dto.setOwnerEmail("someOwner@ctrip.com");
    dto.setDataChangeCreatedBy("apollo");
    dto.setDataChangeLastModifiedBy("apollo");
    return dto;
  }
}
