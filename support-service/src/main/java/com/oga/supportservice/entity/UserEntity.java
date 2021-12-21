package com.oga.supportservice.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


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
