package com.oga.demandeservice.controller;


import com.oga.demandeservice.dto.ListDemande;
import com.oga.demandeservice.dto.NotificationEntity;
import com.oga.demandeservice.dto.UserEntity;
import com.oga.demandeservice.dto.UsersList;
import com.oga.demandeservice.entity.DemandeEntity;
import com.oga.demandeservice.services.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class DemandeController {

  @Autowired
  private  DemandeService demandeService;
  @Autowired
  private RestTemplate restTemplate;

//  public DemandeController(DemandeService demandeService, UserService userService,NotificationsService notificationsService) {
//    this.demandeService = demandeService;
//    this.userService = userService;
//    this.notificationsService = notificationsService;
//  }

  @GetMapping("demandes")
  public List<DemandeEntity> getDemandes() {
    return demandeService.getDemandes();
  }

  @GetMapping("validedemandes")
  public List<DemandeEntity> getValideDemandes() {
    return demandeService.valideDemande();
  }

  @GetMapping("refuseddemandes")
  public List<DemandeEntity> getRefusedDemandes() {
    return demandeService.RefusedDemane();
  }

  @GetMapping("enattentedemandes")
  public List<DemandeEntity> getEnAttenteDemandes() {
    return demandeService.enAttenteDemane();
  }


  @GetMapping("demandeEmployee/{id}")
  public ListDemande getAllDemandesByEmployeeId(@PathVariable(name = "id")long id){

    ListDemande listDemande = new ListDemande(demandeService.findDemandeByIdEmploye(id));
    return listDemande;

  }
  @GetMapping("demande/{id}")
  public ResponseEntity<Optional<DemandeEntity>> getProjectByIdForResponse(@PathVariable(name = "id")long id){

    Optional<DemandeEntity> demandeEntity = demandeService.getDemandeByIdForResponse(id);
    if (demandeEntity.isPresent()){
      return new ResponseEntity(demandeEntity,HttpStatus.OK);
    }else{
      return new ResponseEntity("Demande introuvable",HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("demande/{id}")
  public DemandeEntity updateDemande(@RequestBody DemandeEntity demande,@PathVariable(value = "id") long id) {
    DemandeEntity oldDemande = demandeService.getDemandeByid(id);
    DemandeEntity persistedDemande = new DemandeEntity();

    oldDemande.setStatus(demande.getStatus());
    oldDemande.setCommentaireGrh(demande.getCommentaireGrh());
     persistedDemande = demandeService.ajouterDemande(oldDemande);
     //send validation notification
    UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+oldDemande.getIdEmploye(), UserEntity.class);
     if (persistedDemande.getStatus()!=0){
       NotificationEntity notification = new NotificationEntity();


       notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
       notification.setImageUrl(user.getImage());
       notification.setType("Demande");
       notification.setUserId(user.getId());
       notification.setType2("Validation");
       if (persistedDemande.getStatus()==1){
         notification.setAccepted(true);
         notification.setNotifContenu("Votre demande a été accepté");
       }
       if (persistedDemande.getStatus()==2){
         notification.setAccepted(false);
         notification.setNotifContenu("Votre demande a été refusé");
       }
       restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification, NotificationEntity.class);

       restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+user.getId(), UserEntity.class);
     }

    return persistedDemande;
  }

  @PutMapping("rhValide/{id}")
  public DemandeEntity rhValide(@PathVariable(value = "id") long id ) {
    return demandeService.rhValidation(id);
  }

  @PutMapping("rhrefus/{id}")
  public DemandeEntity rhrefus(@PathVariable(value = "id") long id ) {
    return demandeService.rhRefus(id);
  }

  @PostMapping("demande")
  public DemandeEntity addDemande(@RequestBody DemandeEntity demande ){
    return demandeService.addDemande(demande);



  }


}
