package com.equipo4.chatbot.model.tarea;

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
@Table(name = "TAREA")
  
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_TAREA", updatable = false, nullable = false)
    private Long ID_Tarea;

    @Column(name = "DESCRIPCION_TAREA")
    private String Descripcion_Tarea;

    @Column(name = "ESTADO")
    private String Estado;

    @Column(name = "ID_SPRINT")
    private Long sprint;
    
    public Long getID() {
        return ID_Tarea;
    }

    public void setID(Long ID) {
        this.ID_Tarea = ID;
    }

    public String getDescription() {
        return Descripcion_Tarea;
    }

    public void setDescription(String Descripcion_Tarea) {
        this.Descripcion_Tarea = Descripcion_Tarea;
    }

    public String getEstado() {
        return Estado;
    }

    public void setDone(String Estado) {
        this.Estado = Estado;
    }
    
    public boolean isDone() {
        return Estado.equals("DONE");
    }

}