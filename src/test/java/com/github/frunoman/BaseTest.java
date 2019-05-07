package com.github.frunoman;

import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseTest {

    @Test(priority = 1)
    public void dumpTest() throws Exception {
        Logger logger = new Logger("adb")
                .buffer(Buffer.SYSTEM)
                .buffer(Buffer.MAIN)
                .dump();

        for(Line log:logger.readLineLogs()){
            System.out.println(log.getDate()+" "+ log.getPriority()+" pid "+ log.getPid()+" tag "+log.getTag());
        }

//            for(String log :logger.readLogs()){
//                System.out.println(log);
//            }

    }


}
