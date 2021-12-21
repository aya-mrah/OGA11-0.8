package com.oga.notificationservvice.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TacheEntity {



        private long id;
        private String titre;
        private String description;
        private String status;
        private String DateDebut;
        private String DateFin;
        private String Dificulte;
        private int avancement;
        private String problem;
        private ArrayList<Long> usersList = new ArrayList<>();


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


