package com.equipo4.chatbot.repository.sprint_repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.equipo4.chatbot.model.sprint.Sprint;

@Repository
@Transactional
@EnableTransactionManagement
public interface SprintRepository extends JpaRepository<Sprint, Long> {

    @Query("SELECT m FROM Sprint m WHERE m.id_proyecto = :id_proyecto")
    List<Sprint> findByProyectoId(long id_proyecto);

}