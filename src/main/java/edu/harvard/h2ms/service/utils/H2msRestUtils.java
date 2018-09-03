package edu.harvard.h2ms.service.utils;

import edu.harvard.h2ms.domain.core.Answer;
import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.domain.core.Question;
import edu.harvard.h2ms.exception.InvalidTimeframeException;

import static java.util.Arrays.asList;
import edu.harvard.h2ms.service.EventServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class H2msRestUtils {

    final static Logger log = LoggerFactory.getLogger(H2msRestUtils.class);

    /**
     * Iterates through a list of events and parses the timestamps specified
     * by a timeframe, and generates a map with the timeframe as a key.
     *
     * Ex. If the specified timeframe is 'month', the timestamp is parsed as follows:
     *      "2018-03-21T17:58:05.742+0000"  --> "March (2018)"
     * @param events
     * @param timeframe
     * 
     * @return - when events are emptys an empty map is returned
     *         - when timeframe is not a valid value an empty map is returned
     * @throws InvalidTimeframeException 
     */
    public static Map<String, Set<Event>> groupEventsByTimestamp(List<Event> events, String timeframe) throws InvalidTimeframeException{
    	if(!asList("week", "month", "year", "quarter").contains(timeframe)) {
    		throw new InvalidTimeframeException(timeframe);
    	}

    	
        if(events.isEmpty()) {
            log.warn("There are 0 events to be grouped by timestamp.");
            return new HashMap<>();
        }
        
        
        // Parses event timestamps based on timeframe type
        Map<String, Set<Event>> values = new HashMap<>();
        
        for (Event event : events) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(event.getTimestamp());
            
            String hashKey;
            
            if(timeframe.equals("week")) {            	
            	hashKey = formatWeek(cal.get(Calendar.WEEK_OF_YEAR), cal.get(Calendar.YEAR)); 
            } else if (timeframe.equals("month")) {
            	hashKey = formatMonth(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
            } else if (timeframe.equals("year")) {
            	hashKey = String.valueOf(cal.get(Calendar.YEAR));
            } else if (timeframe.equals("quarter")) {
            	hashKey = formatQuarter(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
            } else {
            	return values;
            }
            
            if(values.containsKey(hashKey)) {
            	values.get(hashKey).add(event);
            } else {
            	values.put(hashKey, new HashSet<Event>(asList(event)));
            }
        }
        
        return values;
    }

    public static <E> Map<String, Long> frequencyCounter (Map<String, Set<E>> mapSet){
    	return mapSet.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> Long.valueOf(e.getValue().size())));
    }
    
    public static double calculateCompliance(Question question, Set<Event> events) {
    	return events.stream()
    			.filter(e -> e.getAnswer(question) != null)
    			.mapToDouble(e -> e.getAnswer(question).getValue().equals("true") ? 1 : 0)
    			.average()
    			.orElse(0.0);    	
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


    /**
     * H2MS Average Definition: X Out of Y Times Percentage
     * @param numerator
     * @param denominator
     * @return
     */
    public static Double calculateAverage(Double numerator, Double denominator){
        Double result = null;
        if(numerator > 0 && denominator > 0 ) {
            result = numerator/denominator;
        } else {
            return 0.0;
        }
        NumberFormat nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        return Double.valueOf(nf.format(result));
    }

}
