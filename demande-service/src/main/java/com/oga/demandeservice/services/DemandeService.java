package com.oga.demandeservice.services;

import com.oga.demandeservice.dto.NotificationEntity;
import com.oga.demandeservice.entity.DemandeEntity;

import java.util.List;
import java.util.Optional;

public interface DemandeService {
    DemandeEntity ajouterDemande(DemandeEntity demande);

    List<DemandeEntity> getDemandes();

    DemandeEntity getDemandeByid(long id);

    DemandeEntity rhValidation(long id);

    DemandeEntity rhRefus(long id);

    DemandeEntity updateDemande(DemandeEntity demande, long id);

    List<DemandeEntity> valideDemande();

    List<DemandeEntity> RefusedDemane();

    List<DemandeEntity> enAttenteDemane();
    public Optional<DemandeEntity> getDemandeByIdForResponse(long id);
    DemandeEntity addDemande(DemandeEntity demande);

    List<DemandeEntity> findDemandeByIdEmploye(long employeId);






}
