package com.equipo4.chatbot.repository.equipo_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.equipo4.chatbot.model.equipo.Equipo;

@Repository
@Transactional
@EnableTransactionManagement
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

}
