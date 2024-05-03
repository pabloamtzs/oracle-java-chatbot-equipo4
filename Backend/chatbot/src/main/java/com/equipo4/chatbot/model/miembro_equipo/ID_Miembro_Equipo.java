package com.equipo4.chatbot.model.miembro_equipo;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ID_Miembro_Equipo implements Serializable {
    
    @Column(name = "ID_MIEMBRO", updatable = false, nullable = false)
    private Long ID_Miembro;

    @Column(name = "ID_EQUIPO", updatable = false, nullable = false)
    private Long ID_Equipo;
}
