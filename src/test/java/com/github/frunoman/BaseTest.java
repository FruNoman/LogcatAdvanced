package com.github.frunoman;

import com.github.frunoman.enums.Buffer;
import com.github.frunoman.enums.Format;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class BaseTest {

    @Test(priority = 1)
    public void lineTest() throws Exception {
        Logcat.Logger logcat = new Logcat("adb")
                .pid("com.android.car")
                .dump()
                .build();
        Logcat.Line line = null;
        while ((line = logcat.readLine())!=null){
            System.out.println(line);
        }
    }

    @Test(priority = 2)
    public void stringTest() throws Exception {
        Logcat.Logger logcat = new Logcat("adb")
                .format(Format.PROCESS)
                .dump()
                .build();
        String line = "";
        while ((line = logcat.readString())!=null){
            System.out.println(line);
        }
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
    public void regexTest() throws IOException {
        Logcat.Logger logcat = new Logcat("adb")
                .dump()
                .regex("uid")
                .tail(20)
                .build();
        Logcat.Line line = null;
        while ((line = logcat.readLine())!=null){
            System.out.println(line);
        }

    }

    @Test(priority = 5)
    public void fileTest() throws IOException {
        File logcat = new Logcat("adb")
                .dump()
                .file("fucking.txt");
        Assert.assertTrue(logcat.exists());
        logcat.delete();
    }

    @Test(priority = 5)
    public void pruneTest() throws IOException {
        Logcat.Logger logcat = new Logcat("adb")
                .prune().build();
        String line = "";
        while ((line = logcat.readString())!=null){
            System.out.println(line);
        }
    }

}
