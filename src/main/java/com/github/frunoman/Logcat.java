package com.github.frunoman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logcat {
    private static final String TIME_FORMAT = "MM-dd HH:mm:ss.sss";
    private static final String priorityPattern = "\\s[A-Z]\\s";
    private static final String pidPattern = "\\s\\d{4,}\\s";
    private static final String tagPattern = "\\s[A-Z]\\s([a-zA-Z\\s\\w-.]*):";
    private static final String descriptionPattern = "\\s[A-Z]\\s([a-zA-Z\\s\\w-.]*):(.*)";

    private List<String> command;
    private StringBuilder formatList;

    public Logcat() {
        this.formatList = new StringBuilder();
        this.command = new ArrayList<String>();
        command.add("logcat");
    }

    public Logcat(String adb) {
        this.formatList = new StringBuilder();
        this.command = new ArrayList<String>();
        command.add(adb);
        command.add("logcat");
    }

    public Logcat(String adb, String udid) {
        this.formatList = new StringBuilder();
        this.command = new ArrayList<String>();

        command.add(adb);
        command.add("logcat");
        command.add("-s");
        command.add(udid);
    }


    public List<String> readStringLogs() throws IOException {
        command.add(formatList.toString());
        Process process = new ProcessBuilder().command(command).start();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        List<String> logs = new ArrayList<String>();
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            logs.add(line);
        }
        return logs;
    }

    public List<Line> readLineLogs() throws IOException {
        command.add(formatList.toString());
        DateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss.sss");
        Process process = new ProcessBuilder().command(command).start();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        List<Line> logs = new ArrayList<Line>();
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            Line logLine = new Line();
            logLine.setPid(findPid(line));
            logLine.setPriority(findPriority(line));
            logLine.setTag(findTag(line));
            logLine.setDescription(findDescription(line));
            try {
                logLine.setDate(format.parse(line));
            } catch (Exception e) {
            }
            logs.add(logLine);
        }
        return logs;
    }

    public Logcat dump() {
        command.add("-d");
        return this;
    }

    public Logcat time(Date date) {
        String data = new SimpleDateFormat(TIME_FORMAT).format(date);
        command.add("-t");
        command.add(data);
        return this;
    }

    public Logcat time(long time) {
        String data = new SimpleDateFormat(TIME_FORMAT).format(new Date(time));
        command.add("-t");
        command.add(data);
        return this;
    }

    public Logcat time(String time) {
        command.add("-t");
        command.add(time);
        return this;
    }

    public Logcat tag(String tag) {
        command.add(tag);
        command.add("*:" + Priority.SILENCE);
        return this;
    }

    public Logcat tag(String tag, Priority priority) {
        command.add(tag + ":" + priority);
        command.add("*:" + Priority.SILENCE);
        return this;
    }

    public Logcat clear() {
        command.add("--clear");
        return this;
    }

    public Logcat format(Format format) {
        formatList.append("-v" + format.toString());
        return this;
    }

    public Logcat bufferSize() {
        command.add("-g");
        return this;
    }

    public Logcat count(int count) {
        command.add("-m");
        command.add(String.valueOf(count));
        return this;
    }

    public Logcat dividers() {
        command.add("-D");
        return this;
    }

    public Logcat statistic() {
        command.add("-S");
        return this;
    }

    public Logcat pid(int pid) {
        command.add("--pid=" + pid);
        return this;
    }

    public Logcat buffer(Buffer buffer) {
        command.add("-b");
        command.add(buffer.toString());
        return this;
    }

    private Priority findPriority(String line) {
        Priority priority = null;
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

    private int findPid(String line) {
        int pid = 0;
        Pattern r = Pattern.compile(pidPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            pid = Integer.parseInt(m.group(0).trim());
        }
        return pid;
    }

    private String findTag(String line) {
        String tag = null;
        Pattern r = Pattern.compile(tagPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            tag = (m.group(1).trim());
        }
        return tag;
    }

    private String findDescription(String line) {
        String tag = null;
        Pattern r = Pattern.compile(descriptionPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            tag = (m.group(2));
        }
        return tag;
    }

    public class Line {
        private Date date;
        private int pid;
        private Priority priority;
        private String tag;
        private String description;

        public Date getDate() {
            return date;
        }

        private void setDate(Date date) {
            this.date = date;
        }

        public int getPid() {
            return pid;
        }

        private void setPid(int pid) {
            this.pid = pid;
        }

        public Priority getPriority() {
            return priority;
        }

        private void setPriority(Priority priority) {
            this.priority = priority;
        }

        public String getTag() {
            return tag;
        }

        private void setTag(String tag) {
            this.tag = tag;
        }

        public String getDescription() {
            return description;
        }

        private void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Line{" +
                    "date=" + date +
                    ", pid=" + pid +
                    ", priority=" + priority +
                    ", tag='" + tag + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

}