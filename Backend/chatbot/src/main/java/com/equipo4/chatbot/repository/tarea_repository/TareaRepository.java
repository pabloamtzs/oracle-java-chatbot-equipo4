package com.equipo4.chatbot.repository.tarea_repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.equipo4.chatbot.model.tarea.Tarea;

@Repository
@Transactional
@EnableTransactionManagement
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    @Query("SELECT t FROM Miembro_Equipo m LEFT JOIN Empleado_Tarea e ON m.id.id_miembro = e.id.id_empleado LEFT JOIN Tarea t ON e.id.id_tarea = t.id_tarea WHERE m.id.id_equipo = :ID_Equipo")
    List<Tarea> findById_AllTareasEquipo(Long ID_Equipo);
}