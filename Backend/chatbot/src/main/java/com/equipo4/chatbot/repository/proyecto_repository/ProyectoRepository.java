package com.equipo4.chatbot.repository.proyecto_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.equipo4.chatbot.model.proyecto.Proyecto;

@Repository
@Transactional
@EnableTransactionManagement
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

}