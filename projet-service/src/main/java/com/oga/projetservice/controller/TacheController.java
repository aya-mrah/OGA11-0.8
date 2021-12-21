package com.oga.projetservice.controller;

import com.oga.projetservice.dto.NotificationEntity;
import com.oga.projetservice.dto.TacheDto;
import com.oga.projetservice.dto.UserDto;
import com.oga.projetservice.entity.ProjectEntity;
import com.oga.projetservice.entity.TacheEntity;
import com.oga.projetservice.entity.UserEntity;
import com.oga.projetservice.entity.UserList;
import com.oga.projetservice.services.TacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api")
public class TacheController {

    public TacheService tacheService;
    private RestTemplate restTemplate;

    @Autowired
    public TacheController(TacheService tacheService, RestTemplate restTemplate) {
        this.tacheService = tacheService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("taches")
    public List<TacheEntity> getAllTaches() {
        return tacheService.getAllTaches();
    }

    @GetMapping("user/{id}/taches/inProject/{projectId}")
    public List<TacheEntity> getUserTaches(@PathVariable(value = "id")long id,@PathVariable(value = "id")long projectId){
        return tacheService.getTachesByUserId(id,projectId);
    }
//


    @GetMapping("tache/{id}")
    public ResponseEntity<TacheDto> getTacheById(@PathVariable(value = "id") long id){
        List<UserDto> userDtos = new ArrayList<>();
        Optional<TacheEntity> tache = tacheService.getTacheByIdForResponse(id);
        TacheEntity tacheEntity = tacheService.getTacheById(id);
        TacheDto tacheDto = new TacheDto();
        if (tacheEntity.getUsersList()!=null){
            for (Iterator<Long> i = tacheEntity.getUsersList().iterator(); i.hasNext();){
                long userId = i.next();
                UserEntity user =  restTemplate.getForObject("http://USER-SERVICE/api/user/"+userId,UserEntity.class);
                userDtos.add(new UserDto(user.getId(),
                        user.getUserName(),
                        user.getNom(),
                        user.getPrenom(),
                        user.getEmail(),
                        user.getImage(),
                        user.getProfileImage(),
                        user.getTelephone(),
                        user.isDisabled(),
                        user.getDepartement(),
                        user.getSex(),
                        user.getRole()

                ));
            }
        }else{
            return new ResponseEntity<>(tacheDto,HttpStatus.OK);
        }

        tacheDto.setId(tache.get().getId());
        tacheDto.setTitre(tache.get().getTitre());
        tacheDto.setDescription(tache.get().getDescription());
        tacheDto.setStatus(tache.get().getStatus());
        tacheDto.setDateDebut(tache.get().getDateDebut());
        tacheDto.setDateFin(tache.get().getDateFin());
        tacheDto.setDificulte(tache.get().getDificulte());
        tacheDto.setAvancement(tache.get().getAvancement());
        tacheDto.setProblem(tache.get().getProblem());
        tacheDto.setUsers(userDtos);
        if (tache.isPresent()){
            return new ResponseEntity(tacheDto, HttpStatus.OK);
        }else {
            return new ResponseEntity("Cette tache n'existe pas",HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("tache/{id}/users")
    public TacheEntity addTacheToUsers(@PathVariable(value = "id")long id, @RequestBody ArrayList<Long> users){
        TacheEntity persistedTache = tacheService.addUsersToTache(id,users);
        UserList usersList =  restTemplate.getForObject("http://USER-SERVICE/api/users",UserList.class);
        if(persistedTache!=null){
            for (Long u : users){
                UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+u,UserEntity.class);
                NotificationEntity notification = new NotificationEntity();
                notification.setNotifContenu(user.getPrenom()+" "+user.getNom()+" "+" vous étes affecté a la tache "+persistedTache.getTitre());
                notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
                notification.setImageUrl(user.getProfileImage());
                notification.setType("Tache");
                notification.setUserId(user.getId());
                notification.setType2("Validation");
                notification.setTacheId(persistedTache.getId());
                notification.setNomTache(persistedTache.getTitre());


                restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification,NotificationEntity.class);
                restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+user.getId(),UserEntity.class);
            }

            for (UserEntity u : usersList.getUsers()){
                if (u.getRole().equalsIgnoreCase("Responsable")&&u.getDepartement().equalsIgnoreCase("Administration")||u.getRole().equalsIgnoreCase("Chef de projet")){
                    restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+u.getId(),UserEntity.class);
                }
            }


        }
        return persistedTache;
    }



    @PutMapping("tache/{id}")
    public ResponseEntity<TacheEntity> updateTache(@PathVariable(value = "id")long id,TacheEntity newTache){
        if (newTache != null){
            return new ResponseEntity(tacheService.updateTache(id,newTache),HttpStatus.OK);
        }else{
            return new ResponseEntity("Vous devez remplir tous les champs ",HttpStatus.BAD_REQUEST);
        }
    }
//
    @DeleteMapping("tache/{id}")
    public ResponseEntity deleteTache(@PathVariable(value = "id")long id){
        boolean result = tacheService.deleteTache(id);

        if(result == true){
            return new ResponseEntity("Cette tache a été suppripmé avec succée",HttpStatus.OK);
        }else{
            return new ResponseEntity("tahce introuvable",HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("tache/{id}/user/{userId}")
    public ResponseEntity<TacheEntity> deleteUserFromProject(@PathVariable(name = "id")long id, @PathVariable(name = "userId")long userId){
        System.out.println(id+" "+userId);
        return tacheService.deleteUserFromTache(id,userId);
    }
    @PutMapping("avancementTache/{id}")
    public ResponseEntity<TacheEntity> avancementTache(@PathVariable(value = "id")long id,@RequestParam int avancement){
        if (avancement != 0){
            return new ResponseEntity(tacheService.updateAvancementTache(id,avancement),HttpStatus.OK);
        }else{
            return new ResponseEntity("Vous devez remplir tous les champs ",HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("blockageTache/{id}")
    public ResponseEntity<TacheEntity> blockageTache(@PathVariable(value = "id")long id,@RequestBody  String problem){
        if (problem != null){

            return new ResponseEntity(tacheService.updateProblem(id,problem),HttpStatus.OK);
        }else{
            return new ResponseEntity("Vous devez remplir tous les champs ",HttpStatus.BAD_REQUEST);
        }
    }
}
