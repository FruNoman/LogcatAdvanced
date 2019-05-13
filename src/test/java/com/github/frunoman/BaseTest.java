package com.github.frunoman;

import com.github.frunoman.enums.Buffer;
import com.github.frunoman.enums.Priority;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class BaseTest {

    @Test(priority = 1)
    public void lineTest() throws Exception {
        Logcat logcat = new Logcat("adb")
                .count(4)
                .dump()
                .pid(2577)
                .build();
        Logcat.Line line = null;
        while ((line = logcat.readLine())!=null){
            System.out.println(line);
        }

    }

    @Test(priority = 2)
    public void stringTest() throws Exception {
        Logcat logcat = new Logcat("adb")
                .dump();

    }


    @Test(priority = 3)
    public void bufferSizeTest() throws IOException {
        List<Logcat.Buffers> buffers = new Logcat("adb")
                .buffer(Buffer.SYSTEM)
                .bufferSize();
        for (Logcat.Buffers buf : buffers) {
            System.out.println(buf);
            Assert.assertEquals(buf.getBuffer(), (Buffer.SYSTEM));
        }
    }

    @Test(priority = 4)
    public void fileTest() throws IOException {
        File logcat = new Logcat("adb")
                .dump()
                .file("fucking.txt");
        Assert.assertTrue(logcat.exists());
//        logcat.delete();
    }

}
