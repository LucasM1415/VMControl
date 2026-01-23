package com.example.vmcontrol.service;

import com.example.vmcontrol.model.dto.Login.LoginRequestDTO;
import com.example.vmcontrol.model.dto.Login.LoginResponseDTO;
import com.example.vmcontrol.model.dto.Register.RegisterRequestDTO;

public interface AuthService {
    LoginResponseDTO registrar(RegisterRequestDTO registroRequest);
    LoginResponseDTO autenticar(LoginRequestDTO loginRequest);
}