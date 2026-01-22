package com.example.vmcontrol.service.exception;

public class VmNotFoundException extends RuntimeException {

    public VmNotFoundException(String message) {
        super(message);
    }

    public VmNotFoundException(Long id) {
        super("VM não encontrada com ID: " + id);
    }

    public VmNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}