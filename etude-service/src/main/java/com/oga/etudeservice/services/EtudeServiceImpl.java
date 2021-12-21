package com.oga.etudeservice.services;


import com.oga.etudeservice.entity.AppelDoffreEntity;
import com.oga.etudeservice.entity.EtudeEntity;
import com.oga.etudeservice.entity.UserEntity;
import com.oga.etudeservice.repository.EtudeRepository;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class EtudeServiceImpl implements EtudeService {
    private EtudeRepository etudeRepository;
    private AppelDoffreService appelDoffreService;
    private RestTemplate restTemplate;

    @Autowired
    public EtudeServiceImpl(EtudeRepository etudeRepository
            ,AppelDoffreService appelDoffreService,
                            RestTemplate restTemplate){
        this.etudeRepository = etudeRepository;
        this.appelDoffreService = appelDoffreService;
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<EtudeEntity> creerEtude(EtudeEntity etude, long id, long appelDoffreId){
        UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+id,UserEntity.class);
        AppelDoffreEntity appelDoffre = appelDoffreService.getAppelDoffre(appelDoffreId);

        if (user != null && appelDoffre!=null){
            etude.setIdEmployee(user.getId());
            etude.setAppelDoffre(appelDoffre);
            return new ResponseEntity<EtudeEntity>(etudeRepository.save(etude), HttpStatus.CREATED);
        }else{
            return new ResponseEntity("this user or appel d'offre dos not exist",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<EtudeEntity> getAllEtudes() {
        return etudeRepository.findAll();
    }
//
    @Override
    @SneakyThrows
    public EtudeEntity getEtudeById(Long id) {
        return etudeRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Not found"));
    }

    @Override
    public ResponseEntity<EtudeEntity> updateEtude(Long id, EtudeEntity newEtude) {
        EtudeEntity etude = getEtudeById(id);
        if (etude != null){
            etude.setReferenceProjet(newEtude.getReferenceProjet());
            etude.setTitre(newEtude.getTitre());
            etude.setDatepost(newEtude.getDatepost());
            etude.setDescription(newEtude.getDescription());
            etude.setStatus(newEtude.getStatus());

            etudeRepository.save(etude);

            return new ResponseEntity(etude,HttpStatus.OK);

        }else{
            return new ResponseEntity("This Etude does not exist",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Optional<EtudeEntity> getEtudeByIdForResponse(long id) {
        return etudeRepository.findById(id);
    }

}
