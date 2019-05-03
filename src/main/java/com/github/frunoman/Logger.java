package com.github.frunoman;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Logger {
    private static String adbPath = "adb";
    private final String buffer;
    private final String filename;
    private final String pid;
    private final List<String> time;
    private final String timeDump;
    private final String clear;
    private final String dump;
    private final String format;
    private List<String> command;
    private final String maxCount;
    private final String tag;
    private final String silence;

    private static final String TIME_FORMAT = "MM-dd HH:mm:ss.sss";

    public static class Logcat {

        private StringBuilder buffer = new StringBuilder();
        private String filename = "";
        private String pid = "";
        private List<String> time  = new ArrayList<String>();
        private String timeDump = "";
        private String clear = "";
        private String dump = "";
        private StringBuilder format = new StringBuilder();
        private String maxCount = "";
        private StringBuilder tag = new StringBuilder();
        private String silence = "";

        public Logcat() {

        }

        public Logcat setBuffer(String buffer) {
            this.buffer.append("-b " + buffer + " ");
            return this;
        }

        public Logcat setFilename(String filename) {
            this.filename = "-f " + filename;
            return this;
        }

        public Logcat setPid(int pid) {
            this.pid = "--pid=" + pid;
            return this;
        }

        public Logcat setTime(String time) {
            this.time.add("-t");
            this.time.add(time);
            return this;
        }

        public Logcat setTime(long time) {
            Date date = new Date(time);
            String dataFormat = new SimpleDateFormat(TIME_FORMAT).format(date);
            this.time.add("-t");
            this.time.add(dataFormat);
            return this;
        }

        public Logcat setTime(Date date) {
            String dataFormat = new SimpleDateFormat(TIME_FORMAT).format(date);
            this.time.add("-t");
            this.time.add(dataFormat);
            return this;
        }

        public Logcat clear() {
            this.clear = "-c";
            return this;
        }

        public Logcat dump() {
            this.dump = "-d";
            return this;
        }

        public Logcat setFormat(Format format) {
            this.format.append("-v" + format);
            return this;
        }

        public Logcat setMaxCount(int count) {
            this.maxCount = "-m " + count;
            return this;
        }

        public Logcat setTimeDump(String timeDump) {
            this.timeDump = "-T '" + timeDump + "'";
            return this;
        }

        public Logcat setTimeDump(long timeDump) {
            Date date = new Date(timeDump);
            String dataFormat = new SimpleDateFormat(TIME_FORMAT).format(date);
            this.timeDump = "-T '" + dataFormat + "'";
            return this;
        }

        public Logcat setTimeDump(Date date) {
            String dataFormat = new SimpleDateFormat(TIME_FORMAT).format(date);
            this.timeDump = "-T '" + dataFormat + "'";
            return this;
        }

        public Logcat setTag(String tag) {
            this.tag.append(tag);
            return this;
        }

        public Logcat setTag(String tag, Priority priority) {
            this.tag.append(tag + priority + " ");
            return this;
        }

        public Logcat silence() {
            this.silence = "-s";
            return this;
        }

        public String build() throws IOException {
            Logger logger = new Logger(this);
            return execute(logger.getCommand());
        }
    }

    public Logger(Logcat builder) {
        command = new ArrayList<String>();
        buffer = builder.buffer.toString();
        filename = builder.filename;
        pid = builder.pid;
        time = builder.time;
        clear = builder.clear;
        dump = builder.dump;
        format = builder.format.toString();
        maxCount = builder.maxCount;
        timeDump = builder.timeDump;
        tag = builder.tag.toString();
        silence = builder.silence;
        command.add(adbPath);
        command.add("logcat");
        command.add(tag);
        command.add(silence);
        command.add(buffer);
        command.add(format);
        command.addAll(time);
        command.add(timeDump);
        command.add(maxCount);
        command.add(dump);
        command.add(filename);
        command.add(clear);
    }

    private String[] getCommand() {
        return  command.toArray(new String[0]);
    }

    private static String execute(String[] command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

}