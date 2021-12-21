package com.oga.projetservice.dto;

import com.oga.projetservice.entity.ProjectEntity;
import com.oga.projetservice.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreationDto {
    @Autowired
    private RestTemplate restTemplate;
    private long id;

    private String title;
    private String description;
    private String dateDebut;
    private String theme;
    private List<Long> users;

}
