package com.oga.notificationservvice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String notifContenu;
    private String date;
    private String imageUrl;
    private String type;
    private String type2;
    private long userId;
    private boolean accepted;
    private String nomProjet;
    private long projetId=0;
    private long chefDeProjetId=0;
    private String nomTache;
    private long tacheId=0;




}
