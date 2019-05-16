package com.github.frunoman.logcat;

import com.github.frunoman.logcat.android.AndroidDevice;
import com.github.frunoman.logcat.enums.Buffer;
import com.github.frunoman.logcat.enums.Format;
import com.github.frunoman.logcat.enums.Priority;
import com.github.frunoman.logcat.utils.Finder;
import com.github.frunoman.logcat.utils.Utils;

import java.io.*;
import java.util.*;


public class Logcat {
    private static final String TIME_FORMAT = "MM-dd HH:mm:ss.SSS";
    private static final String YEAR_TIME_FORMAT = "YYYY-MM-ddHH:mm:ss.SSS";

    private List<String> command;
    private List<String> baseCommand;
    private List<String> flashCommand;
    private BufferedReader reader;
    private Format currentFormat = Format.DEFAULT;

    public Logcat() {
        this.command = new LinkedList<String>();
        this.baseCommand = new ArrayList<>(command);
        command.add("logcat");
        command.add("-v");
        command.add(Format.YEAR.toString());
        this.flashCommand = new ArrayList<>(command);

    }

    public Logcat(String adb) {
        this.command = new LinkedList<>();
        command.add(adb);
        command.add("shell");
        baseCommand = new ArrayList<>(command);
        command.add("logcat");
        command.add("-v");
        command.add(Format.YEAR.toString());
        this.flashCommand = new ArrayList<>(command);

    }

    public Logcat(String adb, String udid) {
        this.command = new LinkedList<String>();
        command.add(adb);
        command.add("-s");
        command.add(udid);
        command.add("shell");
        baseCommand = new ArrayList<>(command);
        command.add("logcat");
        command.add("-v");
        command.add(Format.YEAR.toString());
        this.flashCommand = new ArrayList<>(command);
    }

    public Logger build() throws Exception {
        if (Utils.isAndroid()) {
            reader = AndroidDevice.executeAndroidRootShell(command);
        } else {
            reader = Utils.execute(command);
        }
        command = flashCommand;
        return new Logger();
    }

    public List<Buffers> bufferSize() throws IOException {
        command.add("-g");
        BufferedReader bufferedReader = Utils.execute(command);
        List<Buffers> bufferList = new ArrayList<Buffers>();
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            Buffers buffer = new Buffers();
            buffer.setBuffer(Finder.findBufferName(line));
            buffer.setConsumed(Finder.findConsumedBufferSize(line));
            buffer.setRingBuffer(Finder.findRingBufferSize(line));
            bufferList.add(buffer);
        }
        return bufferList;
    }

    public Logcat dump() {
        command.add("-d");
        return this;
    }

    public Logcat time(Date date) {
        String data = Utils.formatDate(date, TIME_FORMAT);
        command.add("-t");
        command.add(data.replace(" ", ""));
        return this;
    }

    public Logcat time(long time) {
        String data = Utils.formatDate(new Date(time), TIME_FORMAT);
        command.add("-t");
        command.add(data.replace(" ", ""));
        return this;
    }

    public Logcat time(String time) {
        command.add("-t");
        command.add(time.replace(" ", ""));
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
        currentFormat = format;
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

    public Logcat pid(String processName) throws Exception {
        baseCommand.add("pidof");
        baseCommand.add("-s");
        baseCommand.add(processName);
        BufferedReader reader = null;
        if (Utils.isAndroid()) {
            reader = AndroidDevice.executeAndroidRootShell(baseCommand);
        } else {
            reader = Utils.execute(baseCommand);
        }
        String pid = reader.readLine();
        command.add("--pid=" + pid);
        return this;
    }

    public Logcat regex(String expr) {
        command.add("--regex=" + expr);
        return this;
    }

    public Logcat tail(int number) {
        command.add("-T");
        command.add(String.valueOf(number));
        return this;
    }

    public Logcat prune() {
        command.add("--prune");
        return this;
    }

    public Logcat prune(String blacklist) {
        command.add("-P");
        command.add(blacklist);
        return this;
    }

    public File file(String name) throws IOException {
        File file = new File(name);
        return file(file);
    }

    public File file(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        BufferedReader bufferedReader = Utils.execute(command);
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            writer.write(line);
            writer.write("\n");
        }
        writer.close();
        return file;
    }

    public Logcat buffer(Buffer buffer) {
        command.add("-b");
        command.add(buffer.toString());
        return this;
    }


    public class Line {
        private Date date;
        private int pid;
        private Priority priority;
        private String tag;
        private String description;
        private Format currentFormat;

        private Line(Format format) {
            this.currentFormat = format;
        }

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

        private String getFormatOutput(Format format) {
            String output = "";
            switch (format) {
                case BRIEF:
                    output = String.format("%s/%s( %d): %s", priority, tag, pid, description);
                    break;
                case LONG:
                    output = String.format("[ %s %d: %s/%s]\n %s\n", Utils.formatDate(date, TIME_FORMAT), pid, priority, tag, description);
                    break;
                case PROCESS:
                    output = String.format("%s( %d) %s (%s)", priority, pid, description, tag);
                    break;
                case RAW:
                    output = String.format("%s", description);
                    break;
                case TAG:
                    output = String.format("%s/%s: %s", priority, tag, description);
                    break;
                case THREAD:
                    output = String.format("%s( %d:) %s", priority, pid, description);
                    break;
                case TIME:
                    output = String.format("%s %s/%s( %d): %s", Utils.formatDate(date, TIME_FORMAT), priority, tag, pid, description);
                    break;
                case YEAR:
                    output = String.format("%s %s/%s( %d): %s", Utils.formatDate(date, YEAR_TIME_FORMAT), priority, tag, pid, description);
                    break;
                default:
                    output = String.format("%s %d %s %s: %s", Utils.formatDate(date, TIME_FORMAT), pid, priority, tag, description);
                    break;
            }
            return output;
        }

        @Override
        public String toString() {
            return getFormatOutput(currentFormat);
        }
    }

    public class Buffers {
        private Buffer buffer;
        private long ringBuffer;
        private long consumed;

        private Buffers() {
        }

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

    public class Logger {
        public Line readLine() throws IOException {
            String line = "";
            Line logLine = null;
            if (((line = reader.readLine()) != null)) {
                logLine = new Line(currentFormat);
                if (!line.contains("----")) {
                    logLine.setPid(Finder.findPid(line));
                    logLine.setPriority(Finder.findPriority(line));
                    logLine.setTag(Finder.findTag(line));
                    logLine.setDescription(Finder.findDescription(line));
                    try {
                        logLine.setDate(Finder.findTime(line));
                    } catch (Exception e) {
                    }
                } else {
                    logLine.setDescription(line);
                    logLine.setPriority(Priority.UNKNOWN);
                    logLine.setTag(" --------- ");
                    logLine.setPid(0);
                }
            }
            return logLine;
        }

        public String readString() throws IOException {
            return reader.readLine();
        }
    }

}