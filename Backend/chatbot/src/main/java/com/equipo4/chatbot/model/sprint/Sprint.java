package com.equipo4.chatbot.model.sprint;

import java.time.LocalTime;
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
@Table(name = "SPRINT")
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_SPRINT", updatable = false, nullable = false)
    private Long ID_Sprint;

    @Column(name = "NOMBRE")
    private String Nombre;

    @Column(name = "FECHA_INICIO")
    private LocalTime Hora_Entrada;

    @Column(name = "FECHA_FINAL")
    private LocalTime Hora_Salida;

    @Column(name = "ID_PROYECTO")
    private Long proyecto;
}
