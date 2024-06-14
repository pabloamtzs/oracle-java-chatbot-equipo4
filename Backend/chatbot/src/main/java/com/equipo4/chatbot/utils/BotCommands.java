package com.equipo4.chatbot.utils;

public enum BotCommands{

  START_COMMAND("/start"),
  HIDE_COMMAND("/hide"),
  TASK_LIST_COMMAND("/tasklist"),
  ADD_TASK( "/addtask"),
  SELECT_TEAM_COMMAND("/equipo"),
  SELECT_PROJECT_COMMAND("/proyecto"),
  SELECT_SPRINT_COMMAND("/sprint");
  
  private String command;
  
  BotCommands(String command){
    this.command = command;
  }

  public String getCommand(){
    return command;
  }
}