package com.deliveroo.parser;


import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicCronParser implements CronParser {

    private static final String HYPHEN = "-";
    private static final String ASTERISK = "*";
    private static final String COMMA = ",";
    private static final String SLASH = "/";
    private static final String SPACE  = " ";
    private static final String COMMAND  = "command";

    @Override
    public Map<String, String> parse(String cronExpression) {
        if(StringUtils.isBlank(cronExpression))
            throw new IllegalArgumentException("Expression blank. Expecting 6 parts of cron expression separated by space");

        String[] portions = cronExpression.split(SPACE);
        if(portions.length !=6) {
            throw new IllegalArgumentException(String.format("Expecting 6 parts, got %d",portions.length));
        }
        Map<String, String> map = parseMappedPortions(portions);
        filterInvalidDatesByMonth(map);
        return map;
    }

    private Map<String, String> parseMappedPortions(String[] portions) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(FieldType.MINUTE.label(), parse(portions[0], FieldType.MINUTE));
        map.put(FieldType.HOUR.label(), parse(portions[1], FieldType.HOUR));
        map.put(FieldType.DAY_OF_MONTH.label(), parse(portions[2], FieldType.DAY_OF_MONTH));
        map.put(FieldType.MONTH.label(), parse(portions[3], FieldType.MONTH));
        map.put(FieldType.DAY_OF_WEEK.label(), parse(portions[4], FieldType.DAY_OF_WEEK));
        map.put(COMMAND, portions[5]);
        return map;
    }

    private String parse(String expression, FieldType field) {
        validateExpression(expression, field);
        Set<Integer> set = new HashSet<>();
        for(String portion : expression.split(COMMA)) {
            validateExpression(portion, field);
            int slashIndex = portion.indexOf(SLASH);
            if(slashIndex !=-1) {
                String preSlash = portion.substring(0, slashIndex);
                String stepUpPortion = portion.substring(slashIndex+1);
                int stepUp = validateAndParseStepUpValue(field, stepUpPortion);
                int[] range = parseRange(preSlash, field);
                int upperBound = field.maxValue();
                if(range.length ==2) {
                    upperBound = range[1];
                }
                set.addAll(Util.getNumbersInRangeWithIncrement(range[0],upperBound,stepUp));
            } else {
                set.addAll(Util.getNumbersInRange(parseRange(portion, field)));
            }
        }
        return set.stream().sorted(Comparator.naturalOrder()).map(String::valueOf).collect(Collectors.joining(SPACE));
    }

    private void validateExpression(String expression, FieldType field) {
        if(StringUtils.isBlank(expression)) {
            throw new IllegalArgumentException(String.format("Field : %s segment is blank", field.label()));
        } else if(StringUtils.isAlpha(expression)) {
            throw new IllegalArgumentException(String.format("Field : %s segment should not contain alphabets", field.label()));
        } else if(!(StringUtils.isNumeric(expression) || StringUtils.containsAny(expression, '/',',','*','-'))) {
            throw new IllegalArgumentException(String.format("Field : %s segment should not contain special characters other than /(slash) ,(comma) *(asterisk) and - (hyphen)", field.label()));
        } else if(StringUtils.containsOnly(expression, '/',',','-')) {
            throw new IllegalArgumentException(String.format("Field : %s segment should not only contain special characters /(slash) ,(comma) or - (hyphen)", field.label()));
        } else if(StringUtils.containsOnly(expression.substring(expression.length()-1), '/',',','-')) {
            throw new IllegalArgumentException(String.format("Field : %s segment should not end with special characters /(slash) ,(comma) or - (hyphen)", field.label()));
        }
    }

    private int validateAndParseStepUpValue(FieldType field, String stepUpPortion) {
        validateExpression(stepUpPortion, field);
        if(stepUpPortion.contains(ASTERISK)) {
            throw new IllegalArgumentException(String.format("Step value for Field : %s should not contain Asterisk", field.label()));
        }
        int stepUp = Integer.parseInt(stepUpPortion);
        if(stepUp <= 0)
            throw new IllegalArgumentException(String.format("Step value for Field : %s should be positive", field.label()));
        return stepUp;
    }

    private int[] parseRange(String expression, FieldType field) {
        int index;
        int value;
        if(expression.contains(ASTERISK)) {
            return new int[]{field.minValue(), field.maxValue()};
        } else if((index = expression.indexOf(HYPHEN)) != -1) {
            if(index==0) throw new IllegalArgumentException(String.format("Expression for Field : %s cannot start with (-)hyphen", field.label()));
            String lowerBound = expression.substring(0, index);
            validateExpression(lowerBound, field);
            int lowerBoundValue = Integer.parseInt(lowerBound);
            String upperBound = expression.substring(index+1);
            validateExpression(upperBound, field);
            int upperBoundValue = Integer.parseInt(upperBound);
            if (field.isValid(lowerBoundValue) && field.isValid(upperBoundValue))
                return new int[]{lowerBoundValue, upperBoundValue};
        } else {
            if(field.isValid(value = Integer.parseInt(expression))) {
                return new int[]{value};
            }
        }
        return new int[]{-1};
    }

    private void filterInvalidDatesByMonth(Map<String, String> map) {
        Set<Integer> dates = Util.filterInvalidDatesByMonth(map.get(FieldType.DAY_OF_MONTH.label()), map.get(FieldType.MONTH.label()));
        map.put(FieldType.DAY_OF_MONTH.label(), dates.stream().map(String::valueOf).collect(Collectors.joining(SPACE)));
    }
}