package com.example.vmcontrol.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsuarioException extends RuntimeException {

    public UsuarioException(String message) {
        super(message);
    }

    public UsuarioException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UsuarioException emailJaCadastrado(String email) {
        return new UsuarioException("Email já cadastrado: " + email);
    }

    public static UsuarioException credenciaisInvalidas() {
        return new UsuarioException("Email ou senha inválidos");
    }

    public static UsuarioException usuarioNaoEncontrado(Long id) {
        return new UsuarioException("Usuário não encontrado com ID: " + id);
    }
}