package edu.harvard.h2ms.web.init;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import edu.harvard.h2ms.service.utils.H2msRestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.Assert;

import javax.swing.text.html.Option;
import java.util.*;

/**
 * Unit Tests for H2MS Rest Utility Class
 */
@RunWith(DataProviderRunner.class)
public class H2msRestUtilsTest {

    /**
     * Tests Week Formatting from Utils with valid values
     */
    @Test
    @UseDataProvider(value = "getTestData_for_format_week_success_test")
    public void test_format_week_success(Integer week, Integer year, String result){
        H2msRestUtils  utils = new H2msRestUtils();
        Assert.isTrue(utils.formatWeek(week, year).equals(result));
    }

    @DataProvider(format = "%m[%i: %p[0], %p[1]")
    public static Object[][] getTestData_for_format_week_success_test() {
        return new Object[][]
                {
                        // All valid values of weeks
                        {1, 2018, "1st (2018)"}, {2, 2018, "2nd (2018)"},
                        {3, 2018, "3rd (2018)"}, {4, 2018, "4th (2018)"}, {5, 2018, "5th (2018)"},
                        {6, 2018, "6th (2018)"}, {7, 2018, "7th (2018)"}, {8, 2018, "8th (2018)"},
                        {9, 2018, "9th (2018)"}, {10, 2018, "10th (2018)"}, {11, 2018, "11th (2018)"},
                        {12, 2018, "12th (2018)"}, {13, 2018, "13th (2018)"}, {14, 2018, "14th (2018)"},
                        {15, 2018, "15th (2018)"}, {16, 2018, "16th (2018)"}, {17, 2018, "17th (2018)"},
                        {18, 2018, "18th (2018)"}, {19, 2018, "19th (2018)"}, {20, 2018, "20th (2018)"},
                        {21, 2018, "21st (2018)"}, {22, 2018, "22nd (2018)"}, {23, 2018, "23rd (2018)"},
                        {24, 3023, "24th (3023)"}, {25, 1000, "25th (1000)"}, {26, 318, "26th (318)"},
                        {27, 2018, "27th (2018)"}, {28, 2018, "28th (2018)"}, {29, 2018, "29th (2018)"},
                        {30, 2018, "30th (2018)"}, {31, 2018, "31st (2018)"}, {32, 2018, "32nd (2018)"},
                        {33, 2018, "33rd (2018)"}, {34, 2018, "34th (2018)"}, {35, 2018, "35th (2018)"},
                        {36, 3023, "36th (3023)"}, {37, 1000, "37th (1000)"}, {38, 318, "38th (318)"},
                        {39, 2018, "39th (2018)"}, {40, 2018, "40th (2018)"}, {41, 2018, "41st (2018)"},
                        {42, 2018, "42nd (2018)"}, {43, 2018, "43rd (2018)"}, {44, 2018, "44th (2018)"},
                        {45, 3023, "45th (3023)"}, {46, 1000, "46th (1000)"}, {47, 318, "47th (318)"},
                        {48, 3023, "48th (3023)"}, {49, 1000, "49th (1000)"}, {50, 318, "50th (318)"},
                        {51, 1000, "51st (1000)"}, {52, 318, "52nd (318)"}
                };
    }

    /**
     * Testing Week Formatting from Utils with invalid values
     */
    @Test
    public void test_format_week_invalid_values_result_in_null(){
        H2msRestUtils  utils = new H2msRestUtils();
        Assert.isTrue(utils.formatWeek(null,2018) == null);
        Assert.isTrue(utils.formatWeek(1,null) == null);
        Assert.isTrue(utils.formatWeek(null,null) == null);
        Assert.isTrue(utils.formatWeek(-1,2018) == null);
        Assert.isTrue(utils.formatWeek(0,2018) == null);
        Assert.isTrue(utils.formatWeek(53,2018) == null);
        Assert.isTrue(utils.formatWeek(2,-2007) == null);
    }

    /**
     * Tests Month Formatting from Utils with valid values
     */
    @Test
    @UseDataProvider(value = "getTestData_for_format_month_success_test")
    public void test_format_month_success(Integer month, Integer year, String result){
        H2msRestUtils  utils = new H2msRestUtils();
        Assert.isTrue(utils.formatMonth(month, year).equals(result));
    }

    @DataProvider(format = "%m[%i: %p[0], %p[1]")
    public static Object[][] getTestData_for_format_month_success_test() {
        return new Object[][]
                {
                        // All valid values of months
                        {0, 2020, "January (2020)"}, {1, 2019, "February (2019)"}, {2, 2018, "March (2018)"},
                        {3, 2020, "April (2020)"}, {4, 2018, "May (2018)"}, {5, 2018, "June (2018)"},
                        {6, 2020, "July (2020)"}, {7, 2018, "August (2018)"}, {8, 2018, "September (2018)"},
                        {9, 2020, "October (2020)"}, {10, 2018, "November (2018)"}, {11, 2018, "December (2018)"},
                };
    }

    /**
     * Testing Week Formatting from Utils with invalid values
     */
    @Test
    public void test_format_month_invalid_values_result_in_null(){
        H2msRestUtils  utils = new H2msRestUtils();
        Assert.isTrue(utils.formatMonth(null,2018) == null);
        Assert.isTrue(utils.formatMonth(12,null) == null);
        Assert.isTrue(utils.formatMonth(null,null) == null);
        Assert.isTrue(utils.formatMonth(-1,2018) == null);
        Assert.isTrue(utils.formatMonth(2,-2023) == null);
        Assert.isTrue(utils.formatMonth(12,-2023) == null);
    }

    /**
     * Testing Quarter Formatting from Utils with valid values
     */
    @Test
    @UseDataProvider(value = "getTestData_for_format_quarter_success_test")
    public void test_format_quarter_success(Integer month, Integer year, String result){
        H2msRestUtils  utils = new H2msRestUtils();
        Assert.isTrue(utils.formatQuarter(month, year).equals(result));
    }

    @DataProvider(format = "%m[%i: %p[0], %p[1]")
    public static Object[][] getTestData_for_format_quarter_success_test() {
        return new Object[][]
                {
                        // All valid values for quarter
                        {0, 2018, "Q1 (2018)"},
                        {1, 2020, "Q1 (2020)"},
                        {2, 2020, "Q1 (2020)"},
                        {3, 2018, "Q2 (2018)"},
                        {4, 2018, "Q2 (2018)"},
                        {5, 2018, "Q2 (2018)"},
                        {6, 2018, "Q3 (2018)"},
                        {7, 2020, "Q3 (2020)"},
                        {8, 2018, "Q3 (2018)"},
                        {9, 2018, "Q4 (2018)"},
                        {10, 2018, "Q4 (2018)"},
                        {11, 2018, "Q4 (2018)"}
                };
    }

    /**
     * Testing Quarter Formatting from Utils with invalid values
     */
    @Test
    public void test_format_quarter_invalid_values_result_in_nulls(){
        H2msRestUtils  utils = new H2msRestUtils();
        Assert.isTrue(utils.formatQuarter(null,2018) == null);
        Assert.isTrue(utils.formatQuarter(12,null) == null);
        Assert.isTrue(utils.formatQuarter(null,null) == null);
        Assert.isTrue(utils.formatQuarter(-1,2018) == null);
        Assert.isTrue(utils.formatQuarter(2,-2023) == null);
        Assert.isTrue(utils.formatQuarter(12,-2023) == null);
    }

    /**
     * Test frequency counter successffuly returns counts in key-value map
     */
    @Test
    public void test_frequency_counter(){
        // Three occurrences of March
        List<String> parsedTimestamps = Arrays.asList("January (2020)", "March (2018)", "February (2019)",
                                                          "April (2020)","March (2018)", "April (2020)", "March (2018)");
        Map<String, Long> counts = H2msRestUtils.frequencyCounter(parsedTimestamps);
        Assert.isTrue(counts.size() == 4);
        Assert.isTrue(counts.get("March (2018)") == 3);
    }

    /**
     * Tests frequency counter returns 0 count when list of timestamps is empty
     */
    @Test
    public void test_frequency_counter_when_timestamp_list_is_empty(){
        List<String> parsedTimestamps = Arrays.asList();
        Map<String, Long> counts = H2msRestUtils.frequencyCounter(parsedTimestamps);
        Assert.isTrue(counts.isEmpty());
    }

    @Test
    public void test_parse_timestamp_when_invalid_values(){
        Assert.isTrue(H2msRestUtils.extractParsedTimestamps(new ArrayList<>(), "week").isEmpty());
    }

}
