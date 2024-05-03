package com.equipo4.chatbot.service.sprint_service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.equipo4.chatbot.model.sprint.Sprint;
import com.equipo4.chatbot.repository.sprint_repository.SprintRepository;

@Service
public class SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    public ResponseEntity<List<Sprint>> getSprintList() {
        List<Sprint> sprints = sprintRepository.findAll();
        return ResponseEntity.ok(sprints);
    }

    public ResponseEntity<Sprint> getSprintById(long id) {
        Optional<Sprint> data = sprintRepository.findById(id);
        return data.map(sprint -> ResponseEntity.ok(sprint))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Sprint> addSprint(Sprint sprint) {
        Sprint savedSprint = sprintRepository.save(sprint);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSprint);
    }

    public ResponseEntity<Sprint> updateSprint(Sprint sprint, Long id_Sprint) {
        Optional<Sprint> data = sprintRepository.findById(id_Sprint);
        return data.map(_sprint -> {
            _sprint.setNombre(sprint.getNombre());
            _sprint.setHora_Entrada(sprint.getHora_Entrada());
            _sprint.setHora_Salida(sprint.getHora_Salida());
            _sprint.setProyecto(sprint.getProyecto());
            Sprint updatedSprint = sprintRepository.save(_sprint);
            return ResponseEntity.ok(updatedSprint);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Boolean> deleteSprintById(Long id_sprint) {
        try {
            sprintRepository.deleteById(id_sprint);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}