package com.equipo4.chatbot.model.empleado_tarea;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ID_Empleado_Tarea implements Serializable {
    
    @Column(name = "ID_EMPLEADO", updatable = false, nullable = false)
    private Long ID_Empleado;

    @Column(name = "ID_TAREA", updatable = false, nullable = false)
    private Long ID_Tarea;
}
