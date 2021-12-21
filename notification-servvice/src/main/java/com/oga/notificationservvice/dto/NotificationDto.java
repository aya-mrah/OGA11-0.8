package com.oga.notificationservvice.dto;

import com.oga.notificationservvice.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

  private long id;
  private String notifContenu;
  private String date;
  private String imageUrl;
  private String Type;
  private String type2;
  private UserEntity userEntity;


}
