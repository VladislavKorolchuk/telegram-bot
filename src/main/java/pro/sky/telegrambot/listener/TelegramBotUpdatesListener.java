package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notifications;
import pro.sky.telegrambot.repositories.NotificationRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            // ------------------------------------------------------------------------
            String messageText = update.message().text();
            if (update.message() == null || update.message().text() == null) {
                throw new RuntimeException();
            }


            long chatId = update.message().chat().id();

            if (messageText.equals("/start")) {
                SendResponse response = telegramBot.execute(new SendMessage(chatId, "Привет"));
            } else {
                String regex = "([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(messageText);
                if (matcher.matches()) {
                    String date = matcher.group(1);
                    String str = matcher.group(3);
                    Notifications notification = new Notifications(1L, chatId, str, LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                    notificationRepository.save(notification);
                    SendResponse response = telegramBot.execute(new SendMessage(chatId, "Нотификация добавлена"));
                }
                else {
                    SendResponse response = telegramBot.execute(new SendMessage(chatId, "Введите в таком формате: 01.01.2022 20:00 Сделать домашнюю работу: "));
                }
            }
            //--------------------------------------------------------------------------

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(fixedDelay = 60_000L)
    public void run() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        List<Notifications> notificationsList = new ArrayList<>();
        notificationsList = notificationRepository.getAllBy();

        for (int i = 0; i < notificationsList.size(); i++) {
            LocalDate date1 = notificationsList.get(i).getTime().toLocalDate();
            int hour1 = notificationsList.get(i).getTime().getHour();
            int minute1 = notificationsList.get(i).getTime().getMinute();
            if (date.equals(date1) && time.getHour() == hour1 && time.getMinute() == minute1) {
                long chatId = notificationsList.get(i).getChatId();
                String str = notificationsList.get(i).getChatStr();
                SendResponse response = telegramBot.execute(new SendMessage(chatId, str));
            }
        }
    }

}
