package com.github.frunoman;

import org.testng.annotations.Test;

import java.util.Date;

public class BaseTest {

    @Test(priority = 1)
    public void dumpTest() throws Exception {
        Logger logger = new Logger.Logcat("adb").udid("0000").build();

        logger.dump();
        for(String log:logger.readLogs()){
            System.out.println(log);
        }
    }


}
