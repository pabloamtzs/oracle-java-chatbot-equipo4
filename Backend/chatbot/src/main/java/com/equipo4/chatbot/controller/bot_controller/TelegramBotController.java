package com.equipo4.chatbot.controller.bot_controller;
import com.equipo4.chatbot.utils.BotCommands;
import com.equipo4.chatbot.utils.BotLabels;
import com.equipo4.chatbot.utils.BotMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;


import com.equipo4.chatbot.service.tarea_service.TareaService;
import com.equipo4.chatbot.utils.BotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private String nombreBot;

  public TelegramBotController(String botToken, String nombreBot, TareaService tareaService) {
    super(botToken);
    logger.info("Bot Token: " + botToken);
    logger.info("Bot name: " + nombreBot);
    this.tareaService = tareaService;
    this.nombreBot = nombreBot;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
        String messageTextFromTelegram = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())
                || messageTextFromTelegram.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel())) {
            System.out.println("Start command received");
            SendMessage messageToTelegram = new SendMessage();
            messageToTelegram.setChatId(chatId);
            messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();

            // first row
            KeyboardRow row = new KeyboardRow();
            row.add(BotLabels.MY_TASKS_LIST.getLabel());
            row.add(BotLabels.ADD_NEW_TASK.getLabel());
            // Add the first row to the keyboard
            keyboard.add(row);

            // second row
            row = new KeyboardRow();
            row.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
            row.add(BotLabels.HIDE_MAIN_SCREEN.getLabel());
            keyboard.add(row);

            // Set the keyboard
            keyboardMarkup.setKeyboard(keyboard);

            // Add the keyboard markup
            messageToTelegram.setReplyMarkup(keyboardMarkup);

            try {
                execute(messageToTelegram);
            } catch (TelegramApiException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        } // IF MESSAGE == START || SHOW_MAIN_SCREEN
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
        else if (messageTextFromTelegram.equals(BotCommands.TASK_LIST_COMMAND.getCommand())
                || messageTextFromTelegram.equals(BotLabels.TEAM_TASKS_LIST.getLabel())
                || messageTextFromTelegram.equals(BotLabels.MY_TASKS_LIST.getLabel())) {
            ResponseEntity<List<Tarea>> allTareas = tareaService.getTareaList();
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();

            // command back to main screen
            KeyboardRow mainScreenRowTop = new KeyboardRow();
            mainScreenRowTop.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
            keyboard.add(mainScreenRowTop);

            KeyboardRow firstRow = new KeyboardRow();
            firstRow.add(BotLabels.ADD_NEW_TASK.getLabel());
            keyboard.add(firstRow);

            KeyboardRow tareasTitleRow = new KeyboardRow();
            List<Tarea> tareaList = allTareas.getBody();
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
        } // TASK_LISTHELPER
        else if (messageTextFromTelegram.equals(BotCommands.ADD_TASK.getCommand())
                || messageTextFromTelegram.equals(BotLabels.ADD_NEW_TASK.getLabel())) {
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
        } // ADD_CUSTOM_TASK
        else {
            try {
                Tarea nuevaTarea = new Tarea();
                nuevaTarea.setDescripcion_tarea(messageTextFromTelegram);
                nuevaTarea.setEstado("Pendiente");
                ResponseEntity<Tarea> entity = addTarea(nuevaTarea);

                SendMessage messageToTelegram = new SendMessage();
                messageToTelegram.setChatId(chatId);
                messageToTelegram.setText(BotMessages.NEW_TASK_ADDED.getMessage());

                execute(messageToTelegram);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        } // ADD_TASK
    } // IF MESSAGE && MESSAGE.HASTEXT
  }

  @Override
  public String getBotUsername() {
    return nombreBot;
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