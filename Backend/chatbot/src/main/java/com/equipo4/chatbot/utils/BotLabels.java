package com.equipo4.chatbot.utils;

public enum BotLabels {

  SHOW_MAIN_SCREEN("Menu Principal"), 
  HIDE_MAIN_SCREEN("Esconder Menu Principal"),
  ADD_NEW_TASK("Agregar Tarea"),
  DONE("Hecho"),
  UNDO("Deshacer"),
  DELETE("Borrar"),
  MY_TASKS_LIST("Mi Lista de Tareas"),
  TEAM_TASKS_LIST("Lista de Tareas del Equipo"),
  DASH("-");

  private String label;

  BotLabels(String enumLabel) {
    this.label = enumLabel;
  }

  public String getLabel() {
    return label;
  }

}