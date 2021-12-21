package com.oga.projetservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class TacheEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id", unique = true, nullable = false)
        private long id;
        @Column(name = "titre", nullable = false)
        private String titre;
        @Column(name = "description")
        private String description;
        @Column(name = "progress")
        private String status;
        private String DateDebut;
        private String DateFin;
        private String Dificulte;
        private int avancement;
        private boolean blocke;
        private String problem;
        @Column(name = "users")
        private ArrayList<Long> usersList = new ArrayList<>();


        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "project_id", nullable = false)
        @OnDelete(action = OnDeleteAction.CASCADE)


        private ProjectEntity project;

//        @ManyToMany(fetch = FetchType.LAZY,
//                cascade = {
//                        CascadeType.PERSIST,
//                        CascadeType.MERGE,
//                })
//        @JoinTable(name = "user_tache",
//                joinColumns = { @JoinColumn(name = "tache_id") },
//                inverseJoinColumns = { @JoinColumn(name = "user_id") })
//
//
//
//        @JsonIgnore
//        private List<UserEntity> users = new ArrayList<UserEntity>();
    }


