package com.github.frunoman.logcat;

import com.github.frunoman.logcat.enums.Buffer;
import com.github.frunoman.logcat.enums.Format;
import com.github.frunoman.logcat.enums.Priority;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class BaseTest {

    @Test(priority = 1)
    public void lineTest() throws Exception {
        Logcat logcat = new Logcat("adb");
        Logcat.Logger logger =
                logcat.regex("device")
                        .dump()
                        .build();
        Logcat.Line line = null;
        while ((line = logger.readLine()) != null) {
            System.out.println(line);
        }
    
        logger =logcat
                        .tag("*", Priority.ERROR)
                        .time("05-16 11:01:49.064")
                        .dump()
                        .build();

        while ((line = logger.readLine()) != null) {
            System.out.println(line);
        }
    }

    @Test(priority = 2)
    public void stringTest() throws Exception {
        Logcat.Logger logcat = new Logcat("adb")
                .format(Format.BRIEF)
                .dump()
                .build();
        String line = "";
        while ((line = logcat.readString()) != null) {
            System.out.println(line);
        }
    }

    @Test(priority = 3)
    public void bufferSizeTest() throws IOException {
        List<Logcat.Buffers> buffers = new Logcat("adb")
                .buffer(Buffer.ALL)
                .bufferSize();
        for (Logcat.Buffers buf : buffers) {
            System.out.println(buf);
//            Assert.assertEquals(buf.getBuffer(), (Buffer.SYSTEM));
        }
    }


    @Test(priority = 4)
    public void regexTest() throws Exception {
        Logcat.Logger logcat = new Logcat("adb")
                .tag("*", Priority.ERROR)
                .dump()
                .build();
        Logcat.Line line = null;
        while ((line = logcat.readLine()) != null) {
            System.out.println(line);
        }

    }

    @Test(priority = 5)
    public void fileTest() throws Exception {
        File logcat = new Logcat("adb")
                .pid("system_server")
                .dump()
                .file("fucking.txt");
        Assert.assertTrue(logcat.exists());

    }

    @Test(priority = 5)
    public void pruneTest() throws Exception {
        Logcat.Logger logcat = new Logcat("adb")
                .prune().build();
        String line = "";
        while ((line = logcat.readString()) != null) {
            System.out.println(line);
        }
    }

}
