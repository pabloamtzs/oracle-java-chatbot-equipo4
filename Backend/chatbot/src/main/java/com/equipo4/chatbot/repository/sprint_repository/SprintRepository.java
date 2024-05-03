package com.equipo4.chatbot.repository.sprint_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.equipo4.chatbot.model.sprint.Sprint;

@Repository
@Transactional
@EnableTransactionManagement
public interface SprintRepository extends JpaRepository<Sprint, Long> {

}