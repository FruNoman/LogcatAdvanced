package com.github.frunoman.enums;

public enum Buffer {
    RADIO("radio"),
    EVENTS("events"),
    MAIN("main"),
    SYSTEM("system"),
    CRASH("crash"),
    STATS("stats"),
    KERNEL("kernel"),
    ALL("all"),
    DEFAULT("default");

    private final String value;

    Buffer(String value){
        this.value=value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
