package com.equipo4.chatbot.model.equipo;

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
@Table(name = "EQUIPO")

public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EQ_SEQ")
    @SequenceGenerator(name = "EQ_SEQ", sequenceName = "EQ_SEQ", allocationSize = 1)
    @Column(name = "ID_EQUIPO", updatable = false, nullable = false)
    private Long id_equipo;

    @Column(name = "NOMBRE_EQUIPO")
    private String nombre_equipo;

    @Column(name = "ID_LIDER")
    private Long id_lider;

}
