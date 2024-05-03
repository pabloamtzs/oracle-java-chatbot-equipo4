package com.equipo4.chatbot.controller.sprint_controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.equipo4.chatbot.model.sprint.Sprint;
import com.equipo4.chatbot.service.sprint_service.SprintService;


@RestController
@RequestMapping("/api/sprint")
public class SprintController {

    @Autowired 
    private SprintService sprintService;

    @GetMapping
    public ResponseEntity<List<Sprint>> getSprintList() {
        return sprintService.getSprintList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sprint> getSprintById(@PathVariable long id) {
        return sprintService.getSprintById(id);
    }

    @PostMapping
    public ResponseEntity<Sprint> addSprint(@RequestBody Sprint sprint) {
        return sprintService.addSprint(sprint);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sprint> updateSprint(@RequestBody Sprint sprint, @PathVariable long id) {
        return sprintService.updateSprint(sprint, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteSprint(@PathVariable long id) {
        return sprintService.deleteSprintById(id);
    }
}