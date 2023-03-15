package com.example.telegrambot.repository;

import com.example.telegrambot.entity.NotificationTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Integer> {
    NotificationTask findByTime(LocalDateTime dateTime);
}
