package com.equipo4.chatbot.model.tarea;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TAREA")
  
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TAREA", updatable = false, nullable = false)
    private Long id_tarea;

    @Column(name = "DESCRIPCION_TAREA")
    private String descripcion_tarea;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "ID_SPRINT")
    private Long id_sprint;

}