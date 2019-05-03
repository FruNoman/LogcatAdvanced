package com.github.frunoman;

import org.testng.annotations.Test;

import java.util.Date;

public class BaseTest {

    @Test
    public void test() throws Exception {
        Logger.Logcat logcat = new Logger.Logcat();

        Date date = new Date();
        System.out.println(logcat.setTime("05-03 09:55:35.473").dump().build());
    }
}
