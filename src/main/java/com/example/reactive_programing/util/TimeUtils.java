package com.example.reactive_programing.util;

public class TimeUtils {
    public static void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
