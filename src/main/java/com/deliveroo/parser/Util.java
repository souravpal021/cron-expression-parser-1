package com.deliveroo.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {

    private static final String SPACE  = " ";
    private static final String ZERO  = "0";
    private static final DateTimeFormatter BASIC_ISO_DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

    public static Set<Integer> getNumbersInRange(int... nums) {
        if(nums.length == 1) {
            return IntStream.of(nums[0]).boxed().collect(Collectors.toSet());
        } else {
        return IntStream.rangeClosed(nums[0], nums[1]).boxed().collect(Collectors.toSet());
        }
    }

    public static Set<Integer> getNumbersInRangeWithIncrement(int min, int max, int stepUp) {
        Set<Integer> set = new HashSet<>();
        for(int i=min; i <=max; i = i+stepUp)
            set.add(i);
        return set;
    }

    public static String getTestNumbersInRangeWithIncrement(int min, int max, int stepUp) {
        Set<Integer> set = new HashSet<>();
        for(int i=min; i <=max; i = i+stepUp)
            set.add(i);
        return set.stream().sorted(Comparator.naturalOrder()).map(String::valueOf).collect(Collectors.joining(" "));
    }

    public static String formatOutput(Map<String,String> map) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> entry : map.entrySet()) {
            int len = entry.getKey().length();
            sb.append(entry.getKey()).append(SPACE.repeat(14 - len)).append(entry.getValue()).append("\r\n");
        }
        return sb.toString();
    }

    public static Set<Integer> filterInvalidDatesByMonth(String dayOfMonthVals, String monthVals) {
        String year = String.valueOf(LocalDate.now().getYear());

        Set<Integer> validDayOfMonth = new HashSet<>();
        for(String day : dayOfMonthVals.split(SPACE)) {
            for(String month : monthVals.split(SPACE)) {
                try {
                    if(month.length() == 1) month = ZERO + month;
                    if(day.length() == 1) day = ZERO + day;
                    validDayOfMonth.add(LocalDate.parse(year+month+day, BASIC_ISO_DATE_FORMATTER).getDayOfMonth());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        return validDayOfMonth;
    }
}
