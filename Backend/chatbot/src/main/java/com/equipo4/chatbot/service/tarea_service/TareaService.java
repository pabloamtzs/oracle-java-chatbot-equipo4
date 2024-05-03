package com.equipo4.chatbot.service.tarea_service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.equipo4.chatbot.model.tarea.Tarea;
import com.equipo4.chatbot.repository.tarea_repository.TareaRepository;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    public ResponseEntity<List<Tarea>> getTareaList() {
        List<Tarea> tarea = tareaRepository.findAll();
        return ResponseEntity.ok(tarea);
    }

    public ResponseEntity<Tarea> getTareaById(long id) {
        Optional<Tarea> data = tareaRepository.findById(id);
        return data.map(tarea -> ResponseEntity.ok(tarea))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Tarea> addTarea(Tarea tarea) {
        tarea.setEstado("Pendiente"); // Por defecto, el estado de la tarea es Pendiente, ya que no se especifica en el formulario de creación de tareas
        Tarea savedTarea = tareaRepository.save(tarea);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTarea);
    }

    public ResponseEntity<Tarea> updateTarea(Tarea tarea, Long id_Tarea) {
        Optional<Tarea> data = tareaRepository.findById(id_Tarea);
        return data.map(_tarea -> {
            _tarea.setDescripcion_Tarea(tarea.getDescripcion_Tarea());
            _tarea.setEstado(tarea.getEstado());
            _tarea.setSprint(tarea.getSprint());
            Tarea updatedTarea = tareaRepository.save(_tarea);
            return ResponseEntity.ok(updatedTarea);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //xd
    public ResponseEntity<Tarea> updateTarea(Long id_Tarea, Tarea tarea) {
        Optional<Tarea> data = tareaRepository.findById(id_Tarea);
        return data.map(_tarea -> {
            _tarea.setDescripcion_Tarea(tarea.getDescripcion_Tarea());
            _tarea.setEstado(tarea.getEstado());
            _tarea.setSprint(tarea.getSprint());
            Tarea updatedTarea = tareaRepository.save(_tarea);
            return ResponseEntity.ok(updatedTarea);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    public boolean deleteTareaById(long id){
        try{
            tareaRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
