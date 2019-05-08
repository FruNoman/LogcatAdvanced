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
    private static final String bufferPattern = "([a-z]*):";
    private static final String ringBufferPattern = "(ring buffer is )(\\d*\\w*)";
    private static final String consumedBufferPattern = "([(])(\\d*\\w*)\\s";
    public final static long KB = 1024;
    public final static long MB = 1024 * KB;

    private List<String> command;

    public Logcat() {
        this.command = new ArrayList<String>();
        command.add("logcat");
    }

    public Logcat(String adb) {
        this.command = new ArrayList<String>();
        command.add(adb);
        command.add("logcat");
    }

    public Logcat(String adb, String udid) {
        this.command = new ArrayList<String>();

        command.add(adb);
        command.add("logcat");
        command.add("-s");
        command.add(udid);
    }


    public List<String> readStringLogs() throws IOException {
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
        DateFormat format = new SimpleDateFormat(TIME_FORMAT);
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
        if (!tag.equals("*")) {
            command.add("*:" + Priority.SILENCE);
        }
        return this;
    }

    public Logcat tag(String tag, Priority priority) {
        command.add(tag + ":" + priority);
        if (!tag.equals("*")) {
            command.add("*:" + Priority.SILENCE);
        }
        return this;
    }

    public Logcat clear() {
        command.add("--clear");
        return this;
    }

    public Logcat format(Format format) {
        command.add("-v");
        command.add(format.toString());
        return this;
    }

    public List<Buffers> bufferSize() throws IOException {
        command.add("-g");
        List<Buffers> bufferList = new ArrayList<Buffers>();
            Process process = new ProcessBuilder().command(command).start();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                Buffers buffer = new Buffers();
                buffer.setBuffer(findBufferName(line));
                buffer.setConsumed(findConsumedBufferSize(line));
                buffer.setRingBuffer(findRingBufferSize(line));
                bufferList.add(buffer);

            }

        return bufferList;

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
        String tag = "";
        Pattern r = Pattern.compile(tagPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            tag = (m.group(1).trim());
        }
        return tag;
    }

    private String findDescription(String line) {
        String description = "";
        Pattern r = Pattern.compile(descriptionPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            description = (m.group(2));
        }
        return description;
    }

    private Buffer findBufferName(String line) {
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

    private long findRingBufferSize(String line) {
        long bufferSize = 0;
        Pattern r = Pattern.compile(ringBufferPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
           String size = m.group(2);
            bufferSize = byteConverter(size);
        }
        return bufferSize;
    }

    private long findConsumedBufferSize(String line) {
        long bufferSize = 0;
        Pattern r = Pattern.compile(consumedBufferPattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            String size = m.group(2);
            bufferSize = byteConverter(size);
        }
        return bufferSize;
    }

    private long byteConverter(String line){
        long bufferSize = 0;
        if(line.contains("Mb")){
            bufferSize = Long.parseLong(line.replace("Mb","").trim())*MB;
        }else if(line.contains("Kb")){
            bufferSize = Long.parseLong(line.replace("Kb","").trim())*KB;
        }else if(line.contains("b")){
            bufferSize = Long.parseLong(line.replace("b","").trim());
        }
        return bufferSize;
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

    class Buffers {
        private Buffer buffer;
        private long ringBuffer;
        private long consumed;


        public Buffer getBuffer() {
            return buffer;
        }

        private void setBuffer(Buffer buffer) {
            this.buffer = buffer;
        }

        public long getRingBuffer() {
            return ringBuffer;
        }

        private void setRingBuffer(long ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        public long getConsumed() {
            return consumed;
        }

        private void setConsumed(long consumed) {
            this.consumed = consumed;
        }

        @Override
        public String toString() {
            return buffer +
                    ": ring buffer is " + ringBuffer +
                    " bytes (" + consumed + " bytes consumed)";
        }
    }

}