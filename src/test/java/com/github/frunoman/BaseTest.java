package com.github.frunoman;

import com.github.frunoman.enums.Buffer;
import com.github.frunoman.enums.Priority;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;


public class BaseTest {

    @Test(priority = 1)
    public void lineTest() throws Exception {
        Logcat logcat = new Logcat("adb")
                .time("05-10 10:05:40.321")
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


    @Test(priority = 3)
    public void bufferSizeTest() throws IOException {
        List<Logcat.Buffers> buffers = new Logcat("adb")
                .buffer(Buffer.SYSTEM)
                .bufferSize();
        for (Logcat.Buffers buf : buffers) {
            System.out.println(buf);
            Assert.assertEquals(buf.getBuffer(),(Buffer.SYSTEM));
        }
    }


}
