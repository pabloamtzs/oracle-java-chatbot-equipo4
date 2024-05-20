package com.equipo4.chatbot.security;

import com.equipo4.chatbot.model.empleado.Empleado;
import com.equipo4.chatbot.repository.empleado_repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Empleado empleado = empleadoRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Empleado no encontrado con email: " + username));

        return User.builder()
                .username(empleado.getEmail())
                .password(passwordEncoder.encode(empleado.getContrasena()))
                .roles("USER")
                .build();
    }
}
