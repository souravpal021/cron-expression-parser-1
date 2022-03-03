package com.deliveroo.parser;

import java.util.Map;

public class Launcher {

    public static void main(String[] args) {
        if(args.length==0)
            throw new IllegalArgumentException("Cron Expression Expected");
        CronParser cronParser = new BasicCronParser();
        Map<String, String> map = cronParser.parse(args[0]);
        String output = Util.formatOutput(map);
        System.out.println(output);
    }
}
