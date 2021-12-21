package com.oga.notificationservvice.services;

import com.oga.notificationservvice.dto.NotifsAndCompteur;
import com.oga.notificationservvice.entity.NotificationEntity;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    NotificationEntity sendNotification(NotificationEntity notificationEntity);
    List<NotificationEntity> getAllNotification();
    Optional<NotificationEntity> getNotificationById(long id);
    NotifsAndCompteur getAllNotificationByUserId(long userId);

//    NotificationEntity updateClikedStatus(long id);
}
