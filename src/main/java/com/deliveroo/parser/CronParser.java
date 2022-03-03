package com.deliveroo.parser;

import java.util.Map;

public interface CronParser {
    Map<String,String> parse(String cronExpression);
}
