package com.deliveroo.parser;

public enum FieldType {

    MINUTE("minute",0, 59),
    HOUR("hour",0, 23),
    DAY_OF_MONTH("day of month",1, 30),
    MONTH("month",1, 12),
    DAY_OF_WEEK("day of week",1, 7);

    private final String label;
    private final int minValue;
    private final int maxValue;

    FieldType(String label, int minValue, int maxValue) {
        this.label = label;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public String label() {
        return label;
    }

    public int minValue() {
        return minValue;
    }

    public int maxValue() {
        return maxValue;
    }

    public boolean isValid(int value){
        boolean inRange = value >=minValue && value<=maxValue;
        if(!inRange)
            throw new IllegalArgumentException(String.format("Value %d not in range [%d, %d]",value, this.minValue, this.maxValue));
        return true;
    }
}
