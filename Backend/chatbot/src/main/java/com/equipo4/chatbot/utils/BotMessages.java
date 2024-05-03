package com.equipo4.chatbot.utils;

public enum BotMessages {

  HELLO_MYTODO_BOT(
  "¡Hola! Soy el Telegram Bot del Equipo 4. Escribe un nuevo ítem de tarea abajo y presiona el botón de enviar (flecha azul), o selecciona una opción abajo:"),
  
  BOT_REGISTERED_STARTED("¡El bot se registró y se inició con éxito!"),
  
  TASK_DONE("¡Ítem completado! Selecciona /todolist para volver a la lista de ítems pendientes, o /start para ir a la pantalla principal."), 
  
  TASK_UNDONE("¡Ítem deshecho! Selecciona /todolist para volver a la lista de ítems pendientes, o /start para ir a la pantalla principal."), 
  
  TASK_DELETED("¡Ítem eliminado! Selecciona /todolist para volver a la lista de ítems pendientes, o /start para ir a la pantalla principal."),
  
  TYPE_NEW_TASK("Escribe un nuevo ítem de tarea abajo y presiona el botón de enviar (flecha azul) a la derecha."),
  
  NEW_TASK_ADDED("¡Nuevo ítem añadido! Selecciona /todolist para volver a la lista de ítems pendientes, o /start para ir a la pantalla principal."),
  
  BYE("¡Adiós! Selecciona /start para continuar.");

  private String message;

  BotMessages(String enumMessage) {
    this.message = enumMessage;
  }

  public String getMessage() {
    return message;
  }

}