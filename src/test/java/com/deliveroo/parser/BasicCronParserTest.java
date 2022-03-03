package com.deliveroo.parser;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class BasicCronParserTest {

    private CronParser underTest= new BasicCronParser();

    @Test
    void testExceptionParseBlankExpression() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("");
                });
      Assertions.assertEquals("Expression blank. Expecting 6 parts of cron expression separated by space", thrown.getMessage());
    }

    @Test
    void testParseValidMinuteAllStar() {
        Map<String, String> stringMap = underTest.parse("* * * * * *");
        Assertions.assertNotNull(stringMap);
        Assertions.assertEquals(Util.getTestNumbersInRangeWithIncrement(0,59,1), stringMap.get("minute"));
    }

    @Test
    void testParseValidMinuteSingleValue() {
        Map<String, String> stringMap = underTest.parse("10 * * * * *");
        Assertions.assertNotNull(stringMap);
        Assertions.assertEquals(Util.getTestNumbersInRangeWithIncrement(10,10,1), stringMap.get("minute"));
    }

    @Test
    void testParseValidMinuteRange() {
        Map<String, String> stringMap = underTest.parse("1-10 * * * * *");
        Assertions.assertNotNull(stringMap);
        Assertions.assertEquals(Util.getTestNumbersInRangeWithIncrement(1,10,1), stringMap.get("minute"));
    }

    @Test
    void testParseValidMinuteMultipleValues() {
        Map<String, String> stringMap = underTest.parse("1,10 * * * * *");
        Assertions.assertNotNull(stringMap);
        Assertions.assertEquals("1 10", stringMap.get("minute"));
    }

    @Test
    void testParseValidMinuteWithStep() {
        Map<String, String> stringMap = underTest.parse("*/2 * * * * *");
        Assertions.assertNotNull(stringMap);
        Assertions.assertEquals(Util.getTestNumbersInRangeWithIncrement(0,59,2), stringMap.get("minute"));
    }

    @Test
    void testParseValidMinuteMultipleWithStep() {
        Map<String, String> stringMap = underTest.parse("2,10/2 * * * * *");
        Assertions.assertNotNull(stringMap);
        Assertions.assertEquals( "2 "+Util.getTestNumbersInRangeWithIncrement(10,59,2), stringMap.get("minute"));
    }

    @Test
    void testParseValidMinuteRangeComparison() {
        Map<String, String> stringMap1 = underTest.parse("2,10/2 * * * * *");
        Map<String, String> stringMap2 = underTest.parse("10/2,2 * * * * *");
        Assertions.assertNotNull(stringMap1);
        Assertions.assertNotNull(stringMap2);
        Assertions.assertEquals(stringMap1.get("minute"), stringMap2.get("minute"));
    }

    @Test
    void testParseValidMinuteMultiple1() {
        Map<String, String> stringMap = underTest.parse("2,15-20,30/4 12 10-23 3,4 2/3 /sleep/cron");
        Assertions.assertNotNull(stringMap);
        Assertions.assertEquals( "2 "+Util.getTestNumbersInRangeWithIncrement(15,20,1)+" "+Util.getTestNumbersInRangeWithIncrement(30,59,4), stringMap.get("minute"));
    }

    @Test
    void testExceptionParseInvalidMinuteValue1() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("75 * * * * *");
                });
        Assertions.assertEquals("Value 75 not in range [0, 59]", thrown.getMessage());
    }

    @Test
    void testExceptionParseInvalidMinuteValue2() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("60-70 * * * * *");
                });
        Assertions.assertEquals("Value 60 not in range [0, 59]", thrown.getMessage());
    }

    @Test
    void testExceptionParseInvalidMinuteValue3() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("60/2 * * * * *");
                });
        Assertions.assertEquals("Value 60 not in range [0, 59]", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidStepMinute() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("2,15-20,30/0 * * * * *");
                });
        Assertions.assertEquals("Step value for Field : minute should be positive", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidValue4() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("a * * * * *");
                });
        Assertions.assertEquals("Field : minute segment should not contain alphabets", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidValue5() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("^ * * * * *");
                });
        Assertions.assertEquals("Field : minute segment should not contain special characters other than /(slash) ,(comma) *(asterisk) and - (hyphen)", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidValue6() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse(", * * * * *");
                });
        Assertions.assertEquals("Field : minute segment should not only contain special characters /(slash) ,(comma) or - (hyphen)", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidValue7() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("- * * * * *");
                });
        Assertions.assertEquals("Field : minute segment should not only contain special characters /(slash) ,(comma) or - (hyphen)", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidValue8() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse(",- * * * * *");
                });
        Assertions.assertEquals("Field : minute segment should not only contain special characters /(slash) ,(comma) or - (hyphen)", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidValue9() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("1, * * * * *");
                });
        Assertions.assertEquals("Field : minute segment should not end with special characters /(slash) ,(comma) or - (hyphen)", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidValue10() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("/ * * * * *");
                });
        Assertions.assertEquals("Field : minute segment should not only contain special characters /(slash) ,(comma) or - (hyphen)", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidValue11() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("1/ * * * * *");
                });
        Assertions.assertEquals("Field : minute segment should not end with special characters /(slash) ,(comma) or - (hyphen)", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidValue12() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("1- * * * * *");
                });
        Assertions.assertEquals("Field : minute segment should not end with special characters /(slash) ,(comma) or - (hyphen)", thrown.getMessage());
    }

    @Test()
    void testExceptionParseInValidValue13() {
        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    underTest.parse("1,a * * * * *");
                });
        Assertions.assertEquals("Field : minute segment should not contain alphabets", thrown.getMessage());
    }

    @Test
    void testFilterInvalidDatesOfMonth() {
        Map<String, String> stringMap = underTest.parse("2 3 26-30 2 * /sleep/cron");
        Assertions.assertNotNull(stringMap);
        Assertions.assertEquals( Util.getTestNumbersInRangeWithIncrement(26,28,1), stringMap.get("day of month"));
    }

    @Test
    void testFilterInvalidDatesOfMonth2() {
        Map<String, String> stringMap = underTest.parse("2 3 26-30 2,3 1-2 /sleep/cron");
        Assertions.assertNotNull(stringMap);
        Assertions.assertEquals( Util.getTestNumbersInRangeWithIncrement(26,30,1), stringMap.get("day of month"));
    }
}