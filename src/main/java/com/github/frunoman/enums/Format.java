package com.github.frunoman.enums;

public enum Format {
    BRIEF("brief"),
    LONG("long"),
    PROCESS("process"),
    RAW("raw"),
    TAG("tag"),
    THREAD("thread"),
    THREADTIME("threadtime"),
    TIME("time"),
    COLOR("color"),
    EPOCH("epoch"),
    MONOTONIC("monotonic"),
    USEC("usec"),
    UTC("UTC"),
    YEAR("year"),
    ZONE("zone");

    private final String value;

    Format(String value){
        this.value=value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
