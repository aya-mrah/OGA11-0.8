package com.oga.userservice.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class DemandeEntity {

    private long id;
    private String type; // ( ATTESTATION - FICHE_PAIE - CONGE - AVANCE - AUTRE )
    private String info; // par le system
    private int status; // ( 0 => en attente - 1 => validé - 2 => réfusé )
    private long idEmploye;
    private String raison; // par l'mployé
    private long dateCreation;
    private String commentaireEmploye; // par l'employé
    private long idGrh;
    private String commentaireGrh; // par le grh
//    private String commentaire;
    private long dateReponse;
    private Date dateDebut;
    private Date dateFin;

    /** user */




}
