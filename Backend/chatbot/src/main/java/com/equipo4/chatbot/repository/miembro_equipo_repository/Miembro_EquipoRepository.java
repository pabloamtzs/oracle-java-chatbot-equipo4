package com.equipo4.chatbot.repository.miembro_equipo_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equipo4.chatbot.model.miembro_equipo.ID_Miembro_Equipo;
import com.equipo4.chatbot.model.miembro_equipo.Miembro_Equipo;

import java.util.List;

@Repository
public interface Miembro_EquipoRepository extends JpaRepository<Miembro_Equipo, ID_Miembro_Equipo> {
    
    @Query("SELECT m FROM Miembro_Equipo m WHERE m.id.id_equipo = :ID_Equipo")
    List<Miembro_Equipo> findByID_Equipo(Long ID_Equipo);
    @Query("SELECT m FROM Miembro_Equipo m WHERE m.id.id_miembro = :ID_Miembro")
    List<Miembro_Equipo> findByID_Miembro(Long ID_Miembro);
}
