package com.equipo4.chatbot.repository.empleado_repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.equipo4.chatbot.model.empleado.Empleado;

@Repository
@Transactional
@EnableTransactionManagement
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    @Query("SELECT m FROM Empleado m WHERE m.email = :Email")
    Optional<Empleado> findByEmail(String Email);
     
}