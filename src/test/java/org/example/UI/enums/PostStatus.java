package org.example.UI.enums;

public enum PostStatus {
    REMOVED("REMOVED"),
    ACTIVE("ACTIVE");

    private final String text;

    PostStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
