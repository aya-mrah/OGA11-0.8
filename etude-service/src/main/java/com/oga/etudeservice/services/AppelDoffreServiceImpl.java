package com.oga.etudeservice.services;

import com.oga.etudeservice.entity.AppelDoffreEntity;
import com.oga.etudeservice.entity.UserEntity;
import com.oga.etudeservice.repository.AppelDoffreRepository;
import com.oga.etudeservice.exeption.NotFoundExeption;
import com.oga.etudeservice.exeption.FileStorageExeption;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class AppelDoffreServiceImpl implements AppelDoffreService {

    private AppelDoffreRepository appelDoffreRepository;
    private RestTemplate restTemplate;

//    private UserService userService;

    public AppelDoffreServiceImpl(AppelDoffreRepository appelDoffreRepository,
                                  RestTemplate restTemplate
                                                     ) {
        this.appelDoffreRepository = appelDoffreRepository;
        this.restTemplate = restTemplate;

    }

    @Override
    public ResponseEntity<AppelDoffreEntity> AjouterAppelDoffre(AppelDoffreEntity appelDoffre, long id) {

        UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+id,UserEntity.class);


        if (user != null){
            appelDoffre.setIdEmployee(id);
//            appelDoffre.setUserEntity(user);
            return new ResponseEntity<AppelDoffreEntity>(appelDoffreRepository.save(appelDoffre),HttpStatus.CREATED);
        }else{
            return new ResponseEntity("this user dos not exist",HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public List<AppelDoffreEntity> getAllAppelDoffre() {
        return appelDoffreRepository.findAll();
    }
//
    @Override
    @SneakyThrows
    public AppelDoffreEntity getAppelDoffre(long id)  {
        return appelDoffreRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("this user dos not exist"+id));
    }
//
    @Override
    public ResponseEntity<AppelDoffreEntity> updateAppelDoffre(long id, AppelDoffreEntity newAppelDoffre) {

        AppelDoffreEntity appelDoffre = getAppelDoffre(id);
        if (appelDoffre != null){
            appelDoffre.setReference(newAppelDoffre.getReference());
            appelDoffre.setDateFin(newAppelDoffre.getDateFin());
            appelDoffre.setDatepost(newAppelDoffre.getDatepost());
            appelDoffre.setDescription(newAppelDoffre.getDescription());
            appelDoffre.setLienSite(newAppelDoffre.getLienSite());
            appelDoffre.setStatus(newAppelDoffre.getStatus());

            appelDoffreRepository.save(appelDoffre);

            return new ResponseEntity(appelDoffre,HttpStatus.OK);

        }else{
            return new ResponseEntity("this appel d'offre dosNot exist",HttpStatus.NOT_FOUND);
        }

    }
//
    @Override
    public ResponseEntity<AppelDoffreEntity> deleteAppelDoffre(long id) {
        AppelDoffreEntity appelDoffre = getAppelDoffre(id);
        appelDoffreRepository.delete(appelDoffre);
        return new ResponseEntity(appelDoffre,HttpStatus.OK);

    }
//
    @Override
    public ResponseEntity<AppelDoffreEntity> validerAppelDoffre(long id) {
        AppelDoffreEntity appelDoffre = getAppelDoffre(id);

        if (appelDoffre != null){

            appelDoffre.setStatus(1);
            return new ResponseEntity(appelDoffre,HttpStatus.OK);
        }else{
            return new ResponseEntity("Appel d'offre not found"+id,HttpStatus.NOT_FOUND);
        }

    }
//
    @Override
    public ResponseEntity<AppelDoffreEntity> refuserAppelDoffre(long id) {
        AppelDoffreEntity appelDoffre = getAppelDoffre(id);

        if (appelDoffre != null){

            appelDoffre.setStatus(2);
            return new ResponseEntity(appelDoffre,HttpStatus.OK);
        }else{
            return new ResponseEntity("Appel d'offre not found"+id,HttpStatus.NOT_FOUND);
        }

    }
//
    @Override
    public Optional<AppelDoffreEntity> getAppelDoffreByIdForResponse(long id) {
        return appelDoffreRepository.findById(id);
    }


}
