package com.oga.demandeservice.services;


import com.oga.demandeservice.dto.NotificationEntity;
import com.oga.demandeservice.entity.DemandeEntity;
import com.oga.demandeservice.exeption.NotFoundExeption;
import com.oga.demandeservice.repository.DemandeRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeServiceImpl implements DemandeService{

    @Autowired
    private DemandeRepository demandeRepository;

    public DemandeServiceImpl(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    @Override
    public DemandeEntity ajouterDemande(DemandeEntity demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public List<DemandeEntity> getDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    @SneakyThrows
    public DemandeEntity getDemandeByid(long id) {
        DemandeEntity demande =  demandeRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("user not found"+id));

        return   demande;
    }

    @Override
    public DemandeEntity rhValidation(long id) {
        DemandeEntity demande = getDemandeByid(id);
        demande.setStatus(1);
        return ajouterDemande(demande);
    }

    @Override
    public DemandeEntity rhRefus(long id) {
        DemandeEntity demande = getDemandeByid(id);
        demande.setStatus(2);
        return ajouterDemande(demande);
    }


    @Override
    public DemandeEntity updateDemande(DemandeEntity nouvelleDemande, long id) {
        DemandeEntity demande = demandeRepository.findById(id)
                .orElseThrow(()-> new NotAcceptableStatusException("user not found"));
        demande.setType(nouvelleDemande.getType());
        demande.setInfo(nouvelleDemande.getInfo());
        demande.setStatus(nouvelleDemande.getStatus());
        demande.setIdEmploye(nouvelleDemande.getIdEmploye());
        demande.setRaison(nouvelleDemande.getRaison());
        demande.setDateCreation(nouvelleDemande.getDateCreation());

//        demande.setCommentaireEmploye(nouvelleDemande.getCommentaireEmploye());
//        demande.setIdGrh(nouvelleDemande.getIdGrh());
//        demande.setCommentaireGrh(nouvelleDemande.getCommentaireGrh());
        demande.setDateReponse(nouvelleDemande.getDateReponse());
        demandeRepository.save(demande);

        return demande;
    }

    @Override
    public List<DemandeEntity> valideDemande() {

        List<DemandeEntity> demandes = getDemandes();

        for (Iterator<DemandeEntity> i = demandes.iterator(); i.hasNext();){
            DemandeEntity demande = i.next();
            if (demande.getStatus()!= 1){
                i.remove();
            }


        }
        return demandes;
    }

    @Override
    public List<DemandeEntity> RefusedDemane() {
        List<DemandeEntity> demandes = getDemandes();

        for (Iterator<DemandeEntity> i = demandes.iterator(); i.hasNext();){
            DemandeEntity demande = i.next();
            if (demande.getStatus()!= 2){
                i.remove();
            }
        }
        return demandes;
    }

    @Override
    public List<DemandeEntity> enAttenteDemane() {
        List<DemandeEntity> demandes = demandeRepository.findAll();

        for (Iterator<DemandeEntity> i = demandes.iterator(); i.hasNext();){
            DemandeEntity demande = i.next();
            if (demande.getStatus()!= 0){
                i.remove();
            }
        }
        return demandes;
    }

    @Override
    public Optional<DemandeEntity> getDemandeByIdForResponse(long id) {
        return demandeRepository.findById(id);
    }

    @Override
    public DemandeEntity addDemande(DemandeEntity demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public List<DemandeEntity> findDemandeByIdEmploye(long employeId) {
        return demandeRepository.findAllByidEmploye(employeId);
    }


}
