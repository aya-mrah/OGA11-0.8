package com.oga.notificationservvice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEntity {
    private long id;

    private String title;
    private String description;
    private String dateDebut;
    private String theme;
    private boolean termine;
    private ArrayList<Long> users= new ArrayList<>();
}
