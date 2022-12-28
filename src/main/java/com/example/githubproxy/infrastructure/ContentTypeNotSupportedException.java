package com.example.githubproxy.infrastructure;

public class ContentTypeNotSupportedException extends RuntimeException {
    public ContentTypeNotSupportedException() {
        super("Content type not supported");
    }
}
