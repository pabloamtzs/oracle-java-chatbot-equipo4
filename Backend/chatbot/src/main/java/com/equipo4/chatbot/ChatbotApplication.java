package com.equipo4.chatbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.equipo4.chatbot.utils.BotMessages;
import com.equipo4.chatbot.controller.bot_controller.TelegramBotController;
import com.equipo4.chatbot.service.authentication_service.AuthenticationService;
import com.equipo4.chatbot.service.empleado_service.EmpleadoService;
import com.equipo4.chatbot.service.empleado_tarea_service.Empleado_TareaService;
import com.equipo4.chatbot.service.equipo_service.EquipoService;
import com.equipo4.chatbot.service.miembro_equipo_service.Miembro_EquipoService;
import com.equipo4.chatbot.service.proyecto_service.ProyectoService;
import com.equipo4.chatbot.service.sprint_service.SprintService;
import com.equipo4.chatbot.service.tarea_service.TareaService;

@SpringBootApplication
public class ChatbotApplication implements CommandLineRunner{

	private static final Logger logger = LoggerFactory.getLogger(ChatbotApplication.class);
	

	@Autowired
	private TareaService tareaService;
	@Autowired
	private EmpleadoService empleadoService;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private EquipoService equipoService;
	@Autowired
	private Miembro_EquipoService miembroEquipoService;
	@Autowired
	private ProyectoService proyectoService;
	@Autowired
	private SprintService sprintService;
	@Autowired
	private Empleado_TareaService empleadoTareaService;
	

	@Value("${telegram.bot.token}")
	private String telegramBotToken;

	@Value("${telegram.bot.name}")
	private String botName;

	public static void main(String[] args) {
		SpringApplication.run(ChatbotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(new TelegramBotController(telegramBotToken, botName, tareaService, authenticationService, empleadoService, equipoService, miembroEquipoService, proyectoService, sprintService, empleadoTareaService));
			logger.info(BotMessages.BOT_REGISTERED_STARTED.getMessage());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
