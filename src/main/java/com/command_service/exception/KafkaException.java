package com.command_service.exception;

public class KafkaException extends RuntimeException {

    public KafkaException(String message) {
        super(message);
    }

    public KafkaException(String message, Throwable cause) {
        super(message, cause);
    }

}
