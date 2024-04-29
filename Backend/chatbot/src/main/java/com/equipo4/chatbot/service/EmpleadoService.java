package com.equipo4.chatbot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.equipo4.chatbot.model.Empleado;
import com.equipo4.chatbot.repository.EmpleadoRepository;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public ResponseEntity<List<Empleado>> getEmpleadoList() {
        List<Empleado> empleados = empleadoRepository.findAll();
        return ResponseEntity.ok(empleados);
    }

    public ResponseEntity<Empleado> getEmpleadoById(long id) {
        Optional<Empleado> data = empleadoRepository.findById(id);
        return data.map(empleado -> ResponseEntity.ok(empleado))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Empleado> addEmpleado(Empleado empleado) {
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmpleado);
    }

    public ResponseEntity<Empleado> updateEmpleado(Empleado empleado, Long id_empleado) {
        Optional<Empleado> data = empleadoRepository.findById(id_empleado);
        return data.map(_empleado -> {
            _empleado.setNombre(empleado.getNombre());
            _empleado.setApellido(empleado.getApellido());
            _empleado.setPosicion(empleado.getPosicion());
            _empleado.setEmail(empleado.getEmail());
            _empleado.setContrasena(empleado.getContrasena());
            _empleado.setHora_Entrada(empleado.getHora_Entrada());
            _empleado.setHora_Salida(empleado.getHora_Salida());
            _empleado.setSalario(empleado.getSalario());
            Empleado updatedEmpleado = empleadoRepository.save(_empleado);
            return ResponseEntity.ok(updatedEmpleado);
        }).orElseGet(() -> ResponseEntity.notFound().build());
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
