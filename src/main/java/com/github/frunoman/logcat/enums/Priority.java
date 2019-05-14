package com.github.frunoman.logcat.enums;

public enum Priority {
    VERBOSE("V"),
    DEBUG("D"),
    INFO("I"),
    WARNING("W"),
    ERROR("E"),
    ASSERT("A"),
    SILENCE("S"),
    FATAL("F"),
    UNKNOWN("---------");

    private final String value;

    Priority(String value){
        this.value=value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
