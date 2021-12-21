package com.oga.notificationservvice.controller;

import com.oga.notificationservvice.dto.NotificationDto;
import com.oga.notificationservvice.dto.NotifsAndCompteur;
import com.oga.notificationservvice.entity.NotificationEntity;
import com.oga.notificationservvice.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("notification")
    public NotificationEntity addDemande(@RequestBody NotificationEntity notificationEntity ){
        return notificationService.sendNotification(notificationEntity);

    }
    @GetMapping("notifications")
    public List<NotificationEntity> getAllNotifications(){
      return   notificationService.getAllNotification();
    }

    @GetMapping("notifications/user/{userId}")
    public NotifsAndCompteur getAllNotificationsByUserId(@PathVariable(name = "userId")long userId){
        return   notificationService.getAllNotificationByUserId(userId);
    }

//    @PutMapping("clickedStatus/{id}")
//    public NotificationEntity updateNotifCompteur(@PathVariable(name = "id") long id){
//        return notificationService.updateClikedStatus(id);
//
//    }
}
