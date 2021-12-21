package com.oga.projetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TacheDto {
    private long id;
    private String titre;
    private String description;
    private String status;
    private String DateDebut;
    private String DateFin;
    private String Dificulte;
    private int avancement;
    private String problem;
    private List<UserDto> users;

}
