package com.jb;

public class InvalidFileFormatException extends RuntimeException {
    private String reason;

    public InvalidFileFormatException(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
