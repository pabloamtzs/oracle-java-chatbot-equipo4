package com.equipo4.chatbot.service.proyecto_service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.equipo4.chatbot.model.proyecto.Proyecto;
import com.equipo4.chatbot.repository.proyecto_repository.ProyectoRepository;

@Service
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    public ResponseEntity<List<Proyecto>> getProyectoList() {
        List<Proyecto> proyecto = proyectoRepository.findAll();
        return ResponseEntity.ok(proyecto);
    }

    public ResponseEntity<Proyecto> getProyectoById(long id) {
        Optional<Proyecto> data = proyectoRepository.findById(id);
        return data.map(proyecto -> ResponseEntity.ok(proyecto))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Proyecto>> getProyectosByEquipoId(long equipoId) {
        List<Proyecto> proyectos = proyectoRepository.findByEquipoId(equipoId);
        return ResponseEntity.ok(proyectos);
    }

    public ResponseEntity<Proyecto> addProyecto(Proyecto proyecto) {
        Proyecto savedProyecto = proyectoRepository.save(proyecto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProyecto);
    }

    public ResponseEntity<Proyecto> updateProyecto(Proyecto proyecto, Long id_Proyecto) {
        Optional<Proyecto> data = proyectoRepository.findById(id_Proyecto);
        return data.map(_proyecto -> {
            _proyecto.setNombre(proyecto.getNombre());
            _proyecto.setId_equipo(proyecto.getId_equipo());
            Proyecto updatedProyecto = proyectoRepository.save(_proyecto);
            return ResponseEntity.ok(updatedProyecto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Boolean> deleteProyectoById(Long id_proyecto) {
        try {
            proyectoRepository.deleteById(id_proyecto);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
