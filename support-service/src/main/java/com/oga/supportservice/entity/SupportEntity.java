package com.oga.supportservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SupportEntity {
 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)

     private long id;
     private String titre;
     private String date;
     private String description_employe;
     private boolean status;
     private String fileName;
     private String reponse;
     private String fileUrl;
     private long idEmployee;



}
