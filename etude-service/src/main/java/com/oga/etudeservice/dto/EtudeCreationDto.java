package com.oga.etudeservice.dto;

import com.oga.etudeservice.entity.EtudeEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EtudeCreationDto {

    private EtudeEntity etudeEntity;

    private String imageUri;

    private String message;

    public EtudeCreationDto(EtudeEntity etudeEntity, String imageUri, String message) {
        this.etudeEntity = etudeEntity;
        this.imageUri = imageUri;
        this.message = message;
    }

    public EtudeEntity getEtudeEntity() {
        return etudeEntity;
    }

    public void setEtudeEntity(EtudeEntity etudeEntity) {
        this.etudeEntity = etudeEntity;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

