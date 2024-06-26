package com.equipo4.chatbot.model.tarea;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TAREA")
  
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRA_SEQ")
    @SequenceGenerator(name = "TRA_SEQ", sequenceName = "TRA_SEQ", allocationSize = 1)
    @Column(name = "ID_TAREA", updatable = false, nullable = false)
    private Long id_tarea;

    @Column(name = "DESCRIPCION_TAREA")
    private String descripcion_tarea;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "ID_SPRINT")
    private Long id_sprint;

    @Column(name = "FECHA_CREACION")
    private Timestamp fecha_creacion;

    @Column(name = "FECHA_MODIFICACION")
    private Timestamp fecha_modificacion;

    @Column(name = "FECHA_TERMINADA")
    private Timestamp fecha_terminada;

    @Column(name = "NOMBRE_TAREA")
    private String nombre_tarea;

    public boolean isDone() {
        return estado.equals("Hecho");
    }
}