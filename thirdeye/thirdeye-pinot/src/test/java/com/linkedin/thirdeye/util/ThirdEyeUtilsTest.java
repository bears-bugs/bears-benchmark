package com.linkedin.thirdeye.util;


import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

@Test
public class ThirdEyeUtilsTest {

  /*@Test(dataProvider = "testSortedFiltersDataProvider")
  public void testSortedFilters(String filters, String expectedFilters) {
    String sortedFilters = ThirdEyeUtils.getSortedFilters(filters);
    Assert.assertEquals(sortedFilters, expectedFilters);
  }

  @Test(dataProvider = "testSortedFiltersFromJsonDataProvider")
  public void testSortedFiltersFromJson(String filterJson, String expectedFilters) {
    String sortedFilters = ThirdEyeUtils.getSortedFiltersFromJson(filterJson);
    Assert.assertEquals(sortedFilters, expectedFilters);
  }

  @Test(dataProvider = "testSortedFiltersFromMultimapDataProvider")
  public void testSortedFiltersFromMultimap(Multimap<String, String> filterMultimap,
      String expectedFilters) {
    String sortedFilters = ThirdEyeUtils.getSortedFiltersFromMultiMap(filterMultimap);
    Assert.assertEquals(sortedFilters, expectedFilters);
  }

  @DataProvider(name = "testConstructCronDataProvider")
  public Object[][] testConstructCronDataProvider() {
    return new Object[][] {
        {
          "10", "12", "DAYS", "0 10 12 * * ?"
        }, {
          null, null, "DAYS", "0 0 0 * * ?"
        }, {
          null, null, "HOURS", "0 0 * * * ?"
        }, {
          "70", "12", "DAYS", null
        }, {
          "20", "25", "DAYS", null
        }, {
          "70", "12", "WEEKS", null
        }
    };
  }

  @DataProvider(name = "testSortedFiltersDataProvider")
  public Object[][] testSortedFiltersDataProvider() {
    return new Object[][] {
        {
            "a=z;z=d;a=f;a=e;k=m;k=f;z=c;f=g;", "a=e;a=f;a=z;f=g;k=f;k=m;z=c;z=d"
        }, {
            ";", null
        }, {
            "a=b", "a=b"
        }, {
            "a=z;a=b", "a=b;a=z"
        }
    };
  }

  @DataProvider(name = "testSortedFiltersFromJsonDataProvider")
  public Object[][] testSortedFiltersFromJsonDataProvider() {
    return new Object[][] {
        {
            "{\"a\":[\"b\",\"c\"]}", "a=b;a=c"
        }, {
            "{\"z\":[\"g\"],\"x\":[\"l\"],\"a\":[\"b\",\"c\"]}", "a=b;a=c;x=l;z=g"
        }
    };
  }

  @DataProvider(name = "testSortedFiltersFromMultimapDataProvider")
  public Object[][] testSortedFiltersFromMultimapDataProvider() {
    ListMultimap<String, String> multimap1 = ArrayListMultimap.create();
    multimap1.put("a", "b");
    multimap1.put("a", "c");

    ListMultimap<String, String> multimap2 = ArrayListMultimap.create();
    multimap2.put("a", "b");
    multimap2.put("z", "g");
    multimap2.put("k", "b");
    multimap2.put("i", "c");
    multimap2.put("a", "c");

    return new Object[][] {
        {
            multimap1, "a=b;a=c"
        }, {
            multimap2, "a=b;a=c;i=c;k=b;z=g"
        }
    };
  }*/

  @Test
  public void testGetRoundedValue() throws Exception {
    double value = 123;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "123");
    value = 123.24;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "123.24");
    value = 123.246;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "123.25");
    value = 123.241;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "123.24");
    value = 0.23;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0.23");
    value = 0.236;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0.24");
    value = 0.01;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0.01");
    value = 0.016;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0.016");
    value = 0.0167;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0.017");
    value = 0.001;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0.001");
    value = 0.0013;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0.0013");
    value = 0.00135;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0.0014");
    value = 0;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0");
    value = 0.0000;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0");
    value = 0.0000009;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0");
    value = 0.00123456789;
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(value), "0.0012");
  }

  @Test
  public void testGetRoundedValueNonRegularNumber() {
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(Double.NaN), Double.toString(Double.NaN));
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(Double.POSITIVE_INFINITY), Double.toString(Double.POSITIVE_INFINITY));
    Assert.assertEquals(ThirdEyeUtils.getRoundedValue(Double.NEGATIVE_INFINITY), Double.toString(Double.NEGATIVE_INFINITY));
  }

}
