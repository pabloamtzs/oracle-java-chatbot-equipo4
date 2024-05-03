package com.equipo4.chatbot.repository.tarea_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.equipo4.chatbot.model.tarea.Tarea;

@Repository
@Transactional
@EnableTransactionManagement
public interface TareaRepository extends JpaRepository<Tarea, Long> {

}