package com.github.frunoman;

import com.github.frunoman.enums.Buffer;
import com.github.frunoman.enums.Format;
import com.github.frunoman.enums.Priority;
import com.github.frunoman.utils.Finder;
import com.github.frunoman.utils.Utils;

import java.io.*;
import java.util.*;


public class Logcat {
    private static final String TIME_FORMAT = "MM-dd HH:mm:ss.SSS";
    private List<String> command;
    private BufferedReader reader;


    public Logcat() {
        this.command = new LinkedList<String>();
        command.add("logcat");
    }

    public Logcat(String adb) {
        this.command = new LinkedList<>();
        command.add(adb);
        command.add("shell");
        command.add("logcat");
    }

    public Logcat(String adb, String udid) {
        this.command = new LinkedList<String>();
        command.add(adb);
        command.add("-s");
        command.add(udid);
        command.add("shell");
        command.add("logcat");
    }

    public Logger build() throws IOException {
        reader = Utils.execute(command);
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
        command.add(data);
        return this;
    }

    public Logcat time(long time) {
        String data = Utils.formatDate(new Date(time), TIME_FORMAT);
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

    public Logcat pid(String processName) {
        command.add("--pid=$(pidof -s "+processName+")");
        return this;
    }

    public Logcat regex(String expr){
        command.add("--regex="+expr);
        return this;
    }

    public Logcat tail(int number){
        command.add("-T");
        command.add(String.valueOf(number));
        return this;
    }

    public Logcat prune(){
        command.add("--prune");
        return this;
    }

    public Logcat prune(String blacklist){
        command.add("-P");
        command.add(blacklist);
        return this;
    }

    public File file(String name) throws IOException {
        File file = new File(name);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
        BufferedReader bufferedReader = Utils.execute(command);
        String line ="";
        while ((line = bufferedReader.readLine())!=null){
            writer.write(line);
            writer.write("\n");
        }
        writer.close();
        return file;
    }

    public File file(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
        BufferedReader bufferedReader = Utils.execute(command);
        String line ="";
        while ((line = bufferedReader.readLine())!=null){
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

        private Line() {
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

        @Override
        public String toString() {
            return Utils.formatDate(date, TIME_FORMAT)
                    + " " + pid
                    + " " + priority
                    + " " + tag
                    + ": " + description;
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

    public class Logger{
        public Line readLine() throws IOException {
            String line = "";
            Line logLine = null;
            if(((line = reader.readLine()) != null)) {
                logLine = new Line();
                if(!line.contains("----")) {
                    logLine.setPid(Finder.findPid(line));
                    logLine.setPriority(Finder.findPriority(line));
                    logLine.setTag(Finder.findTag(line));
                    logLine.setDescription(Finder.findDescription(line));
                    try {
                        logLine.setDate(Finder.findTime(line));
                    } catch (Exception e) {
                    }
                }else {
                    logLine.setDescription(line.split("---------")[1]);
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