package com.example.vmcontrol.service.exception;

public class VmNotFoundException extends RuntimeException {
    public VmNotFoundException(String message) {
        super(message);
    }
}