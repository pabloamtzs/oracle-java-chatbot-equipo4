package com.equipo4.chatbot.service.authentication_service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.equipo4.chatbot.controller.auth_controller.LoginRequest;
import com.equipo4.chatbot.model.authentication_response.AuthenticationResponse;
import com.equipo4.chatbot.model.empleado.Empleado;
import com.equipo4.chatbot.repository.empleado_repository.EmpleadoRepository;
import com.equipo4.chatbot.security.config.EmpleadoUserDetails;
import com.equipo4.chatbot.security.jwt.JWTService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmpleadoRepository empleadoRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AuthenticationResponse login(LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getContrasena()
                    )
            );
        } catch (AuthenticationException e) {
            // Manejar la excepción de autenticación
            throw new UsernameNotFoundException("Credenciales inválidas", e);
        }
        Empleado empleado = empleadoRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("Empleado no encontrado"));
        UserDetails user = new EmpleadoUserDetails(empleado);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
    } 
}
