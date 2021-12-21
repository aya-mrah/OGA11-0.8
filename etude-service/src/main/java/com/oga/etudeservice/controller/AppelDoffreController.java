package com.oga.etudeservice.controller;

import com.oga.etudeservice.dto.NotificationEntity;
import com.oga.etudeservice.dto.UserList;
import com.oga.etudeservice.entity.AppelDoffreEntity;
import com.oga.etudeservice.entity.UserEntity;
import com.oga.etudeservice.services.AppelDoffreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Api( description="API pour les opérations CRUD sur les Appel d'offre.")
@RestController
@RequestMapping("api")
public class AppelDoffreController {


    private AppelDoffreService appelDoffreService;
    private RestTemplate restTemplate;

    @Autowired
    AppelDoffreController( AppelDoffreService appelDoffreService,RestTemplate restTemplate){
        this.appelDoffreService = appelDoffreService;
        this.restTemplate = restTemplate;
    }


    @ApiOperation(value = "Récupère toutes les appels d'offre/retour objet appel d'offre voir model appel d'offre")
    @GetMapping("appelDoffres")
    public List<AppelDoffreEntity> getAllAppelDoffres(){
        return appelDoffreService.getAllAppelDoffre();
    }


    @ApiOperation(value = "Récupère une appel d'offre par id /input => id appel d'offre existant /retour objet appel d'offre")
    @GetMapping("appelDoffre/{id}")
    public ResponseEntity<Optional<AppelDoffreEntity>> getProjectByIdForResponse(@PathVariable(name = "id")long id){

        Optional<AppelDoffreEntity> appelDoffre = appelDoffreService.getAppelDoffreByIdForResponse(id);
        if (appelDoffre.isPresent()){
            return new ResponseEntity(appelDoffre,HttpStatus.OK);
        }else{
            return new ResponseEntity("Appel d'offre introuvable",HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "ajouter une appel d'offre / input id utilisateur + objet appel d'offre /ourput => objet appel d'offre ajouter")
    @PostMapping("user/{id}/appelDoffre")
    public ResponseEntity<AppelDoffreEntity> saveAppelDoffre(@RequestBody AppelDoffreEntity appelDoffre,@PathVariable(name = "id") long id){
         ResponseEntity<AppelDoffreEntity> appelDoffreEntity = appelDoffreService.AjouterAppelDoffre(appelDoffre,id);

        if (appelDoffreEntity!=null){

            UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+id,UserEntity.class);
            NotificationEntity notification = new NotificationEntity();
            notification.setNotifContenu(user.getPrenom()+" "+user.getNom()+" "+" a crée une appel d'offre");
            notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            notification.setImageUrl(user.getImage());
            notification.setType("AppelDoffre");
            notification.setUserId(user.getId());
            notification.setType2("Creation");

            restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification,NotificationEntity.class);
            UserList users =  restTemplate.getForObject("http://USER-SERVICE/api/users", UserList.class);
            for (UserEntity u : users.getUsers()){
                if (u.getRole().equalsIgnoreCase("Responsable")&&u.getDepartement().equalsIgnoreCase("Administration")){
                    restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+u.getId(),UserEntity.class);
                }
            }

      }
        return appelDoffreEntity;
    }

    @ApiOperation(value = "modification d'appel d'offre /input id appel d'offre + objet appel d'offre/output nouveau objet appel d'offre")
    @PutMapping("appelDoffre/{id}")
    public ResponseEntity<AppelDoffreEntity> updateAppelDoffre(@PathVariable(value = "id") long id , @RequestBody AppelDoffreEntity newAppelDoffre) {
        if (appelDoffreService.getAppelDoffre(id) != null) {

            ResponseEntity<AppelDoffreEntity>  persistedAppelDoffre = appelDoffreService.updateAppelDoffre(id,newAppelDoffre);
            UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+persistedAppelDoffre.getBody().getIdEmployee(), UserEntity.class);
            UserList userList = restTemplate.getForObject("http://USER-SERVICE/api/users/", UserList.class);
            if (persistedAppelDoffre.getBody().getStatus() != 0) {
                NotificationEntity notification = new NotificationEntity();


                notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
                notification.setImageUrl(user.getImage());
                notification.setType("AppelDoffre");
                notification.setUserId(user.getId());
                notification.setType2("Validation");
                if (persistedAppelDoffre.getBody().getStatus() == 1) {
                    notification.setAccepted(true);
                    notification.setNotifContenu("Appel d'offre accepté");
                }
                if (persistedAppelDoffre.getBody().getStatus() == 2) {
                    notification.setAccepted(false);
                    notification.setNotifContenu("Appel d'offre refusé");
                }

                restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/", notification, NotificationEntity.class);

                restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+notification.getUserId(),UserEntity.class);
                if (notification.getNotifContenu().equalsIgnoreCase("Appel d'offre accepté")){
                    for (UserEntity u : userList.getUsers()){
                        if (u.getDepartement().equalsIgnoreCase("Etude")){
                            restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+u.getId(),UserEntity.class);
                        }
                    }
                }


            }
            return persistedAppelDoffre;

            } else {
                return new ResponseEntity("this appel d'offre dos not exist", HttpStatus.NOT_FOUND);
            }

        }


    @ApiOperation(value = "supprimer défénitivement l'appel d 'offre /Input id appel d'offre /output appel d 'offre supprimer")
    @DeleteMapping("appelDoffre/{id}")
    public ResponseEntity<AppelDoffreEntity> deleteAppelDoffre (@PathVariable(value = "id") long id){
        if (appelDoffreService.getAppelDoffre(id)!=null){
            return appelDoffreService.deleteAppelDoffre(id);
        }else{
            return new ResponseEntity("this appel d'offre dos not exist", HttpStatus.NOT_FOUND);
        }

    }

    @ApiOperation(value = "Réfuser l'appel d'offre sans la supprimer (attribut status recoit 2) /Input id appel d'offre /Output objet appel d 'offre")
    @DeleteMapping("refusAppelDoffre/{id}")
    public ResponseEntity<AppelDoffreEntity> refusAppelDoffre(@PathVariable(value = "id")long id){
        if (appelDoffreService.getAppelDoffre(id)!=null){
            return appelDoffreService.refuserAppelDoffre(id);
        }else{
            return new ResponseEntity("this appel d'offre dos not exist", HttpStatus.NOT_FOUND);
        }

    }

    @ApiOperation(value = "Validation appel d'offre /Input id appel d'offre existant l'attribut status recoit 1/Output objet appel d'offre ")
    @GetMapping("valedAppelDoffre/{id}")
    public ResponseEntity<AppelDoffreEntity> validerAppelDoffre(@PathVariable(value = "id")long id){
        if (appelDoffreService.getAppelDoffre(id)!=null){
            return  appelDoffreService.validerAppelDoffre(id);
        }else{
            return new ResponseEntity("this appel d'offre dos not exist", HttpStatus.NOT_FOUND);
        }

    }
}
