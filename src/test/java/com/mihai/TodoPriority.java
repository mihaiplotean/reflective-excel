package com.mihai;

public enum TodoPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    ;

    private final String name;

    TodoPriority(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
