package com.equipo4.chatbot.service.equipo_service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.equipo4.chatbot.model.equipo.Equipo;
import com.equipo4.chatbot.repository.equipo_repository.EquipoRepository;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    public ResponseEntity<List<Equipo>> getEquipoList() {
        List<Equipo> equipos = equipoRepository.findAll();
        return ResponseEntity.ok(equipos);
    }

    public ResponseEntity<Equipo> getEquipoById(long id) {
        Optional<Equipo> data = equipoRepository.findById(id);
        return data.map(equipo -> ResponseEntity.ok(equipo))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Equipo> addEquipo(Equipo equipo) {
        Equipo savedEquipo = equipoRepository.save(equipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipo);
    }

    public ResponseEntity<Equipo> updateEquipo(Equipo equipo, Long id_equipo) {
        Optional<Equipo> data = equipoRepository.findById(id_equipo);
        return data.map(_equipo -> {
            _equipo.setNombre_Equipo(equipo.getNombre_Equipo());
            _equipo.setManager(equipo.getManager());
            Equipo updatedEquipo = equipoRepository.save(_equipo);
            return ResponseEntity.ok(updatedEquipo);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    public ResponseEntity<Boolean> deleteEquipoById(Long id_equipo) {
        try {
            equipoRepository.deleteById(id_equipo);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}