package com.github.frunoman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger {
    private static final String TIME_FORMAT = "MM-dd HH:mm:ss.sss";
    private final String adb;
    private final List<String> udid;
    private List<String> command;
    private StringBuilder formatList;


    public Logger(Logcat logcat) {
        this.formatList = new StringBuilder();
        this.udid = new ArrayList<String>();
        this.command = new ArrayList<String>();
        this.adb = logcat.adb;
        this.udid.addAll(logcat.udid);
        if(!adb.isEmpty()) {
            command.add(adb);
        }
        if(!udid.isEmpty()){
            command.addAll(udid);
        }
        command.add("logcat");
    }

    public static class Logcat{
        private String adb ="";
        private List<String> udid = new ArrayList<String>();

        public Logcat(String adb) {
            this.adb = adb;
        }

        public Logcat() {
        }

        public Logcat udid(String udid){
            this.udid.add("-s");
            this.udid.add(udid);
            return this;
        }

        public Logger build(){
            return new Logger(this);
        }
    }

    public List<String> readLogs() throws IOException {
        command.add(formatList.toString());
        Process process = new ProcessBuilder().command(command).start();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        List<String> logs = new ArrayList<String>();
        String line = "";
        while ((line=bufferedReader.readLine())!=null){
            logs.add(line);
        }
        return logs;
    }

    public Logger dump(){
        command.add("-d");
        return this;
    }

    public Logger time(Date date){
        String data = new SimpleDateFormat(TIME_FORMAT).format(date);
        command.add("-t");
        command.add(data);
        return this;
    }

    public Logger time(long time){
        String data = new SimpleDateFormat(TIME_FORMAT).format(new Date(time));
        command.add("-t");
        command.add(data);
        return this;
    }

    public Logger time(String time){
        command.add("-t");
        command.add(time);
        return this;
    }

    public Logger tag(String tag){
        command.add(tag);
        command.add("*"+Priority.SILENCE);
        return this;
    }

    public Logger tag(String tag,Priority priority){
        command.add(tag+priority);
        command.add("*"+Priority.SILENCE);
        return this;
    }

    public Logger clear(){
        command.add("--clear");
        return this;
    }

    public Logger format(Format format){
        formatList.append("-v"+format.toString());
        return this;
    }

    public Logger bufferSize(){
        command.add("-g");
        return this;
    }

    public Logger count(int count){
        command.add("-m");
        command.add(String.valueOf(count));
        return this;
    }

    public Logger dividers(){
        command.add("-D");
        return this;
    }

    public Logger statistic(){
        command.add("-S");
        return this;
    }

    public Logger pid(int pid){
        command.add("--pid="+pid);
        return this;
    }

    public Logger buffer(Buffer buffer){
        command.add("-b");
        command.add(buffer.toString());
        return this;
    }




}