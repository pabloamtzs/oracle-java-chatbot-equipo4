package com.equipo4.chatbot.model.empleado;

import java.math.BigDecimal;
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
@Table(name = "EMPLEADO")
public class Empleado {
 
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMP_SEQ")
    @SequenceGenerator(name = "EMP_SEQ", sequenceName = "EMP_SEQ", allocationSize = 1)
    @Column(name = "ID_EMPLEADO", updatable = false, nullable = false)
    private Long id_empleado;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "POSICION")
    private String posicion;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CONTRASENA")
    private String contrasena;

    @Column(name = "HORA_ENTRADA")
    private LocalTime hora_entrada;

    @Column(name = "HORA_SALIDA")
    private LocalTime hora_salida;

    @Column(name = "SALARIO")
    private BigDecimal salario;
}