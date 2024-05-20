package com.equipo4.chatbot.model.sprint;

import java.time.LocalTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "SPRINT")
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPR_SEQ")
    @SequenceGenerator(name = "SPR_SEQ", sequenceName = "SPR_SEQ", allocationSize = 1)
    @Column(name = "ID_SPRINT", updatable = false, nullable = false)
    private Long id_sprint;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "FECHA_INICIO")
    private LocalTime fecha_inicio;

    @Column(name = "FECHA_FINAL")
    private LocalTime fecha_final;

    @Column(name = "ID_PROYECTO")
    private Long id_proyecto;
}
