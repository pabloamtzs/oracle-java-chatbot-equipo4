package com.equipo4.chatbot.model;

import java.math.BigDecimal;
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
@Table(name = "EMPLEADO")
public class Empleado {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_EMPLEADO", updatable = false, nullable = false)
    private Long ID_Empleado;

    @Column(name = "NOMBRE")
    private String Nombre;

    @Column(name = "APELLIDO")
    private String Apellido;

    @Column(name = "POSICION")
    private String Posicion;

    @Column(name = "EMAIL")
    private String Email;

    @Column(name = "CONTRASENA")
    private String Contrasena;

    @Column(name = "HORA_ENTRADA")
    private LocalTime Hora_Entrada;

    @Column(name = "HORA_SALIDA")
    private LocalTime Hora_Salida;

    @Column(name = "SALARIO")
    private BigDecimal Salario;
}
