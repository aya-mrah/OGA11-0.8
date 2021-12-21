package com.oga.projetservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"})
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    private String title;
    private String description;
    private String dateDebut;
    private String theme;
    private boolean termine;
    private ArrayList<Long> users= new ArrayList<>();

    @OneToMany(mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<QualityEntity> qualitys;

    @OneToMany(mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TacheEntity> taches;

//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {
//                    CascadeType.PERSIST,
//                    CascadeType.MERGE
//            },
//            mappedBy = "projects")
//    private List<UserEntity> users = new ArrayList<>();
//@ManyToMany(fetch = FetchType.LAZY,
//        cascade = {
//                CascadeType.PERSIST,
//                CascadeType.MERGE,
//        })
//@JoinTable(name = "user_project",
//        joinColumns = { @JoinColumn(name = "project_id") },
//        inverseJoinColumns = { @JoinColumn(name = "user_id") })
//@JsonIgnore
//private List<UserEntity> users = new ArrayList<UserEntity>();



}
