package com.equipo4.chatbot.controller.empleado_tarea_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.equipo4.chatbot.model.empleado_tarea.Empleado_Tarea;
import com.equipo4.chatbot.model.tarea.Tarea;
import com.equipo4.chatbot.service.empleado_tarea_service.Empleado_TareaService;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/empleado-tarea")
public class Empleado_TareaController {

    @Autowired
    private Empleado_TareaService empleado_TareaService;

    @GetMapping
    public ResponseEntity<List<Empleado_Tarea>> getEmpleadoTareaList() {
        return empleado_TareaService.getEmpleadoTareaList();
    }

    @GetMapping("/{idEmpleado}/{idTarea}")
    public ResponseEntity<Empleado_Tarea> getEmpleadoTareaById(@PathVariable Long idEmpleado, @PathVariable Long idTarea) {
        return empleado_TareaService.getEmpleadoTareaById(idEmpleado, idTarea);
    }

    @GetMapping("/tarea/{idEmpleado}")
    public ResponseEntity<List<Empleado_Tarea>> getTareaByEmpleadoId(@PathVariable Long idEmpleado) {
        return empleado_TareaService.getTareaByEmpleadoId(idEmpleado);
    }

    @GetMapping("/empleado/{idTarea}")
    public ResponseEntity<List<Empleado_Tarea>> getEmpleadoByTareaId(@PathVariable Long idEmpleado) {
        return empleado_TareaService.getEmpleadoByTareaId(idEmpleado);
    }

    @PostMapping("/{idEmpleado}/{idTarea}")
    public ResponseEntity<Empleado_Tarea> addEmpleadoTarea(@PathVariable Long idEmpleado, @PathVariable Long idTarea) {
        return empleado_TareaService.addEmpleadoTarea(idEmpleado, idTarea);
    }

    @PutMapping("/{idEmpleado}/{idTarea}")
    public ResponseEntity<Empleado_Tarea> updateEmpleadoTarea(@PathVariable Long idEmpleado, @PathVariable Long idTarea, @RequestBody Empleado_Tarea empleadoTarea) {
        return empleado_TareaService.updateEmpleadoTarea(idEmpleado, idTarea, empleadoTarea);
    }

    @DeleteMapping("/{idEmpleado}/{idTarea}")
    public ResponseEntity<Boolean> deleteEmpleadoTareaById(@PathVariable Long idEmpleado, @PathVariable Long idTarea) {
        return empleado_TareaService.deleteEmpleadoTareaById(idEmpleado, idTarea);
    }

    @GetMapping("/tareas/{idEmpleado}")
    public ResponseEntity<List<Tarea>> getAllTareaByEmpleadoId(@PathVariable Long idEmpleado) {
        return empleado_TareaService.getAllTareaByEmpleadoId(idEmpleado);
    }
    
}