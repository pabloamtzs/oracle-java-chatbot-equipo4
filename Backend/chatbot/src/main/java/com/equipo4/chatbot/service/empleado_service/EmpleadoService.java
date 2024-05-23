package com.equipo4.chatbot.service.empleado_service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.equipo4.chatbot.model.empleado.Empleado;
import com.equipo4.chatbot.repository.empleado_repository.EmpleadoRepository;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<List<Empleado>> getEmpleadoList() {
        List<Empleado> empleados = empleadoRepository.findAll();
        return ResponseEntity.ok(empleados);
    }

    public ResponseEntity<Empleado> getEmpleadoById(long id) {
        Optional<Empleado> data = empleadoRepository.findById(id);
        return data.map(empleado -> ResponseEntity.ok(empleado))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Empleado> getEmpleadoByEmail(String email) {
        Optional<Empleado> data = empleadoRepository.findByEmail(email);
        return data.map(empleado -> ResponseEntity.ok(empleado))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }    

    public ResponseEntity<Empleado> addEmpleado(Empleado empleado) {
        empleado.setContrasena(passwordEncoder.encode(empleado.getContrasena()));
        if (empleado.getId_empleado() == null) {
            empleado.setId_empleado(null);
        } else {
            Optional<Empleado> existingEmpleado = empleadoRepository.findById(empleado.getId_empleado());
            if (existingEmpleado.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        System.out.println("Guardando desde service con id: " + empleado.getId_empleado());
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmpleado);
    }

    private void copyNonNullProperties(Empleado source, Empleado target) {
        Field[] fields = Empleado.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = ReflectionUtils.getField(field, source);
            if (value != null) {
                ReflectionUtils.setField(field, target, value);
            }
        }
    }

    public ResponseEntity<Empleado> updateEmpleado(Empleado empleado, Long id_empleado) {
        Optional<Empleado> data = empleadoRepository.findById(id_empleado);
        if (data.isPresent()) {
            Empleado existingEmpleado = data.get();
            copyNonNullProperties(empleado, existingEmpleado);

            if (empleado.getContrasena() != null) {
                existingEmpleado.setContrasena(passwordEncoder.encode(empleado.getContrasena()));
            }

            Empleado updatedEmpleado = empleadoRepository.save(existingEmpleado);
            return ResponseEntity.ok(updatedEmpleado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Boolean> deleteEmpleadoById(Long id_empleado) {
        try {
            empleadoRepository.deleteById(id_empleado);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
