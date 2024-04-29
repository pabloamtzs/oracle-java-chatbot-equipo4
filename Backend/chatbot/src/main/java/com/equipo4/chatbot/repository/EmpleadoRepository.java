package com.equipo4.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.equipo4.chatbot.model.Empleado;

@Repository
@Transactional
@EnableTransactionManagement
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
     
}