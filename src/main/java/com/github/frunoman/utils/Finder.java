package com.github.frunoman.utils;

import com.github.frunoman.enums.Buffer;
import com.github.frunoman.enums.Priority;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {
    private static final String TIME_FORMAT = "MM-dd HH:mm:ss.SSS";
    private static final String priorityPattern = "\\s[A-Z]\\s";
    private static final String pidPattern = "\\s\\d{4,}\\s";
    private static final String tagPattern = "\\s[A-Z]\\s([a-zA-Z\\s\\w-.]*):";
    private static final String descriptionPattern = "\\s[A-Z]\\s([a-zA-Z\\s\\w-.]*):(.*)";
    private static final String bufferPattern = "([a-z]*):";
    private static final String ringBufferPattern = "(ring buffer is )(\\d*\\w*)";
    private static final String consumedBufferPattern = "([(])(\\d*\\w*)\\s";
    private static final String timePattern = "(\\d*)-(\\d*)\\s(\\d*):(\\d*):(\\d*).(\\d*)";

    public static Date findTime(String line)  {
        Date time = null;
        try {
            Pattern r = Pattern.compile(timePattern);
            Matcher m = r.matcher(line);
            if (m.find()) {
                String data = m.group();
                time = Utils.stringFormatToDate(TIME_FORMAT, data);
            }
        }catch (Exception e){

        }
        return time;
    }

    public static Priority findPriority(String line) {
        Priority priority = Priority.UNKNOWN;
        Pattern r = Pattern.compile(priorityPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            for (Priority prior : Priority.values()) {
                if (prior.toString().equals(m.group(0).trim())) {
                    priority = prior;
                    break;
                }
            }
        }
        return priority;
    }

    public static int findPid(String line) {
        int pid = 0;
        Pattern r = Pattern.compile(pidPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            pid = Integer.parseInt(m.group(0).trim());
        }
        return pid;
    }

    public static String findTag(String line) {
        String tag = "";
        Pattern r = Pattern.compile(tagPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            tag = (m.group(1).trim());
        }
        return tag;
    }

    public static String findDescription(String line) {
        String description = "";
        Pattern r = Pattern.compile(descriptionPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            description = (m.group(2));
        }
        return description;
    }

    public static Buffer findBufferName(String line) {
        Buffer buffer = null;
        Pattern r = Pattern.compile(bufferPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            for (Buffer buf : Buffer.values()) {
                if (buf.toString().equals(m.group(1).trim())) {
                    buffer = buf;
                    break;
                }
            }
        }
        return buffer;
    }

    public static long findRingBufferSize(String line) {
        long bufferSize = 0;
        Pattern r = Pattern.compile(ringBufferPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            String size = m.group(2);
            bufferSize = Utils.byteConverter(size);
        }
        return bufferSize;
    }

    public static long findConsumedBufferSize(String line) {
        long bufferSize = 0;
        Pattern r = Pattern.compile(consumedBufferPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            String size = m.group(2);
            bufferSize = Utils.byteConverter(size);
        }
        return bufferSize;
    }
}
