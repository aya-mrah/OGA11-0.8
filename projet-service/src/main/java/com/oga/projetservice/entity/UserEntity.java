package com.oga.projetservice.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {


    private long id;
    private String userName;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private MultipartFile[] imageFile;
    private String image;
    private String profileImage;
    private String telephone;
    private boolean disabled=false;
    private String  departement;
    private String sex;
    private String role;
    private String[] roles;
    private String[] authorities;
    private int compteurNotif;
    private List<Long> Projects = new ArrayList<>();
    private List<Long> taches = new ArrayList<>();









//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {
//                    CascadeType.PERSIST,
//                    CascadeType.MERGE,
//            })
//    @JoinTable(name = "user_projects",
//            joinColumns = { @JoinColumn(name = "user_id") },
//            inverseJoinColumns = { @JoinColumn(name = "project_id") })
//    private List<ProjectEntity> projects = new ArrayList<ProjectEntity>();


//



}
