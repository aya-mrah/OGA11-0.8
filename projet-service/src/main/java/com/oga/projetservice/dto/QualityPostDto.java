package com.oga.projetservice.dto;

import com.oga.projetservice.entity.QualityEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QualityPostDto {

        private QualityEntity qualityEntity;
        private String imageUri;

}
