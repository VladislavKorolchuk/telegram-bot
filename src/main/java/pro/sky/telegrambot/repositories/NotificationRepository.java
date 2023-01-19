package pro.sky.telegrambot.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Notifications;

import java.util.Collection;
import java.util.List;

public interface NotificationRepository extends JpaRepository <Notifications,Long>{

    List<Notifications> getAllBy();

}
