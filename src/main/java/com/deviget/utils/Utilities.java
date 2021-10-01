package com.deviget.utils;

public class Utilities {

    public static int timerCount(long start, long finish){
        long timeElapsed = finish - start;
        return (int)(timeElapsed / 1000);
    }
}