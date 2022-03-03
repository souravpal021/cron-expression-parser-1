# Cron Expression Parser

A command line application  which parses a cron string and expands each field to show the times at which it will run.

It supports the standard cron format with five time fields (minute, hour, day of month, month, and day of week) plus a command. It does not handle the special time strings such as "@yearly". 
The input will be on a single line.

The cron string will be passed to the application as a single argument.

The output should be formatted as a table with the field name taking the first 14 columns and
the times as a space-separated list following it.

<pre>
minute        0 15 30 45
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find

</pre>

References : https://crontab.guru/


## Prerequisites

Install Java (11+) and gradle

## Build

Execute :
<pre>
gradle clean build
</pre>
The java project is built, unit test cases are executed and executable jar , CronParser-1.0-SNAPSHOT.jar, is created in build/libs directory.

## Run

<pre>
java -jar build/libs/CronParser-1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"
</pre>


