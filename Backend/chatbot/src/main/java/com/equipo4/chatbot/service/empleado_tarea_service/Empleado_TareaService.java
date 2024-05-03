package com.equipo4.chatbot.service.empleado_tarea_service;

import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.equipo4.chatbot.model.empleado_tarea.Empleado_Tarea;
import com.equipo4.chatbot.model.empleado_tarea.ID_Empleado_Tarea;
import com.equipo4.chatbot.repository.empleado_tarea_repository.Empleado_TareaRepository;

@Service
public class Empleado_TareaService{

    @Autowired
    private Empleado_TareaRepository empleado_TareaRepository;

    public ResponseEntity<List<Empleado_Tarea>> getEmpleadoTareaList() {
        List<Empleado_Tarea> empleados_Tareas = empleado_TareaRepository.findAll();
        return ResponseEntity.ok(empleados_Tareas);
    }

    public ResponseEntity<Empleado_Tarea> getEmpleadoTareaById(Long idEmpleado, Long idTarea) {
        Optional<Empleado_Tarea> data = empleado_TareaRepository.findById(new ID_Empleado_Tarea(idEmpleado, idTarea));
        return data.map(empleado_tarea -> ResponseEntity.ok(empleado_tarea))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Empleado_Tarea>> getEmpleadoByTareaId(Long idTarea) {
        List<Empleado_Tarea> empleados = empleado_TareaRepository.findByID_Tarea(idTarea);
        if (!empleados.isEmpty()) {
            return ResponseEntity.ok(empleados);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<Empleado_Tarea>> getTareaByEmpleadoId(Long idEmpleado) {
        List<Empleado_Tarea> tareas = empleado_TareaRepository.findByID_Empleado(idEmpleado);
        if (!tareas.isEmpty()) {
            return ResponseEntity.ok(tareas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Empleado_Tarea> addEmpleadoTarea(Long idEmpleado, Long idTarea) {
        ID_Empleado_Tarea empleadoTareaId = new ID_Empleado_Tarea(idEmpleado, idTarea);
        Empleado_Tarea empleado_Tarea = Empleado_Tarea.builder()
                .id(empleadoTareaId)
                .build();
        Empleado_Tarea savedEmpleadoTarea = empleado_TareaRepository.save(empleado_Tarea);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(savedEmpleadoTarea);
    }

    public ResponseEntity<Empleado_Tarea> updateEmpleadoTarea(Long idEmpleado, Long idTarea, Empleado_Tarea empleadoTarea) {
        Optional<Empleado_Tarea> data = empleado_TareaRepository.findById(new ID_Empleado_Tarea(idEmpleado, idTarea));
        return data.map(_empleadoTarea -> {
            Empleado_Tarea updatedEmpleadoTarea = empleado_TareaRepository.save(_empleadoTarea);
            return ResponseEntity.ok(updatedEmpleadoTarea);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Boolean> deleteEmpleadoTareaById(Long idEmpleado, Long idTarea) {
        try {
            empleado_TareaRepository.deleteById(new ID_Empleado_Tarea(idEmpleado, idTarea));
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
