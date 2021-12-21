package com.oga.notificationservvice.dto;

import com.oga.notificationservvice.entity.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class NotifsAndCompteur {
   private List<NotificationEntity> notifs;
   private int notifCompteur ;
}
