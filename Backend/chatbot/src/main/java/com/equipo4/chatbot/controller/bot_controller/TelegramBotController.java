package com.equipo4.chatbot.controller.bot_controller;
import com.equipo4.chatbot.utils.BotCommands;
import com.equipo4.chatbot.utils.BotLabels;
import com.equipo4.chatbot.utils.BotMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import com.equipo4.chatbot.service.authentication_service.AuthenticationService;
import com.equipo4.chatbot.service.empleado_service.EmpleadoService;
import com.equipo4.chatbot.service.empleado_tarea_service.Empleado_TareaService;
import com.equipo4.chatbot.service.equipo_service.EquipoService;
import com.equipo4.chatbot.service.miembro_equipo_service.Miembro_EquipoService;
import com.equipo4.chatbot.service.proyecto_service.ProyectoService;
import com.equipo4.chatbot.service.sprint_service.SprintService;
import com.equipo4.chatbot.service.tarea_service.TareaService;
import com.equipo4.chatbot.utils.BotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.equipo4.chatbot.controller.auth_controller.LoginRequest;
import com.equipo4.chatbot.model.authentication_response.AuthenticationResponse;
import com.equipo4.chatbot.model.empleado.Empleado;
import com.equipo4.chatbot.model.equipo.Equipo;
import com.equipo4.chatbot.model.miembro_equipo.Miembro_Equipo;
import com.equipo4.chatbot.model.proyecto.Proyecto;
import com.equipo4.chatbot.model.sprint.Sprint;
import com.equipo4.chatbot.model.tarea.Tarea;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

public class TelegramBotController extends TelegramLongPollingBot {

  private static final Logger logger = LoggerFactory.getLogger(TelegramBotController.class);
  private TareaService tareaService;
  private AuthenticationService authenticationService;
  private EmpleadoService empleadoService;
  private String nombreBot;
  private EquipoService equipoService;
  private Miembro_EquipoService miembro_EquipoService;
  private ProyectoService proyectoService;
  private SprintService sprintService;
  private Empleado_TareaService empleado_TareaService;
  private Map<Long, String> userAuthStep = new HashMap<>();
  private Map<Long, String> userEmail = new HashMap<>();
  private Map<Long, String> authenticatedUsers = new HashMap<>();
  private Map<Long, Long> authenticatedUserIds = new HashMap<>();
  private Map<Long, Long> selectedTeams = new HashMap<>();
  private Map<Long, Long> selectedProjects = new HashMap<>();
  private Map<Long, Long> selectedSprints = new HashMap<>();
  private Map<Long, String> selecting = new HashMap<>();



  
  public TelegramBotController(String botToken, String nombreBot, TareaService tareaService, 
  AuthenticationService authenticationService, EmpleadoService empleadoService, EquipoService equipoService, 
  Miembro_EquipoService miembro_EquipoService, ProyectoService proyectoService, SprintService sprintService,
  Empleado_TareaService empleado_TareaService) {
    super(botToken);
    this.tareaService = tareaService;
    this.authenticationService = authenticationService;
    this.empleadoService = empleadoService;
    this.equipoService = equipoService;
    this.miembro_EquipoService = miembro_EquipoService;
    this.proyectoService = proyectoService;
    this.sprintService = sprintService;
    this.nombreBot = nombreBot;
}

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String messageTextFromTelegram = update.getMessage().getText();
      long chatId = update.getMessage().getChatId();

      if (!authenticatedUsers.containsKey(chatId)) {
          handleAuthentication(messageTextFromTelegram, chatId);
          return;
      }

      String role = authenticatedUsers.get(chatId);

      if (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())
                    || messageTextFromTelegram.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel())) {
                showMainMenu(chatId, role);
          } // IF MESSAGE == START || SHOW_MAIN_SCREEN
      else if (messageTextFromTelegram.equals(BotCommands.SELECT_TEAM_COMMAND.getCommand())
      || messageTextFromTelegram.equals(BotLabels.SELECT_TEAM.getLabel())) {
          showTeamSelection(chatId);
          selecting.put(chatId, "selectingTeam");  // Indicar que el usuario está seleccionando un equipo
        } 
      else if (messageTextFromTelegram.equals(BotCommands.SELECT_PROJECT_COMMAND.getCommand())
            || messageTextFromTelegram.equals(BotLabels.SELECT_PROJECT.getLabel())) {
                Long selectedTeamId = selectedTeams.get(chatId);
                if (selectedTeamId == null) {
                    BotHelper.sendMessageToTelegram(chatId, "Por favor selecciona un equipo primero usando /equipo.", this);
                    return;
                }
                showProjectSelection(chatId, selectedTeamId);
                selecting.put(chatId, "selectingProject");  // Indicar que el usuario está seleccionando un proyecto
              } 
      else if (messageTextFromTelegram.equals(BotCommands.SELECT_SPRINT_COMMAND.getCommand())
            || messageTextFromTelegram.equals(BotLabels.SELECT_SPRINT.getLabel())) {
                Long selectedProjectId = selectedProjects.get(chatId);
                if (selectedProjectId == null) {
                    BotHelper.sendMessageToTelegram(chatId, "Por favor selecciona un proyecto primero usando /proyecto.", this);
                    return;
                }
                showSprintSelection(chatId, selectedProjectId);
                selecting.put(chatId, "selectingSprint");  // Indicar que el usuario está seleccionando un sprint
              } 
      else if ("selectingTeam".equals(selecting.get(chatId))) {
                handleTeamSelection(chatId, messageTextFromTelegram);
                selecting.remove(chatId);  // Indicar que el usuario ha terminado de seleccionar
              } 
      else if ("selectingProject".equals(selecting.get(chatId))) {
                handleProjectSelection(chatId, messageTextFromTelegram);
                selecting.remove(chatId);  // Indicar que el usuario ha terminado de seleccionar
              } 
      else if ("selectingSprint".equals(selecting.get(chatId))) {
                handleSprintSelection(chatId, messageTextFromTelegram);
                selecting.remove(chatId);  // Indicar que el usuario ha terminado de seleccionar
              }
      else if (messageTextFromTelegram.indexOf(BotLabels.DONE.getLabel()) != -1) {
          String done = messageTextFromTelegram.substring(0, messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
          Integer id = Integer.valueOf(done);

          try {
              Tarea tarea = getTareaById(id).getBody();
              tarea.setEstado("Hecho");
              updateTarea(tarea, id);
              BotHelper.sendMessageToTelegram(chatId, BotMessages.TASK_DONE.getMessage(), this);
              } catch (Exception e) {
                  logger.error(e.getLocalizedMessage(), e);
              }
        } // TASK_DONE
      else if (messageTextFromTelegram.indexOf(BotLabels.UNDO.getLabel()) != -1) {
          String undo = messageTextFromTelegram.substring(0, messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
          Integer id = Integer.valueOf(undo);

          try {
              Tarea tarea = getTareaById(id).getBody();
              tarea.setEstado("Pendiente");
              updateTarea(tarea, id);
              BotHelper.sendMessageToTelegram(chatId, BotMessages.TASK_UNDONE.getMessage(), this);
          } catch (Exception e) {
              logger.error(e.getLocalizedMessage(), e);
          }
        } // UNDO
      else if (messageTextFromTelegram.indexOf(BotLabels.DELETE.getLabel()) != -1) {
          String delete = messageTextFromTelegram.substring(0, messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
          Integer id = Integer.valueOf(delete);

          try {
              tareaService.deleteTareaById(id);
              BotHelper.sendMessageToTelegram(chatId, BotMessages.TASK_DELETED.getMessage(), this);
          } catch (Exception e) {
              logger.error(e.getLocalizedMessage(), e);
          }
        } // DELETE
        else if (messageTextFromTelegram.equals(BotCommands.HIDE_COMMAND.getCommand())
                    || messageTextFromTelegram.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel())) {
                BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), this);
            } // HIDE COMMANDS
        else if (messageTextFromTelegram.equals(BotLabels.SHOW_ALL_TASKS.getLabel())) {
            showAllTasks(chatId);
          } // SHOW_ALL_TASKS
        else if (messageTextFromTelegram.equals(BotCommands.TASK_LIST_COMMAND.getCommand())
        || messageTextFromTelegram.equals(BotLabels.TEAM_TASKS_LIST.getLabel())
        || messageTextFromTelegram.equals(BotLabels.MY_TASKS_LIST.getLabel())) {
              showTaskList(chatId);
          } // TASK_LIST
        else if (messageTextFromTelegram.equals(BotCommands.ADD_TASK.getCommand())
        || messageTextFromTelegram.equals(BotLabels.ADD_NEW_TASK.getLabel())) {
              promptForNewTask(chatId);
            } // ADD_CUSTOM_TASK
        else {
          addNewTask(chatId, messageTextFromTelegram);
          } // ADD_TASK
    } // IF MESSAGE && MESSAGE.HASTEXT
  }

  @Override
  public String getBotUsername() {
    return nombreBot;
  }

  private void handleAuthentication(String messageTextFromTelegram, long chatId) {
    if (!userAuthStep.containsKey(chatId)) {
        userAuthStep.put(chatId, "ASK_EMAIL");
        BotHelper.sendMessageToTelegram(chatId, "Por favor, introduce tu correo electrónico:", this);
    } else if (userAuthStep.get(chatId).equals("ASK_EMAIL")) {
        userEmail.put(chatId, messageTextFromTelegram);
        userAuthStep.put(chatId, "ASK_PASSWORD");
        BotHelper.sendMessageToTelegram(chatId, "Por favor, introduce tu contraseña:", this);
    } else if (userAuthStep.get(chatId).equals("ASK_PASSWORD")) {
        String email = userEmail.get(chatId);
        String password = messageTextFromTelegram;
        LoginRequest loginRequest = new LoginRequest(email, password);

        try {
            AuthenticationResponse response = authenticationService.login(loginRequest);
            Empleado empleado = empleadoService.getEmpleadoByEmail(email).getBody();
            authenticatedUsers.put(chatId, empleado.getPosicion().name());
            // Almacenar el ID del empleado
            authenticatedUserIds.put(chatId, empleado.getId_empleado());
            BotHelper.sendMessageToTelegram(chatId, "Autenticación exitosa. Usa /start para ver el menú principal.", this);
            userAuthStep.remove(chatId);
            userEmail.remove(chatId);
        } catch (Exception e) {
            BotHelper.sendMessageToTelegram(chatId, "Autenticación fallida. Intenta nuevamente con /start.", this);
            userAuthStep.remove(chatId);
            userEmail.remove(chatId);
        }
    }
}

private void showMainMenu(long chatId, String role) {
    SendMessage messageToTelegram = new SendMessage();
    messageToTelegram.setChatId(chatId);
    messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();

    KeyboardRow row = new KeyboardRow();
    row.add(BotLabels.MY_TASKS_LIST.getLabel());
    row.add(BotLabels.ADD_NEW_TASK.getLabel());
    if ("Manager".equals(role)) {
        row.add(BotLabels.SHOW_ALL_TASKS.getLabel());
    }
    keyboard.add(row);

    row = new KeyboardRow();
    row.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
    row.add(BotLabels.HIDE_MAIN_SCREEN.getLabel());
    keyboard.add(row);

    keyboardMarkup.setKeyboard(keyboard);
    messageToTelegram.setReplyMarkup(keyboardMarkup);

    try {
        execute(messageToTelegram);
    } catch (TelegramApiException e) {
        logger.error(e.getLocalizedMessage(), e);
    }
}

private void showTeamSelection(long chatId) {
  if (!authenticatedUserIds.containsKey(chatId)) {
      BotHelper.sendMessageToTelegram(chatId, "Por favor, inicia sesión con /start para acceder a esta función.", this);
      return;
  }

  Long empleadoId = authenticatedUserIds.get(chatId);

  ResponseEntity<List<Miembro_Equipo>> miembroEquiposResponse = miembro_EquipoService.getEquipoByMiembroId(empleadoId);
  if (miembroEquiposResponse.getStatusCode() != HttpStatus.OK) {
      BotHelper.sendMessageToTelegram(chatId, "No se encontraron equipos asociados con tu cuenta.", this);
      return;
  }

  List<Miembro_Equipo> miembroEquipos = miembroEquiposResponse.getBody();

  List<Equipo> equipos = miembroEquipos.stream()
          .map(miembroEquipo -> equipoService.getEquipoById(miembroEquipo.getId().getId_equipo()).getBody())
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

  if (equipos.isEmpty()) {
      BotHelper.sendMessageToTelegram(chatId, "No tienes equipos disponibles para seleccionar.", this);
      return;
  }

  // Mostrar selección de equipos
  SendMessage message = new SendMessage();
  message.setChatId(chatId);
  message.setText("Selecciona un equipo:");

  ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
  List<KeyboardRow> keyboard = new ArrayList<>();

  // Agregar la opción "Ninguno"
  KeyboardRow noneRow = new KeyboardRow();
  noneRow.add("Ninguno");
  keyboard.add(noneRow);

  for (Equipo equipo : equipos) {
      KeyboardRow row = new KeyboardRow();
      row.add(equipo.getNombre_equipo());
      keyboard.add(row);
  }

  keyboardMarkup.setKeyboard(keyboard);
  message.setReplyMarkup(keyboardMarkup);

  try {
      execute(message);
  } catch (TelegramApiException e) {
      logger.error(e.getLocalizedMessage(), e);
  }
}

private void handleTeamSelection(long chatId, String selectedTeamName) {
  if ("Ninguno".equals(selectedTeamName)) {
    selectedTeams.remove(chatId);
    BotHelper.sendMessageToTelegram(chatId, "Selección de equipo eliminada.", this);
    return;
}
  Long empleadoId = authenticatedUserIds.get(chatId);

  ResponseEntity<List<Miembro_Equipo>> miembroEquiposResponse = miembro_EquipoService.getEquipoByMiembroId(empleadoId);
  List<Miembro_Equipo> miembroEquipos = miembroEquiposResponse.getBody();

  List<Equipo> equipos = miembroEquipos.stream()
          .map(miembroEquipo -> equipoService.getEquipoById(miembroEquipo.getId().getId_equipo()).getBody())
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

  Optional<Equipo> selectedEquipoOptional = equipos.stream()
          .filter(equipo -> equipo.getNombre_equipo().equals(selectedTeamName))
          .findFirst();

  if (!selectedEquipoOptional.isPresent()) {
      BotHelper.sendMessageToTelegram(chatId, "Selecciona un equipo válido de la lista mostrada.", this);
      return;
  }

  selectedTeams.put(chatId, selectedEquipoOptional.get().getId_equipo());
  BotHelper.sendMessageToTelegram(chatId, "Equipo seleccionado correctamente.", this);
}

private void showProjectSelection(long chatId, long equipoId) {
  List<Proyecto> proyectos = proyectoService.getProyectosByEquipoId(equipoId).getBody();
  SendMessage message = new SendMessage();
  message.setChatId(chatId);
  message.setText("Selecciona un proyecto:");

  ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
  List<KeyboardRow> keyboard = new ArrayList<>();

  // Agregar la opción "Ninguno"
  KeyboardRow noneRow = new KeyboardRow();
  noneRow.add("Ninguno");
  keyboard.add(noneRow);

  for (Proyecto proyecto : proyectos) {
      KeyboardRow row = new KeyboardRow();
      row.add(proyecto.getNombre());
      keyboard.add(row);
  }

  keyboardMarkup.setKeyboard(keyboard);
  message.setReplyMarkup(keyboardMarkup);

  try {
      execute(message);
  } catch (TelegramApiException e) {
      logger.error(e.getLocalizedMessage(), e);
  }
}

private void handleProjectSelection(long chatId, String selectedProjectName) {
  if ("Ninguno".equals(selectedProjectName)) {
    selectedProjects.remove(chatId);
    BotHelper.sendMessageToTelegram(chatId, "Selección de proyecto eliminada.", this);
    return;
}
  Long equipoId = selectedTeams.get(chatId);
  List<Proyecto> proyectos = proyectoService.getProyectosByEquipoId(equipoId).getBody();

  Optional<Proyecto> selectedProyectoOptional = proyectos.stream()
          .filter(proyecto -> proyecto.getNombre().equals(selectedProjectName))
          .findFirst();

  if (!selectedProyectoOptional.isPresent()) {
      BotHelper.sendMessageToTelegram(chatId, "Selecciona un proyecto válido de la lista mostrada.", this);
      return;
  }

  selectedProjects.put(chatId, selectedProyectoOptional.get().getId_proyecto());
  BotHelper.sendMessageToTelegram(chatId, "Proyecto seleccionado correctamente.", this);
}

private void showSprintSelection(long chatId, long proyectoId) {
  List<Sprint> sprints = sprintService.getSprintsByProyectoId(proyectoId).getBody();
  SendMessage message = new SendMessage();
  message.setChatId(chatId);
  message.setText("Selecciona un sprint:");

  ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
  List<KeyboardRow> keyboard = new ArrayList<>();

  // Agregar la opción "Ninguno"
  KeyboardRow noneRow = new KeyboardRow();
  noneRow.add("Ninguno");
  keyboard.add(noneRow);

  for (Sprint sprint : sprints) {
      KeyboardRow row = new KeyboardRow();
      row.add(sprint.getNombre());
      keyboard.add(row);
  }

  keyboardMarkup.setKeyboard(keyboard);
  message.setReplyMarkup(keyboardMarkup);

  try {
      execute(message);
  } catch (TelegramApiException e) {
      logger.error(e.getLocalizedMessage(), e);
  }
}

private void handleSprintSelection(long chatId, String selectedSprintName) {
  if ("Ninguno".equals(selectedSprintName)) {
    selectedSprints.remove(chatId);
    BotHelper.sendMessageToTelegram(chatId, "Selección de sprint eliminada.", this);
    return;
}
  Long proyectoId = selectedProjects.get(chatId);
  List<Sprint> sprints = sprintService.getSprintsByProyectoId(proyectoId).getBody();

  Optional<Sprint> selectedSprintOptional = sprints.stream()
          .filter(sprint -> sprint.getNombre().equals(selectedSprintName))
          .findFirst();

  if (!selectedSprintOptional.isPresent()) {
      BotHelper.sendMessageToTelegram(chatId, "Selecciona un sprint válido de la lista mostrada.", this);
      return;
  }

  selectedSprints.put(chatId, selectedSprintOptional.get().getId_sprint());
  BotHelper.sendMessageToTelegram(chatId, "Sprint seleccionado correctamente.", this);
}



private void showAllTasks(long chatId) {
  // Obtener todas las tareas del servicio
  ResponseEntity<List<Tarea>> allTareas = tareaService.getTareaList();
  
  // Filtrar las tareas en función de las selecciones del usuario
  Long selectedTeamId = selectedTeams.get(chatId);
  Long selectedProjectId = selectedProjects.get(chatId);
  Long selectedSprintId = selectedSprints.get(chatId);
  
  List<Tarea> tareaList = allTareas.getBody();
  
  // Si se ha seleccionado un equipo, filtrar las tareas de ese equipo
  if (selectedTeamId != null) {
      List<Long> miembroIds = miembro_EquipoService.getMiembroByEquipoId(selectedTeamId)
          .getBody()
          .stream()
          .map(miembroEquipo -> miembroEquipo.getId().getId_miembro())
          .collect(Collectors.toList());
      List<Long> tareaIds = miembroIds.stream()
          .flatMap(id -> empleado_TareaService.getTareaByEmpleadoId(id).getBody().stream())
          .map(empleadoTarea -> empleadoTarea.getId().getId_tarea())
          .collect(Collectors.toList());
      tareaList = tareaList.stream()
          .filter(tarea -> tareaIds.contains(tarea.getId_tarea()))
          .collect(Collectors.toList());
  }
  
  // Si se ha seleccionado un proyecto, filtrar las tareas de ese proyecto
  if (selectedProjectId != null) {
      List<Long> sprintIds = sprintService.getSprintsByProyectoId(selectedProjectId)
          .getBody()
          .stream()
          .map(Sprint::getId_sprint)
          .collect(Collectors.toList());
      tareaList = tareaList.stream()
          .filter(tarea -> sprintIds.contains(tarea.getId_sprint()))
          .collect(Collectors.toList());
  }
  
  // Si se ha seleccionado un sprint, filtrar las tareas de ese sprint
  if (selectedSprintId != null) {
      tareaList = tareaList.stream()
          .filter(tarea -> tarea.getId_sprint().equals(selectedSprintId))
          .collect(Collectors.toList());
  }
          
  // Crear el mensaje de la tabla
  StringBuilder tasksTable = new StringBuilder("Lista de todas las tareas:\n\n");
  tasksTable.append("ID | Descripción | Estado\n");
  tasksTable.append("---------------------------------\n");
  
  for (Tarea tarea : tareaList) {
      tasksTable.append(tarea.getId_tarea()).append(" | ");
      tasksTable.append(tarea.getDescripcion_tarea()).append(" | ");
      tasksTable.append(tarea.getEstado()).append("\n");
  }
  
  // Enviar el mensaje al usuario
  SendMessage messageToTelegram = new SendMessage();
  messageToTelegram.setChatId(chatId);
  messageToTelegram.setText(tasksTable.toString());

  try {
      execute(messageToTelegram);
  } catch (TelegramApiException e) {
      logger.error(e.getLocalizedMessage(), e);
  }
}

private void showTaskList(long chatId) {
  // Enviar un mensaje al usuario indicando que se están buscando las tareas
  BotHelper.sendMessageToTelegram(chatId, "Buscando tareas...", this);

  ResponseEntity<List<Tarea>> allTareas = tareaService.getTareaList();
  ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
  List<KeyboardRow> keyboard = new ArrayList<>();
  // Filtrar las tareas en función de las selecciones del usuario
  Long selectedTeamId = selectedTeams.get(chatId);
  Long selectedProjectId = selectedProjects.get(chatId);
  Long selectedSprintId = selectedSprints.get(chatId);

  // Enviar un mensaje al usuario con el equipo, proyecto y sprint seleccionados
  String selectedTeamMessage = selectedTeamId != null ? "Equipo seleccionado: " + equipoService.getEquipoById(selectedTeamId).getBody().getNombre_equipo() : "No se seleccionó ningún equipo";
  String selectedProjectMessage = selectedProjectId != null ? "Proyecto seleccionado: " + proyectoService.getProyectoById(selectedProjectId).getBody().getNombre() : "No se seleccionó ningún proyecto";
  String selectedSprintMessage = selectedSprintId != null ? "Sprint seleccionado: " + sprintService.getSprintById(selectedSprintId).getBody().getNombre() : "No se seleccionó ningún sprint";
  BotHelper.sendMessageToTelegram(chatId, selectedTeamMessage + "\n" + selectedProjectMessage + "\n" + selectedSprintMessage, this);

  // command back to main screen
  KeyboardRow mainScreenRowTop = new KeyboardRow();
  mainScreenRowTop.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
  keyboard.add(mainScreenRowTop);

  KeyboardRow firstRow = new KeyboardRow();
  firstRow.add(BotLabels.ADD_NEW_TASK.getLabel());
  keyboard.add(firstRow);

  KeyboardRow tareasTitleRow = new KeyboardRow();
  List<Tarea> tareaList = allTareas.getBody();

  // Si se ha seleccionado un equipo, filtrar las tareas de ese equipo
  if (selectedTeamId != null) {
    List<Long> miembroIds = miembro_EquipoService.getMiembroByEquipoId(selectedTeamId)
        .getBody()
        .stream()
        .map(miembroEquipo -> miembroEquipo.getId().getId_miembro())
        .collect(Collectors.toList());
    List<Long> tareaIds = miembroIds.stream()
        .flatMap(id -> empleado_TareaService.getTareaByEmpleadoId(id).getBody().stream())
        .map(empleadoTarea -> empleadoTarea.getId().getId_tarea())
        .collect(Collectors.toList());
    tareaList = tareaList.stream()
        .filter(tarea -> tareaIds.contains(tarea.getId_tarea()))
        .collect(Collectors.toList());
}

  // Si se ha seleccionado un proyecto, filtrar las tareas de ese proyecto
  if (selectedProjectId != null) {
      List<Long> sprintIds = sprintService.getSprintsByProyectoId(selectedProjectId)
          .getBody()
          .stream()
          .map(Sprint::getId_sprint)
          .collect(Collectors.toList());
      tareaList = tareaList.stream()
          .filter(tarea -> sprintIds.contains(tarea.getId_sprint()))
          .collect(Collectors.toList());
  }

  // Si se ha seleccionado un sprint, filtrar las tareas de ese sprint
  if (selectedSprintId != null) {
      tareaList = tareaList.stream()
          .filter(tarea -> tarea.getId_sprint().equals(selectedSprintId))
          .collect(Collectors.toList());
  }
  List<Tarea> tareasActivas = tareaList.stream().filter(tarea -> !tarea.isDone()).collect(Collectors.toList());

  for (Tarea tarea : tareasActivas) {
    KeyboardRow currentRow = new KeyboardRow();
    currentRow.add(tarea.getDescripcion_tarea());
    currentRow.add(tarea.getId_tarea() + BotLabels.DASH.getLabel() + BotLabels.DONE.getLabel());
    keyboard.add(currentRow);
  }

  List<Tarea> tareaTerminada = tareaList.stream().filter(tarea -> tarea.isDone()).collect(Collectors.toList());

  for (Tarea tarea : tareaTerminada) {
    KeyboardRow currentRow = new KeyboardRow();
    currentRow.add(tarea.getDescripcion_tarea());
    currentRow.add(tarea.getId_tarea() + BotLabels.DASH.getLabel() + BotLabels.UNDO.getLabel());
    currentRow.add(tarea.getId_tarea() + BotLabels.DASH.getLabel() + BotLabels.DELETE.getLabel());
    keyboard.add(currentRow);
  }

  // command back to main screen
  KeyboardRow mainScreenRowBottom = new KeyboardRow();
  mainScreenRowBottom.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
  keyboard.add(mainScreenRowBottom);

  keyboardMarkup.setKeyboard(keyboard);

  SendMessage messageToTelegram = new SendMessage();
  messageToTelegram.setChatId(chatId);
  messageToTelegram.setText(BotLabels.MY_TASKS_LIST.getLabel());
  messageToTelegram.setReplyMarkup(keyboardMarkup);

  try {
      execute(messageToTelegram);
  } catch (TelegramApiException e) {
      logger.error(e.getLocalizedMessage(), e);
  }
}

private void promptForNewTask(long chatId) {
  try {
    SendMessage messageToTelegram = new SendMessage();
    messageToTelegram.setChatId(chatId);
    messageToTelegram.setText(BotMessages.TYPE_NEW_TASK.getMessage());
    // Esconder keyboard
    ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
    messageToTelegram.setReplyMarkup(keyboardMarkup);

    // Enviar message
    execute(messageToTelegram);
  } catch (Exception e) {
      logger.error(e.getLocalizedMessage(), e);
  }
}

private void addNewTask(long chatId, String taskDescription) {
  try {
    Tarea nuevaTarea = new Tarea();
    nuevaTarea.setDescripcion_tarea(taskDescription);
    nuevaTarea.setEstado("Pendiente");
    ResponseEntity<Tarea> entity = addTarea(nuevaTarea);

    SendMessage messageToTelegram = new SendMessage();
    messageToTelegram.setChatId(chatId);
    messageToTelegram.setText(BotMessages.NEW_TASK_ADDED.getMessage());

    execute(messageToTelegram);
} catch (Exception e) {
    logger.error(e.getLocalizedMessage(), e);
}
}

  // GET /Tarea
  public ResponseEntity<List<Tarea>> getAllTareas() { 
    return tareaService.getTareaList();
  }

  // GET BY ID /Tarea/{id}
  public ResponseEntity<Tarea> getTareaById(@PathVariable int id) {
    try {
      ResponseEntity<Tarea> responseEntity = tareaService.getTareaById(id);
      return new ResponseEntity<Tarea>(responseEntity.getBody(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getLocalizedMessage(), e);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  //Checar

  public ResponseEntity<Tarea> addTarea(@RequestBody Tarea tarea) throws Exception {
    ResponseEntity<Tarea> responseEntity = tareaService.addTarea(tarea);
    Tarea td = responseEntity.getBody();
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("location", "" + td.getId_tarea());
    responseHeaders.set("Access-Control-Expose-Headers", "location");
    // URI location = URI.create(""+td.getID())

    return ResponseEntity.ok().headers(responseHeaders).body(td);
  }

  // UPDATE /Tarea/{id}
  public ResponseEntity<Tarea> updateTarea(@RequestBody Tarea tarea, @PathVariable long id) {
    try {
      Tarea _tarea = tareaService.updateTarea(tarea, id).getBody();
      System.out.println(_tarea.toString());
      return ResponseEntity.ok(_tarea);
    } catch (Exception e) {
      logger.error(e.getLocalizedMessage(), e);
      return ResponseEntity.notFound().build();
    }
  }

  // DELETE /Tarea/{id}
  public ResponseEntity<Boolean> deleteTarea(@PathVariable("id") long id) {
    Boolean flag = false;
    try {
      flag = tareaService.deleteTareaById(id);
      return new ResponseEntity<>(flag, HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getLocalizedMessage(), e);
      return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
    }
  }
}