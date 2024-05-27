package com.equipo4.chatbot.controller.empleado_controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.equipo4.chatbot.model.empleado.Empleado;
import com.equipo4.chatbot.service.empleado_service.EmpleadoService;


@RestController
@RequestMapping("/api/empleado")
public class EmpleadoController {

    @Autowired 
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<Empleado>> getEmpleadoList() {
        return empleadoService.getEmpleadoList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> getEmpleadoById(@PathVariable long id) {
        return empleadoService.getEmpleadoById(id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Empleado> getEmpleadoByEmail(@PathVariable String email) {
        return empleadoService.getEmpleadoByEmail(email);
    }

    @PostMapping
    public ResponseEntity<Empleado> addEmpleado(@RequestBody Empleado empleado) {
        return empleadoService.addEmpleado(empleado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> updateEmpleado(@RequestBody Empleado empleado, @PathVariable long id) {
        return empleadoService.updateEmpleado(empleado, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmpleado(@PathVariable long id) {
        return empleadoService.deleteEmpleadoById(id);
    }


}