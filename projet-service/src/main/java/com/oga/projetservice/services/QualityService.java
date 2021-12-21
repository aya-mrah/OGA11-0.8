
package com.oga.projetservice.services;

import com.oga.projetservice.entity.QualityEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualityService {
    public List<QualityEntity> getAllQualityRapport();

    public ResponseEntity<QualityEntity> creerQualityRapport(QualityEntity qualityEntity, Long id, long projectId);

    public QualityEntity getQualityRapport(Long id);
    public ResponseEntity<QualityEntity> updateQualityRapport(Long id, QualityEntity newQualityRapport);
    public Optional<QualityEntity> getQualityRapportByIdForResponse(long id);
    public QualityEntity addQualityRapport(QualityEntity qualityEntity);
}