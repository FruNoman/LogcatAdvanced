package com.github.frunoman.logcat.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Utils {
    public final static long KB = 1024;
    public final static long MB = 1024 * KB;

    public static long byteConverter(String line){
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

    public static String formatDate(Date date, String format){
        String data = "";
        if(date!=null) {
            data = new SimpleDateFormat(format).format(date);
        }
        return data;
    }

    public static Date stringFormatToDate(String format,String data) throws ParseException {
//        TimeZone gmt = TimeZone.getTimeZone("GMT");
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
//        dateFormat.setTimeZone(gmt);
        return dateFormat.parse(data);
    }

    public static BufferedReader execute(List<String> command) throws IOException {
        Process process = new ProcessBuilder().command(command).start();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        return bufferedReader;
    }

    public static boolean isAndroid(){
        boolean android = false;
        if(System.getProperty("java.vm.name").equalsIgnoreCase("Dalvik")) {
            android = true;
        }
        return android;
    }
}
