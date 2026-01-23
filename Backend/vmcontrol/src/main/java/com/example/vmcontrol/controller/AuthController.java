package com.example.vmcontrol.controller;

import com.example.vmcontrol.model.dto.Login.LoginRequestDTO;
import com.example.vmcontrol.model.dto.Login.LoginResponseDTO;
import com.example.vmcontrol.model.dto.Register.RegisterRequestDTO;
import com.example.vmcontrol.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "API para registro e login de usuários")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar novo usuário")
    public ResponseEntity<LoginResponseDTO> registrar(
            @Valid @RequestBody RegisterRequestDTO registerRequest) {
        LoginResponseDTO response = authService.registrar(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.autenticar(loginRequest);
        return ResponseEntity.ok(response);
    }

}