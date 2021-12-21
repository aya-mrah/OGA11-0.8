package com.oga.etudeservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "appelDoffres")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class AppelDoffreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String reference; //référence de l'appel d'offre
    private String lienSite; // lien de site d'appel d'offre
    private String datepost; // la date de l'ajout de l'appel d'offre
    private String description;
    private String dateFin; // date délais pour la candidature a l'appel d'offres
    private int status; // ( 0 => en attente - 1 => validé - 2 => réfusé )
    private long idEmployee;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private UserEntity userEntity;

    @OneToMany(mappedBy = "appelDoffre",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<EtudeEntity> etudes;


}
