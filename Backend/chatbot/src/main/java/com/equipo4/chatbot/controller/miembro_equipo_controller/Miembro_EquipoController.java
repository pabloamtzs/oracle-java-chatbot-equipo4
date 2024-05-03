package com.equipo4.chatbot.controller.miembro_equipo_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.equipo4.chatbot.model.miembro_equipo.Miembro_Equipo;
import com.equipo4.chatbot.service.miembro_equipo_service.Miembro_EquipoService;

import java.util.List;

@RestController
@RequestMapping("/api/miembro-equipo")
public class Miembro_EquipoController {

    @Autowired
    private Miembro_EquipoService miembro_EquipoService;

    @GetMapping
    public ResponseEntity<List<Miembro_Equipo>> getMiembroEquipoList() {
        return miembro_EquipoService.getMiembroEquipoList();
    }

    @GetMapping("/{idMiembro}/{idEquipo}")
    public ResponseEntity<Miembro_Equipo> getMiembroEquipoById(@PathVariable Long idMiembro, @PathVariable Long idEquipo) {
        return miembro_EquipoService.getMiembroEquipoById(idMiembro, idEquipo);
    }

    @GetMapping("/miembro/{idEquipo}")
    public ResponseEntity<List<Miembro_Equipo>> getMiembroByEquipoId(@PathVariable Long idEquipo) {
        return miembro_EquipoService.getMiembroByEquipoId(idEquipo);
    }

    @GetMapping("/equipo/{idMiembro}")
    public ResponseEntity<List<Miembro_Equipo>> getEquipoByMiembroId(@PathVariable Long idMiembro) {
        return miembro_EquipoService.getEquipoByMiembroId(idMiembro);
    }

    @PostMapping("/{idMiembro}/{idEquipo}")
    public ResponseEntity<Miembro_Equipo> addMiembroEquipo(@PathVariable Long idMiembro, @PathVariable Long idEquipo) {
        return miembro_EquipoService.addMiembroEquipo(idMiembro, idEquipo);
    }

    @PutMapping("/{idMiembro}/{idEquipo}")
    public ResponseEntity<Miembro_Equipo> updateMiembroEquipo(@PathVariable Long idMiembro, @PathVariable Long idEquipo, @RequestBody Miembro_Equipo miembroEquipo) {
        return miembro_EquipoService.updateMiembroEquipo(idMiembro, idEquipo, miembroEquipo);
    }

    @DeleteMapping("/{idMiembro}/{idEquipo}")
    public ResponseEntity<Boolean> deleteMiembroEquipo(@PathVariable Long idMiembro, @PathVariable Long idEquipo) {
        return miembro_EquipoService.deleteMiembroEquipoById(idMiembro, idEquipo);
    }
}