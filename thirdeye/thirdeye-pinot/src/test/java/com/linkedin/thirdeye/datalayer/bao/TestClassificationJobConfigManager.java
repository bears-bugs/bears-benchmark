package com.linkedin.thirdeye.datalayer.bao;

import com.linkedin.thirdeye.datalayer.DaoTestUtils;
import com.linkedin.thirdeye.datalayer.dto.ClassificationConfigDTO;
import com.linkedin.thirdeye.datasource.DAORegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestClassificationJobConfigManager {
  private static final List<Long> MAIN_FUNCTION_ID = Collections.singletonList(101L);

  private static final String NAME1 = "test classification name1";
  private static final List<Long> MAIN_FUNCTION_ID1 = MAIN_FUNCTION_ID;
  private static List<Long> AUX_FUNCTION_IDS1 = new ArrayList<Long>() {{
    addAll(MAIN_FUNCTION_ID1);
    add(102L);
    add(103L);
  }};

  private static final String NAME2 = "test classification name2";
  private static final List<Long> MAIN_FUNCTION_ID2 = MAIN_FUNCTION_ID;
  private static List<Long> AUX_FUNCTION_IDS2 = new ArrayList<Long>() {{
    addAll(MAIN_FUNCTION_ID2);
    add(103L);
    add(204L);
  }};

  private Long configId1;
  private Long configId2;

  private DAOTestBase testDAOProvider;
  private ClassificationConfigManager classificationConfigDAO;
  @BeforeClass
  void beforeClass() {
    testDAOProvider = DAOTestBase.getInstance();
    DAORegistry daoRegistry = DAORegistry.getInstance();
    classificationConfigDAO = daoRegistry.getClassificationConfigDAO();
  }

  @AfterClass(alwaysRun = true)
  void afterClass() {
    testDAOProvider.cleanup();
  }

  @Test
  public void testCreateConfig() {
    ClassificationConfigDTO classificationConfig1 = DaoTestUtils.getTestClassificationConfig(NAME1, MAIN_FUNCTION_ID1,
        AUX_FUNCTION_IDS1);
    configId1 = classificationConfigDAO.save(classificationConfig1);
    Assert.assertTrue(configId1 > 0);

    ClassificationConfigDTO classificationConfig2 = DaoTestUtils.getTestClassificationConfig(NAME2, MAIN_FUNCTION_ID2,
        AUX_FUNCTION_IDS2);
    classificationConfig2.setActive(false);
    configId2 = classificationConfigDAO.save(classificationConfig2);
    Assert.assertTrue(configId2 > 0);
  }

  @Test (dependsOnMethods = {"testCreateConfig"})
  public void testFetchConfig() {
    ClassificationConfigDTO response = classificationConfigDAO.findById(configId1);
    Assert.assertNotNull(response);
    Assert.assertEquals(response.getId(), configId1);
    Assert.assertEquals(response.getName(), NAME1);

    response = classificationConfigDAO.findByName(NAME1);
    Assert.assertNotNull(response);
    Assert.assertEquals(response.getId(), configId1);
    Assert.assertEquals(response.getName(), NAME1);
    Assert.assertEquals(response.isActive(), true);
    Assert.assertEquals(response.getAuxFunctionIdList(), AUX_FUNCTION_IDS1);

    response = classificationConfigDAO.findByName(NAME2);
    Assert.assertNotNull(response);
    Assert.assertEquals(response.getId(), configId2);
    Assert.assertEquals(response.getName(), NAME2);
    Assert.assertEquals(response.isActive(), false);
    Assert.assertEquals(response.getAuxFunctionIdList(), AUX_FUNCTION_IDS2);
  }

  @Test (dependsOnMethods = {"testFetchConfig"})
  public void testDeleteConfig() {
    classificationConfigDAO.deleteById(configId1);
    List<ClassificationConfigDTO> allDTO =  classificationConfigDAO.findAll();
    Assert.assertEquals(allDTO.size(), 1);
  }
}
