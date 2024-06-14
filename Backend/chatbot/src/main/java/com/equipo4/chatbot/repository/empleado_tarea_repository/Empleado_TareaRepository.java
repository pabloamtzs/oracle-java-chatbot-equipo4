package com.equipo4.chatbot.repository.empleado_tarea_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equipo4.chatbot.model.empleado_tarea.Empleado_Tarea;
import com.equipo4.chatbot.model.empleado_tarea.ID_Empleado_Tarea;
import com.equipo4.chatbot.model.tarea.Tarea;

import java.util.List;

@Repository
public interface Empleado_TareaRepository extends JpaRepository<Empleado_Tarea, ID_Empleado_Tarea> {
    
    @Query("SELECT m FROM Empleado_Tarea m WHERE m.id.id_empleado = :ID_Empleado")
    List<Empleado_Tarea> findByID_Empleado(Long ID_Empleado);
    @Query("SELECT m FROM Empleado_Tarea m WHERE m.id.id_tarea = :ID_Tarea")
    List<Empleado_Tarea> findByID_Tarea(Long ID_Tarea);
    @Query("SELECT t FROM Empleado_Tarea m JOIN Tarea t ON m.id.id_tarea = t.id_tarea WHERE m.id.id_empleado = :ID_Empleado")
    List<Tarea> findById_AllTareas(Long ID_Empleado);
}
