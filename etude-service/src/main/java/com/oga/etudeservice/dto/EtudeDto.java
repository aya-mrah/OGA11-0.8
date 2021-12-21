package com.oga.etudeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtudeDto {

    private long id;
    private String referenceProjet; //référence de projet
    private String datepost; // la date de l'ajout de l'étude
    private  String titre; // titre de l'etude
    private String description;
    private int status; // ( 0 => en attente - 1 => validé - 2 => réfusé )
    private String fileName;
    private String imageUri;
    private UserDto userEntity;
    private AppelDoffreDto appelDoffre;

}
