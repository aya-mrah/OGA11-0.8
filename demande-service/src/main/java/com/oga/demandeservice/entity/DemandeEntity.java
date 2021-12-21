package com.oga.demandeservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Table(name = "demandes")
@Entity
@Data
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class DemandeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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







}
