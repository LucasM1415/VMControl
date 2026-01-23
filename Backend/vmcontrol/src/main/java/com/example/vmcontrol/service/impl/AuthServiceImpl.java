package com.example.vmcontrol.service.impl;

import com.example.vmcontrol.config.JwtService;
import com.example.vmcontrol.model.dto.Login.LoginRequestDTO;
import com.example.vmcontrol.model.dto.Login.LoginResponseDTO;
import com.example.vmcontrol.model.dto.Register.RegisterRequestDTO;
import com.example.vmcontrol.model.entity.Usuario;
import com.example.vmcontrol.repository.UsuarioRepository;
import com.example.vmcontrol.service.AuthService;
import com.example.vmcontrol.service.exception.UsuarioException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDTO registrar(RegisterRequestDTO registroRequest) {
        if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
            throw new UsuarioException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(registroRequest.getNome())
                .email(registroRequest.getEmail())
                .senha(passwordEncoder.encode(registroRequest.getSenha()))
                .role("USER")
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);


        String token = jwtService.generateToken(usuarioSalvo);

        return LoginResponseDTO.builder()
                .token(token)
                .email(usuarioSalvo.getEmail())
                .nome(usuarioSalvo.getNome())
                .role(usuarioSalvo.getRole())
                .id(usuarioSalvo.getId())
                .build();
    }

    @Override
    public LoginResponseDTO autenticar(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getSenha()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsuarioException("Usuário não encontrado"));

        // Atualiza último login
        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        // Gera token JWT
        String token = jwtService.generateToken(usuario);

        return LoginResponseDTO.builder()
                .token(token)
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .role(usuario.getRole())
                .id(usuario.getId())
                .build();
    }
}