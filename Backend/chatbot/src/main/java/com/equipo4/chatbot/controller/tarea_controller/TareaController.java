package com.equipo4.chatbot.controller.tarea_controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.equipo4.chatbot.model.tarea.Tarea;
import com.equipo4.chatbot.service.tarea_service.TareaService;


@RestController
@RequestMapping("/api/tarea")
public class TareaController {

    @Autowired 
    private TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<Tarea>> getTareaList() {
        return tareaService.getTareaList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> getTareaById(@PathVariable long id) {
        return tareaService.getTareaById(id);
    }

    @PostMapping
    public ResponseEntity<Tarea> addTarea(@RequestBody Tarea tarea) {
        return tareaService.addTarea(tarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> updateTarea(@RequestBody Tarea tarea, @PathVariable long id) {
        return tareaService.updateTarea(tarea, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTarea(@PathVariable long id) {
        return tareaService.deleteTareaById(id);
    }
}
