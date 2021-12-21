package com.oga.projetservice.dto;

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
    private String nomProjet;
    private String nomTache;
    private long projetId=0;
    private long tacheId=0;
    private long chefDeProjetId=0;

}
