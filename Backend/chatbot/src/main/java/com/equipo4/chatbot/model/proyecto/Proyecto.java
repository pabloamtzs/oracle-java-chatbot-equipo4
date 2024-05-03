package com.equipo4.chatbot.model.proyecto;

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
@Table(name = "PROYECTO")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PROYECTO", updatable = false, nullable = false)
    private Long ID_Proyecto;

    @Column(name = "NOMBRE")
    private String Nombre;

    @Column(name = "ID_EQUIPO")
    private Long equipo;
}
