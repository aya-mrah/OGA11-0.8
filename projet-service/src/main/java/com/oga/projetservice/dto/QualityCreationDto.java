package com.oga.projetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QualityCreationDto {


    private long id;
    private String projectReference;
    private String description;
    private String title;
    private String fileName;
    private String projectTitle;
    private UserDto userEntity;
    private String imageUri;


}