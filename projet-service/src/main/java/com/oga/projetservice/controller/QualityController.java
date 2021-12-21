package com.oga.projetservice.controller;

import com.oga.projetservice.dto.NotificationEntity;
import com.oga.projetservice.dto.QualityCreationDto;
import com.oga.projetservice.dto.QualityPostDto;
import com.oga.projetservice.dto.UserDto;
import com.oga.projetservice.entity.ProjectEntity;
import com.oga.projetservice.entity.QualityEntity;
import com.oga.projetservice.entity.UserEntity;
import com.oga.projetservice.entity.UserList;
import com.oga.projetservice.services.ImageService;
import com.oga.projetservice.services.ProjectService;
import com.oga.projetservice.services.QualityService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
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
@NoArgsConstructor

public class QualityController {

    private QualityService qualityService;
    private ImageService imageService;
    private RestTemplate restTemplate;
    private ProjectService projectService;

    @Autowired
    public QualityController(QualityService qualityService,ImageService imageService,
                             RestTemplate restTemplate,
                             ProjectService projectServic) {
        this.qualityService = qualityService;
        this.imageService = imageService;
        this.restTemplate = restTemplate;
        this.projectService = projectServic;
    }

    @GetMapping("qualityRapports")
    public List<QualityEntity> getAllQualityRapport(){
        return qualityService.getAllQualityRapport();
    }

    @GetMapping("qualityRapport/{id}")
    public ResponseEntity<Optional<QualityEntity>> getEtudeByIdForResponse(@PathVariable(name = "id")long id){

        QualityEntity qualityEntity = qualityService.getQualityRapport(id);
        UserEntity user =  restTemplate.getForObject("http://USER-SERVICE/api/user/"+qualityEntity.getIdEmployee(),UserEntity.class);
//

        Optional<QualityEntity> quality = qualityService.getQualityRapportByIdForResponse(id);

//       UserEntity user = etude.get().getUserEntity();
//        AppelDoffreEntity appelDoffreEntity = etude.get().getAppelDoffre();
        if (quality.isPresent()){

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/downloadFile/")
                    .path(quality.get().getFileName())
                    .toUriString();

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUserName(user.getUserName());
            userDto.setNom(user.getNom());
            userDto.setPrenom(userDto.getPrenom());
            userDto.setEmail(user.getEmail());
            userDto.setImage(user.getImage());
            userDto.setProfileImage(user.getProfileImage());
            userDto.setTelephone(user.getTelephone());
            userDto.setDisabled(false);
            userDto.setDepartement(user.getDepartement());
            userDto.setSex(user.getSex());
            userDto.setRole(user.getRole());

            return new ResponseEntity(new QualityCreationDto(
                    qualityEntity.getId(),
                    qualityEntity.getProjectReference(),
                    qualityEntity.getDescription(),
                    qualityEntity.getTitle(),
                    qualityEntity.getFileName(),
                    qualityEntity.getProject().getTitle(),
                    userDto,
                    fileDownloadUri
            ),HttpStatus.OK);
        }else{
            return new ResponseEntity("Quality Rapport introuvable",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user/{id}/project/{projectId}/QualityRapport", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity<QualityCreationDto> addQualityrapport(@ModelAttribute QualityEntity qualityEntity, @PathVariable(name = "id") long id, @PathVariable(name = "projectId") long projectId, MultipartFile file){

//        if (userService.getUserByUserName(user.getUserName())!=null){
//            return new ResponseEntity(new UserCreationDto(user,"null","this "),HttpStatus.BAD_REQUEST);
//        }
        String fileName = imageService.storeFile(file);

        qualityEntity.setFileName(fileName);

        QualityEntity rapportPerseisted = qualityService.creerQualityRapport(qualityEntity,id,projectId).getBody();
        UserList usersList =  restTemplate.getForObject("http://USER-SERVICE/api/users",UserList.class);
        ProjectEntity project = projectService.getProjectById(projectId);
        if(rapportPerseisted!=null){

                UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+id,UserEntity.class);
                NotificationEntity notification = new NotificationEntity();
                notification.setNotifContenu(user.getPrenom()+" "+user.getNom()+" "+" a crée l' étude "+rapportPerseisted.getTitle()+" pour le projet "+project.getTitle());
                notification.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                notification.setImageUrl(user.getProfileImage());
                notification.setType("Rapport");
                notification.setUserId(user.getId());
                notification.setType2("Creation");

                restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification,NotificationEntity.class);

            for (UserEntity u : usersList.getUsers()){
                if (u.getRole().equalsIgnoreCase("Responsable")&&u.getDepartement().equalsIgnoreCase("Administration")||u.getRole().equalsIgnoreCase("Chef de projet")){
                    restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+u.getId(),UserEntity.class);
                }
            }





        }


        return new ResponseEntity(new QualityPostDto(rapportPerseisted, ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(fileName)
                .toUriString()),HttpStatus.CREATED);
    }

    @PutMapping("qualityRapport/{id}")
    public ResponseEntity<QualityEntity> updateQualityRapport(@PathVariable(value = "id") long id , @RequestBody QualityEntity newQuality){
        if (qualityService.getQualityRapport(id)!=null){
            return qualityService.updateQualityRapport(id,newQuality);
        }else{
            return new ResponseEntity("Quality Rapport does not exist", HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value = "qualityRapport/{id}/updateQualityRapportFile", method = RequestMethod.PUT, consumes = { "multipart/form-data" })
    public QualityEntity updateQualityRapportFile(@PathVariable(value = "id") long id, @ModelAttribute MultipartFile file){

        QualityEntity foundQualityRapport = qualityService.getQualityRapport(id);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(imageService.storeFile(file))
                .toUriString();


        foundQualityRapport.setFileName(fileDownloadUri);
        return qualityService.addQualityRapport(foundQualityRapport);
    }
}