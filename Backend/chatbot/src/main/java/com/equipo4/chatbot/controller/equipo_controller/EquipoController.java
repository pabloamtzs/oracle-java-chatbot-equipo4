package com.equipo4.chatbot.controller.equipo_controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.equipo4.chatbot.model.equipo.Equipo;
import com.equipo4.chatbot.service.equipo_service.EquipoService;


@RestController
@RequestMapping("/api/equipo")
public class EquipoController {

    @Autowired 
    private EquipoService equipoService;

    @GetMapping
    public ResponseEntity<List<Equipo>> getEquipoList() {
        return equipoService.getEquipoList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipo> getEquipoById(@PathVariable long id) {
        return equipoService.getEquipoById(id);
    }

    @PostMapping
    public ResponseEntity<Equipo> addEquipo(@RequestBody Equipo equipo) {
        return equipoService.addEquipo(equipo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipo> updateEquipo(@RequestBody Equipo equipo, @PathVariable long id) {
        return equipoService.updateEquipo(equipo, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEquipo(@PathVariable long id) {
        return equipoService.deleteEquipoById(id);
    }
}
