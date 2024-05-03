package com.equipo4.chatbot.service.miembro_equipo_service;

import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.equipo4.chatbot.model.miembro_equipo.ID_Miembro_Equipo;
import com.equipo4.chatbot.model.miembro_equipo.Miembro_Equipo;
import com.equipo4.chatbot.repository.miembro_equipo_repository.Miembro_EquipoRepository;

@Service
public class Miembro_EquipoService{

    @Autowired
    private Miembro_EquipoRepository miembro_EquipoRepository;

    public ResponseEntity<List<Miembro_Equipo>> getMiembroEquipoList() {
        List<Miembro_Equipo> miembros_Equipos = miembro_EquipoRepository.findAll();
        return ResponseEntity.ok(miembros_Equipos);
    }

    public ResponseEntity<Miembro_Equipo> getMiembroEquipoById(Long idMiembro, Long idEquipo) {
        Optional<Miembro_Equipo> data = miembro_EquipoRepository.findById(new ID_Miembro_Equipo(idMiembro, idEquipo));
        return data.map(miembro_Equipo -> ResponseEntity.ok(miembro_Equipo))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Miembro_Equipo>> getMiembroByEquipoId(Long idEquipo) {
        List<Miembro_Equipo> miembros = miembro_EquipoRepository.findByID_Equipo(idEquipo);
        if (!miembros.isEmpty()) {
            return ResponseEntity.ok(miembros);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<Miembro_Equipo>> getEquipoByMiembroId(Long idMiembro) {
        List<Miembro_Equipo> equipos = miembro_EquipoRepository.findByID_Miembro(idMiembro);
        if (!equipos.isEmpty()) {
            return ResponseEntity.ok(equipos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Miembro_Equipo> addMiembroEquipo(Long idMiembro, Long idEquipo) {
        ID_Miembro_Equipo miembroEquipoId = new ID_Miembro_Equipo(idMiembro, idEquipo);
        Miembro_Equipo miembro_Equipo = Miembro_Equipo.builder()
                .id(miembroEquipoId)
                .build();
        Miembro_Equipo savedMiembroEquipo = miembro_EquipoRepository.save(miembro_Equipo);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(savedMiembroEquipo);
    }

    public ResponseEntity<Miembro_Equipo> updateMiembroEquipo(Long idMiembro, Long idEquipo, Miembro_Equipo miembroEquipo) {
        Optional<Miembro_Equipo> data = miembro_EquipoRepository.findById(new ID_Miembro_Equipo(idMiembro, idEquipo));
        return data.map(_miembroEquipo -> {
            Miembro_Equipo updatedMiembroEquipo = miembro_EquipoRepository.save(_miembroEquipo);
            return ResponseEntity.ok(updatedMiembroEquipo);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Boolean> deleteMiembroEquipoById(Long idMiembro, Long idEquipo) {
        try {
            miembro_EquipoRepository.deleteById(new ID_Miembro_Equipo(idMiembro, idEquipo));
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}