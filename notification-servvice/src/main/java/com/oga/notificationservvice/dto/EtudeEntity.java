package com.oga.notificationservvice.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtudeEntity {

    private long id;
    private String referenceProjet; //référence de projet
    private String datepost; // la date de l'ajout de l'étude
    private  String titre; // titre de l'etude
    private String description;
    private int status; // ( 0 => en attente - 1 => validé - 2 => réfusé )
    private String fileName;
    private long idEmployee;



//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private UserEntity userEntity;

}

