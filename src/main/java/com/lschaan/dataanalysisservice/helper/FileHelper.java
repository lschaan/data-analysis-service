package com.lschaan.dataanalysisservice.helper;

public class FileHelper {

    public static String getInputDirectory() {
        return System.getProperty("user.home") + "/data/in/";
    }

    public static String getOutputDirectory() {
        return System.getProperty("user.home") + "/data/out/";
    }
}
