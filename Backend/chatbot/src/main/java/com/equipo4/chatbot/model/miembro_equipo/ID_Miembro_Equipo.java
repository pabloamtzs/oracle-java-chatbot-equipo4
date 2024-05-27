package com.equipo4.chatbot.model.miembro_equipo;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ID_Miembro_Equipo implements Serializable {
    
    @Column(name = "ID_MIEMBRO", updatable = false, nullable = false)
    private Long id_miembro;

    @Column(name = "ID_EQUIPO", updatable = false, nullable = false)
    private Long id_equipo;
}
