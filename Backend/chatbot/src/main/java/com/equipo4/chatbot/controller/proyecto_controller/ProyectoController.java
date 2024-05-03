package com.equipo4.chatbot.controller.proyecto_controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.equipo4.chatbot.model.proyecto.Proyecto;
import com.equipo4.chatbot.service.proyecto_service.ProyectoService;


@RestController
@RequestMapping("/api/proyecto")
public class ProyectoController {

    @Autowired 
    private ProyectoService proyectoService;

    @GetMapping
    public ResponseEntity<List<Proyecto>> getProyectoList() {
        return proyectoService.getProyectoList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> getProyectoById(@PathVariable long id) {
        return proyectoService.getProyectoById(id);
    }

    @PostMapping
    public ResponseEntity<Proyecto> addProyecto(@RequestBody Proyecto proyecto) {
        return proyectoService.addProyecto(proyecto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> updateProyecto(@RequestBody Proyecto proyecto, @PathVariable long id) {
        return proyectoService.updateProyecto(proyecto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProyecto(@PathVariable long id) {
        return proyectoService.deleteProyectoById(id);
    }
}
