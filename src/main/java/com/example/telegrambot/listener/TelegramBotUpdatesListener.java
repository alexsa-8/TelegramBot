package com.example.telegrambot.listener;

import com.example.telegrambot.entity.NotificationTask;
import com.example.telegrambot.repository.NotificationTaskRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener{
    private static final Pattern PATTERN = Pattern.compile("([0-9.:\s]{16})(\s)([\\W+]+)");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private final NotificationTaskRepository taskRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskRepository taskRepository) {
        this.telegramBot = telegramBot;
        this.taskRepository = taskRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
//   textMessage
            String textMessage = update.message().text();

            if (update.message().text().equals("/start")) {
                SendMessage greeting = new SendMessage(update.message().chat().id(), "Добро пожаловать. Это бот для уведомлений." +
                        "Введите заметку в формате: дд.мм.гггг чч:мм задача");
                telegramBot.execute(greeting);
            } else {
                Matcher matcher = PATTERN.matcher(textMessage);
                if (matcher.find()) {
                    LocalDateTime localDateTime = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
                    String text = matcher.group(3);
                    NotificationTask notificationTask = new NotificationTask();
                    notificationTask.setChatId(update.message().chat().id());
                    notificationTask.setTime(localDateTime);
                    notificationTask.setMessage(text);
                    taskRepository.save(notificationTask);
                    SendMessage textMessage1 = new SendMessage(update.message().chat().id(), "Задача сохранена");
                    telegramBot.execute(textMessage1);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
