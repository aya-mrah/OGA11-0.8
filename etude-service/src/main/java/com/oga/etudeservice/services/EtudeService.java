package com.oga.etudeservice.services;

import com.oga.etudeservice.entity.EtudeEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface EtudeService {
    public ResponseEntity<EtudeEntity> creerEtude(EtudeEntity etude, long id, long appelDoffreId);
    public List<EtudeEntity> getAllEtudes();
    public EtudeEntity getEtudeById(Long id);
    public ResponseEntity<EtudeEntity> updateEtude(Long id, EtudeEntity newEtude);
    public Optional<EtudeEntity> getEtudeByIdForResponse(long id);
}
