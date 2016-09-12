package com.lz69.androidvideoplayer.jni;

public class Test {
    static {
        System.loadLibrary("test");
    }
//    public native String test();
    public static native String test();
}
