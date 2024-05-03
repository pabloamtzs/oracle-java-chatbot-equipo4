package com.equipo4.chatbot.model.empleado_tarea;

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
@Table(name = "EMPLEADO_TAREA")
public class Empleado_Tarea {
    @EmbeddedId
    private ID_Empleado_Tarea id;
}

