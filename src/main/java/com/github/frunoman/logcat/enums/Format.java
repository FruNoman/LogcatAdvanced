package com.github.frunoman.logcat.enums;

public enum Format {
    BRIEF("brief"),
    LONG("long"),
    PROCESS("process"),
    RAW("raw"),
    TAG("tag"),
    THREAD("thread"),
    THREADTIME("threadtime"),
    TIME("time"),
    EPOCH("epoch"),
    MONOTONIC("monotonic"),
    YEAR("year"),
    ZONE("zone"),
    DEFAULT("");

    private final String value;

    Format(String value){
        this.value=value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
