package com.github.frunoman.logcat.android;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AndroidDevice {
    private static String instrumentationRegistry = "android.support.test.InstrumentationRegistry";
    private static String parcelDescriptorClass = "android.os.ParcelFileDescriptor$AutoCloseInputStream";
    private static String getInstrumentationMethod = "getInstrumentation";
    private static String getAutomationMethod = "getUiAutomation";
    private static String executeShellCommandMethod = "executeShellCommand";

    public static BufferedReader executeAndroidRootShell(List<String> command) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException {
        StringBuilder builder = new StringBuilder();
        for (String str : command) {
            builder.append(str);
            builder.append(" ");
        }
        Class<?> instrumentationRegistryClazz = Class.forName(instrumentationRegistry);
        Method getInstrumentation = instrumentationRegistryClazz.getMethod(getInstrumentationMethod);
        Object instance = getInstrumentation.invoke(null);
        Method getAutomation = instance.getClass().getMethod(getAutomationMethod);
        Object automation = getAutomation.invoke(instance);
        Method executeShell = automation.getClass().getMethod(executeShellCommandMethod, String.class);
        Object parcelDescriptor = executeShell.invoke(automation, builder.toString());
        Class<?> clazz = Class.forName(parcelDescriptorClass);
        Constructor<?> cons = clazz.getConstructor(parcelDescriptor.getClass());
        FileInputStream fis = (FileInputStream) cons.newInstance(parcelDescriptor);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        return reader;
    }

}
