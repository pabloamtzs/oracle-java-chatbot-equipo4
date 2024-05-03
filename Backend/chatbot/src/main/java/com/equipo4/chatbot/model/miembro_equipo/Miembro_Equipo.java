package com.equipo4.chatbot.model.miembro_equipo;

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
@Table(name = "MIEMBRO_EQUIPO")
public class Miembro_Equipo {
    @EmbeddedId
    private ID_Miembro_Equipo id;
}
