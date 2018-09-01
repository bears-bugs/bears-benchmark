package edu.harvard.h2ms.service.utils;

import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.service.EventServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class H2msRestUtils {

    final static Logger log = LoggerFactory.getLogger(H2msRestUtils.class);

    /**
     * Iterates through a list of events and parses the timestamps specified
     * by a timeframe.
     * Ex. If the specified timeframe is 'month', the timestamp is parsed as follows:
     *      "2018-03-21T17:58:05.742+0000"  --> "March (2018)"
     * @param events
     * @param timeframe
     * @return - when events are emptys a null string list is returned
     *         - when timeframe is not a valid value a null string list is returned
     */
    public static List<String> extractParsedTimestamps(List<Event> events, String timeframe){
        if(events.isEmpty()) {
            log.warn("There are 0 events in the H2MS system.");
            return new ArrayList<>();
        }
        // Parses event timestamps based on timeframe type
        List<String> values = new ArrayList<>();
        for (Event event : events) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(event.getTimestamp());
            if(timeframe.equals("week")) {
                values.add(formatWeek(cal.get(Calendar.WEEK_OF_YEAR), cal.get(Calendar.YEAR)));
            } else if (timeframe.equals("month")) {
                values.add(formatMonth(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)));
            } else if (timeframe.equals("year")) {
                values.add(String.valueOf(cal.get(Calendar.YEAR)));
            } else if (timeframe.equals("quarter")) {
                values.add(formatQuarter(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)));
            }
        }
        return values;
    }

    public static Map<String, Long> frequencyCounter (List<String> parsedTimestamps){
        return parsedTimestamps.stream().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
    }

    public static String formatMonth(Integer month, Integer year){
        // Null cases
        if (month == null || year == null)
            return null;

        // Invalid value cases
        if(month < 0 || month > 11 || year < 1)
            return null;

        // Format Month
        if(month.equals(0)){
            return "January " + "(" + year + ")";
        } else if(month.equals(1)){
            return "February " + "(" + year + ")";
        } else if(month.equals(2)){
            return "March " + "(" + year + ")";
        } else if(month.equals(3)){
            return "April " + "(" + year + ")";
        } else if(month.equals(4)){
            return "May " + "(" + year + ")";
        } else if(month.equals(5)){
            return "June " + "(" + year + ")";
        } else if(month.equals(6)){
            return "July " + "(" + year + ")";
        } else if(month.equals(7)){
            return "August " + "(" + year + ")";
        } else if(month.equals(8)){
            return "September " + "(" + year + ")";
        } else if(month.equals(9)){
            return "October " + "(" + year + ")";
        } else if(month.equals(10)){
            return "November " + "(" + year + ")";
        } else if (month.equals(11)){
            return "December " + "(" + year + ")";
        } else {
            return null;
        }

    }

    public static String formatWeek(Integer week, Integer year){

        // Null cases
        if(week == null || year == null)
                return null;

        // Invalid value cases
        if(week < 1 || week > 52 || year < 1)
            return null;

        // Format Week for special cases (ie. 11, 12, 13 are postfixed with 'th')
        if(week == 11 || week == 12 || week == 13)
            return String.valueOf(week) + "th (" + year + ")";

        // Format Week
        char digit;
        if(week.toString().length() == 2){
            digit = week.toString().charAt(1);
        } else {
            digit = week.toString().charAt(0);
        }
        if(digit == '1'){
            return String.valueOf(week) + "st (" + year + ")";
        } else if (digit == '2') {
            return String.valueOf(week) + "nd (" + year + ")";
        } else if (digit == '3') {
            return String.valueOf(week) + "rd (" + year + ")";
        } else {
            return String.valueOf(week) + "th (" + year + ")";
        }
    }

    public static String formatQuarter(Integer month, Integer year){

        // Null cases
        if(month == null || year == null)
             return null;

        // Invalid value cases
        if(month < 0 || month > 11 || year < 1)
            return null;

        // Format month
        if (month == 0 || month == 1 || month == 2 ) {
            return "Q1 (" + year + ")";
        } else if (month == 3 || month == 4 || month == 5 ) {
            return "Q2 (" + year + ")";
        } else if (month == 6 || month == 7 || month == 8 )  {
            return "Q3 (" + year + ")";
        } else {
            return "Q4 (" + year + ")";
        }
    }

}
