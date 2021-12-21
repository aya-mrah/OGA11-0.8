package com.oga.etudeservice.controller;

import com.oga.etudeservice.dto.*;
import com.oga.etudeservice.entity.AppelDoffreEntity;
import com.oga.etudeservice.entity.EtudeEntity;
import com.oga.etudeservice.entity.UserEntity;
import com.oga.etudeservice.services.EtudeService;
import com.oga.etudeservice.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api")
public class EtudeController {

    private EtudeService etudeService;
    private ImageService imageService;
    private RestTemplate restTemplate;

    @Autowired
    public EtudeController(EtudeService etudeService, ImageService imageService,
                           RestTemplate restTemplate) {
        this.etudeService = etudeService;
        this.imageService = imageService;
        this.restTemplate = restTemplate;
    }




    @GetMapping("etudes")
    public List<EtudeEntity> getAllEtudes(){
        return etudeService.getAllEtudes();
    }
//
    @GetMapping("etude/{id}")
    public ResponseEntity<EtudeDto> getProjectByIdForResponse(@PathVariable(name = "id")long id){

        EtudeEntity etude = etudeService.getEtudeById(id);
        UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+etude.getIdEmployee(),UserEntity.class);
        AppelDoffreEntity appelDoffreEntity = etude.getAppelDoffre();
        AppelDoffreDto appelDoffreDto = new AppelDoffreDto(appelDoffreEntity.getId(),
                appelDoffreEntity.getReference());
        UserDto userDto = new UserDto(user.getId(),
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
                user.getRole());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(etude.getFileName())
                .toUriString();
        EtudeDto etudeDto = new EtudeDto(etude.getId(),
                etude.getReferenceProjet(),
                etude.getDatepost(),

                etude.getTitre(),
                etude.getDescription(),
                etude.getStatus(),
                etude.getFileName(),
                fileDownloadUri,
                userDto,
                appelDoffreDto);
        Optional<EtudeEntity> etude1 = etudeService.getEtudeByIdForResponse(id);
        if (etude1.isPresent()){

            return new ResponseEntity(etudeDto,HttpStatus.OK);
        }else{
            return new ResponseEntity("etude introuvable",HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("user/{id}/etude")
//    public ResponseEntity<EtudeEntity> saveEtude(@RequestBody EtudeEntity etude, @PathVariable(name = "id") long id){
//        return etudeService.creerEtude(etude,id);
//    }
    @RequestMapping(value = "/user/{id}/appelDoffre/{appelDoffreId}/etude", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity<EtudeCreationDto> addUser(@ModelAttribute EtudeEntity etude, @PathVariable(name = "id") long id, @PathVariable(name = "appelDoffreId") long appelDoffreId, MultipartFile file){

//        if (userService.getUserByUserName(user.getUserName())!=null){
//            return new ResponseEntity(new UserCreationDto(user,"null","this "),HttpStatus.BAD_REQUEST);
//        }
        String fileName = imageService.storeFile(file);

        etude.setFileName(fileName);
        EtudeEntity etudeEntity = etudeService.creerEtude(etude,id,appelDoffreId).getBody();
        if (etudeEntity!=null){

            UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+id,UserEntity.class);
            NotificationEntity notification = new NotificationEntity();
            notification.setNotifContenu(user.getPrenom()+" "+user.getNom()+" "+" a crée une etude");
            notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            notification.setImageUrl(user.getImage());
            notification.setType("Etude");
            notification.setUserId(user.getId());
            notification.setType2("Creation");

            System.out.println(notification);

//            Movie movie1 =  webClient.build()
//                    .get()
//                    .uri("http://localhost:8080/api/movie/"+rating.getMovieId())
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();


            restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification,NotificationEntity.class);
            UserList users =  restTemplate.getForObject("http://USER-SERVICE/api/users", UserList.class);
            for (UserEntity u : users.getUsers()){
                if (u.getRole().equalsIgnoreCase("Responsable")&&u.getDepartement().equalsIgnoreCase("Administration")){
                    restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+u.getId(),UserEntity.class);
                }
            }
        }

        return new ResponseEntity(new EtudeCreationDto(etudeEntity, ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(fileName)
                .toUriString(),"project succsusfully added to database"),HttpStatus.CREATED);
    }

    @PutMapping("etude/{id}")
    public ResponseEntity<EtudeEntity> updateEtude(@PathVariable(value = "id") long id , @RequestBody EtudeEntity newEtude){
        if (etudeService.getEtudeById(id)!=null){
            ResponseEntity<EtudeEntity> persistedEtude = etudeService.updateEtude(id,newEtude);


            //send validation notification
            UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+persistedEtude.getBody().getIdEmployee(), UserEntity.class);
            if (persistedEtude.getBody().getStatus()!=0){
                NotificationEntity notification = new NotificationEntity();


                notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
                notification.setImageUrl(user.getImage());
                notification.setType("Etude");
                notification.setUserId(user.getId());
                notification.setType2("Validation");
                if (persistedEtude.getBody().getStatus()==1){
                    notification.setAccepted(true);
                    notification.setNotifContenu("Votre etude a été accepté");
                }
                if (persistedEtude.getBody().getStatus()==2){
                    notification.setAccepted(false);
                    notification.setNotifContenu("Votre etude a été refusé");
                }

                restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification, NotificationEntity.class);
                restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+notification.getUserId(),UserEntity.class);
            }
            return persistedEtude;

        }else{
            return new ResponseEntity("Etude does not exist", HttpStatus.NOT_FOUND);
        }
    }


}
