package com.equipo4.chatbot.model.proyecto;

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
@Table(name = "PROYECTO")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PR_SEQ")
    @SequenceGenerator(name = "PR_SEQ", sequenceName = "PR_SEQ", allocationSize = 1)
    @Column(name = "ID_PROYECTO", updatable = false, nullable = false)
    private Long id_proyecto;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "ID_EQUIPO")
    private Long id_equipo;
}
