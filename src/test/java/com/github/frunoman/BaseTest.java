package com.github.frunoman;

import org.testng.annotations.Test;


public class BaseTest {

    @Test(priority = 1)
    public void lineTest() throws Exception {
        Logcat logcat = new Logcat("adb")
                .dump();
        for (Logcat.Line log : logcat.readLineLogs()) {
            System.out.println(log);
        }
    }

    @Test(priority = 2)
    public void stringTest() throws Exception {
        Logcat logcat = new Logcat("adb")
                .dump();
        for (String log : logcat.readStringLogs()) {
            System.out.println(log);
        }

    }


}
