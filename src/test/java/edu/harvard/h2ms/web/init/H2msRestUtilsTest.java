package edu.harvard.h2ms.web.init;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import edu.harvard.h2ms.service.utils.H2msRestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;


@RunWith(DataProviderRunner.class)
public class H2msRestUtilsTest {

    @Test
    @UseDataProvider(value = "getTestData_for_format_week_success_test")
    public void format_week_success(Integer week, Integer year, String result){
        H2msRestUtils  utils = new H2msRestUtils();
        Assert.isTrue(utils.formatWeek(week, year).equals(result));
    }

    @DataProvider(format = "%m[%i: %p[0], %p[1]")
    public static Object[][] getTestData_for_format_week_success_test() {
        return new Object[][]
                {
                        {2, 2018, "2nd (2018)"},
                        {11, 2018, "11th (2018)"},
                        {12, 2018, "12th (2018)"},
                        {13, 2018, "13th (2018)"},
                        {21, 2018, "21st (2018)"},
                        {22, 2018, "22nd (2018)"},
                        {23, 2018, "23rd (2018)"},
                        {44, 3023, "44th (3023)"},
                        {36, 1000, "36th (1000)"},
                        {41, 318, "41st (318)"}
                };
    }

    @Test
    @UseDataProvider(value = "getTestData_for_format_month_success_test")
    public void format_month_success(Integer month, Integer year, String result){
        H2msRestUtils  utils = new H2msRestUtils();
        Assert.isTrue(utils.formatMonth(month, year).equals(result));
    }

    @DataProvider(format = "%m[%i: %p[0], %p[1]")
    public static Object[][] getTestData_for_format_month_success_test() {
        return new Object[][]
                {
                        {2, 2018, "March (2018)"},
                        {0, 2020, "January (2020)"},
                        {11, 2018, "December (2018)"},
                        {23, 2018, ""}
                };
    }

    @Test
    @UseDataProvider(value = "getTestData_for_format_quarter_success_test")
    public void format_quarter_success(Integer month, Integer year, String result){
        H2msRestUtils  utils = new H2msRestUtils();
        Assert.isTrue(utils.formatQuarter(month, year).equals(result));
    }

    @DataProvider(format = "%m[%i: %p[0], %p[1]")
    public static Object[][] getTestData_for_format_quarter_success_test() {
        return new Object[][]
                {
                        {2, 2018, "Q1 (2018)"},
                        {0, 2020, "Q1 (2020)"},
                        {11, 2018, "Q4 (2018)"},
                        {4, 2018, "Q2 (2018)"},
                        {7, 2018, "Q3 (2018)"}
                };
    }

}
