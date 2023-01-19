package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Notifications {

    @Id
    @GeneratedValue
    private Long id;

    private Long chatId;

    private String chatStr;

    private LocalDateTime time;

    public Notifications(Long id, Long chatId, String chatStr, LocalDateTime time) {
        this.id = id;
        this.chatId = chatId;
        this.chatStr = chatStr;
        this.time = time;
    }

    public Notifications() {

    }

    public Long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getChatStr() {
        return chatStr;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
