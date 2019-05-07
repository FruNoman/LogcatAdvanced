package com.github.frunoman;

import org.testng.annotations.Test;


public class BaseTest {

    @Test(priority = 1)
    public void dumpTest() throws Exception {
        Logger logger = new Logger("adb")
                .buffer(Buffer.SYSTEM)
                .buffer(Buffer.MAIN)
                .dump();

        for(Line log:logger.readLineLogs()){
            System.out.println(log.getDate()+" "+ log.getPriority()+" pid "+ log.getPid()+" tag "+log.getTag()+" description: "+log.getDescription());
        }

//            for(String log :logger.readLogs()){
//                System.out.println(log);
//            }

    }


}
