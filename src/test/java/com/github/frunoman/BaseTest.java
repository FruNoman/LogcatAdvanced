package com.github.frunoman;

import org.testng.annotations.Test;


public class BaseTest {

    @Test(priority = 1)
    public void lineTest() throws Exception {
        Logger logger = new Logger("adb")
                .dump();
        for (Logger.Line log : logger.readLineLogs()) {
            System.out.println(log);
        }
    }

    @Test(priority = 2)
    public void stringTest() throws Exception {
        Logger logger = new Logger("adb")
                .dump();
        for (String log : logger.readStringLogs()) {
            System.out.println(log);
        }

    }


}
