package com.oga.userservice.dto;

import com.oga.userservice.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NotificationEntity {

    private long id;
    private String notifContenu;
    private String date;
    private String imageUrl;
    private String Type;
    private String type2;
    private long  userId;

}
