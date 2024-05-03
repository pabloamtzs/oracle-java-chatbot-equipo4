package com.equipo4.chatbot.model.equipo;

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
@Table(name = "EQUIPO")

public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_EQUIPO", updatable = false, nullable = false)
    private Long ID_Equipo;

    @Column(name = "NOMBRE_EQUIPO")
    private String Nombre_Equipo;

    @Column(name = "ID_LIDER")
    private Long manager;

}
