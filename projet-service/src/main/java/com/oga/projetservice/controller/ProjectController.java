package com.oga.projetservice.controller;

import com.oga.projetservice.dto.NotificationEntity;
import com.oga.projetservice.dto.ProjectDto;
import com.oga.projetservice.dto.TacheDto;
import com.oga.projetservice.dto.UserDto;
import com.oga.projetservice.entity.ProjectEntity;
import com.oga.projetservice.entity.TacheEntity;
import com.oga.projetservice.entity.UserEntity;
import com.oga.projetservice.entity.UserList;
import com.oga.projetservice.services.ProjectService;
import com.oga.projetservice.services.TacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProjectController {

    private ProjectService projectService;
    private RestTemplate restTemplate;

    @Autowired
    public ProjectController(ProjectService projectService,
                             RestTemplate restTemplate) {
        this.projectService = projectService;

        this.restTemplate = restTemplate;
    }

    @GetMapping("project")
    public List<ProjectEntity> getAllProject(){
        return projectService.getAllProject();
    }

    @GetMapping("user/{id}/projects")
    public List<ProjectEntity> getUserTaches(@PathVariable(value = "id")long id){
        return projectService.getProjectsByUserId(id).getBody();
    }


    @GetMapping("project/{projectId}/user/{userId}/taches")
    public List<TacheEntity> getAllTaches(@PathVariable(value = "projectId")long projectId, @PathVariable(value = "userId")long userId){
        return projectService.getTachesByUserIdAndProjectId(userId,projectId);
    }

//    @GetMapping("projectAllUsers/{id}")
//    public List<UserDto> getAllprojectUsers(@PathVariable(value = "id")long id){
//        return projectService.getAllUsersOfProject(id);
//    }
    @GetMapping("nonAffectedUsersToProject/{id}")
    public List<UserEntity> getAllUsersNonAffected(@PathVariable(value = "id")long id){
        return projectService.getAllNonAffectedUserTopPoject(id);
    }
    @GetMapping("nonAffectedUsersToTache/{id}")
    public List<UserEntity> getAllUsersNonAffectedToTache(@PathVariable(value = "id")long id){
        return projectService.getAllNonAffectedUserToTache(id);
    }
////    @DeleteMapping("project/{projectId}/user/{userId}")
////    public ResponseEntity deleteUserFromProject(@PathVariable(value = "projectId")long projectId,@PathVariable(value = "userId")long userId){
////        projectService.deleteUserFromProject(projectId,userId);
////        return new ResponseEntity("User "+userId + " is deleted",HttpStatus.OK);
////    }

    @GetMapping("project/{id}")
    public ResponseEntity<ProjectDto> getProjectByIdForResponse(@PathVariable(name = "id")long id){

        Optional<ProjectEntity> projectEntity = projectService.getprojectByIdForResponse(id);
        ProjectEntity project = projectService.getProjectById(id);
        if (projectEntity.isPresent()){
            ProjectDto projectDto = new ProjectDto();
            projectDto.setId(project.getId());
            projectDto.setTheme((project.getTheme()));
            projectDto.setDateDebut(project.getDateDebut());
            projectDto.setDescription(project.getDescription());
            projectDto.setTitle(project.getTitle());
            UserDto user = new UserDto();

            List<UserDto> users = new ArrayList<>();
            List<TacheDto> taches = new ArrayList<>();
            List<UserDto> usersInTache = new ArrayList<>();

            for (Iterator<Long> i = project.getUsers().iterator(); i.hasNext();){
                long utilisateur = i.next();
                UserEntity userRest = restTemplate.getForObject("http://USER-SERVICE/api/user/"+utilisateur,UserEntity.class);

                users.add(new UserDto(userRest.getId(),
                        userRest.getUserName(),
                        userRest.getNom(),
                        userRest.getPrenom(),
                        userRest.getEmail(),
                        userRest.getImage(),
                        userRest.getProfileImage(),
                        userRest.getTelephone(),
                        userRest.isDisabled(),
                        userRest.getDepartement(),
                        userRest.getSex(),
                        userRest.getRole()

                ));
            }
            projectDto.setUserDto(users);

            for (Iterator<TacheEntity> i = project.getTaches().iterator(); i.hasNext();){
                TacheEntity tache = i.next();

                if(tache.getUsersList()==null){
                    System.out.println("fault");
                }else{
                    for (Iterator<Long> j = tache.getUsersList().iterator(); j.hasNext();) {
                        long userInTache = j.next();
                        UserEntity userRest = restTemplate.getForObject("http://USER-SERVICE/api/user/"+userInTache,UserEntity.class);
                        usersInTache.add(new UserDto(userRest.getId(),
                                userRest.getUserName(),
                                userRest.getNom(),
                                userRest.getPrenom(),
                                userRest.getEmail(),
                                userRest.getImage(),
                                userRest.getProfileImage(),
                                userRest.getTelephone(),
                                userRest.isDisabled(),
                                userRest.getDepartement(),
                                userRest.getSex(),
                                userRest.getRole()
                        ));
                    }
                }

                    taches.add(new TacheDto(tache.getId(),
                            tache.getTitre(),
                            tache.getDescription(),
                            tache.getStatus(),
                            tache.getDateDebut(),
                            tache.getDateFin(),
                            tache.getDificulte(),
                            tache.getAvancement(),
                            tache.getProblem(),

                            usersInTache
                    ));
            }

            projectDto.setTahces(taches);

            return new ResponseEntity(projectDto,HttpStatus.OK);
        }else{
            return new ResponseEntity("projet introuvable",HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("project/{id}/users")
    public ProjectEntity addProjectToUsers(@PathVariable(value = "id")long id, @RequestBody ArrayList<Long> users){
        ProjectEntity projectPersisted = projectService.addUsersToProject(id,users);
        UserList usersList =  restTemplate.getForObject("http://USER-SERVICE/api/users",UserList.class);
        if(projectPersisted!=null){
            for (Long u : users){
                UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+u,UserEntity.class);
                NotificationEntity notification = new NotificationEntity();
                notification.setNotifContenu(user.getPrenom()+" "+user.getNom()+" "+" vous étes affecté au projet "+projectPersisted.getTitle());
                notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
                notification.setImageUrl(user.getProfileImage());
                notification.setType("Project");
                notification.setUserId(user.getId());
                notification.setType2("Validation");
                notification.setNomProjet(projectPersisted.getTitle());
                notification.setProjetId(id);


                restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification,NotificationEntity.class);
                restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+u,UserEntity.class);

            }
            for (UserEntity u : usersList.getUsers()){
                if (u.getRole().equalsIgnoreCase("Responsable")&&u.getDepartement().equalsIgnoreCase("Administration")||u.getRole().equalsIgnoreCase("Chef de projet")){
                    restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+u.getId(),UserEntity.class);
                }
            }




        }
        return projectPersisted;

    }

    @PostMapping("projet/{id}/user/{logeUserId}/tache")
    public ResponseEntity<TacheEntity> addTache(@PathVariable(value = "id") long id,@PathVariable(value = "logeUserId") long logeUserId,@RequestBody TacheEntity tache){
        if (tache != null){
           TacheEntity persistedTache =  projectService.addTache(id,tache).getBody();
           ProjectEntity project = projectService.getProjectById(id);
            if(persistedTache!=null){

                UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+logeUserId,UserEntity.class);
                NotificationEntity notification = new NotificationEntity();
                notification.setNotifContenu(user.getPrenom()+" "+user.getNom()+" "+" a crée la tache "+persistedTache.getTitre()+" au projet "+project.getTitle());
                notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
                notification.setImageUrl(user.getProfileImage());
                notification.setType("Tache");
                notification.setUserId(logeUserId);
                notification.setType2("CreationTache");
                notification.setNomTache(persistedTache.getTitre());
                notification.setTacheId(persistedTache.getId());


                restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification,NotificationEntity.class);
                UserList users =  restTemplate.getForObject("http://USER-SERVICE/api/users/",UserList.class);

                for (UserEntity u : users.getUsers()){
                    if (u.getId()!=notification.getUserId()&&u.getDepartement().equalsIgnoreCase("Administration")){
                        restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+u.getId(),UserEntity.class);
                    }
                }
//                restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+notification.getChefDeProjetId(),UserEntity.class);

            }
            return new ResponseEntity(persistedTache,HttpStatus.OK);
        }else{
            return new ResponseEntity("vous devez remplir tous les champs",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("project/{userId}/logedUserId/{logedUserId}")
    public ResponseEntity<ProjectEntity> addProject(@PathVariable(value = "userId")long userId,@PathVariable(value = "userId")long logedUserId,@RequestBody ProjectEntity project){
        if (project!=null){

            logedUserId=1;
            ProjectEntity projectPersisted = projectService.addProject(userId,project);
            if(projectPersisted!=null){

                    UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+userId,UserEntity.class);
                    NotificationEntity notification = new NotificationEntity();
                notification.setNotifContenu(user.getPrenom()+" "+user.getNom()+" "+" vous étes affecté au projet "+projectPersisted.getTitle());
                    notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
                    notification.setImageUrl(user.getProfileImage());
                    notification.setType("Project");
                    notification.setUserId(logedUserId);
                    notification.setType2("CreationProject");
                    notification.setNomProjet(projectPersisted.getTitle());
                    notification.setProjetId(projectPersisted.getId());
                    notification.setChefDeProjetId(userId);

                    restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification,NotificationEntity.class);
                UserList users =  restTemplate.getForObject("http://USER-SERVICE/api/users/",UserList.class);

                for (UserEntity u : users.getUsers()){
                    if (u.getId()!=notification.getUserId()&&u.getDepartement().equalsIgnoreCase("Administration")){
                        restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+u.getId(),UserEntity.class);
                    }
                }
                restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+notification.getChefDeProjetId(),UserEntity.class);






            }

            return new ResponseEntity<ProjectEntity>(projectPersisted, HttpStatus.CREATED);
        }else{
            return new ResponseEntity("Vous devez remplir les information du projet",HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("project/{id}")
    public ResponseEntity<ProjectEntity> updateProject(@PathVariable(name = "id")long id,@RequestBody ProjectEntity newproject){
        ProjectEntity project = projectService.getProjectById(id);
        if (project!=null){
            return new ResponseEntity<ProjectEntity>(projectService.updateproject(id,newproject),HttpStatus.OK);
        }else{
            return new ResponseEntity("Projet introuvable",HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("project/{id}")
    public ResponseEntity<ProjectEntity> updateproject(@PathVariable(name = "id")long id){
        ProjectEntity project = projectService.getProjectById(id);
        if (project!=null){
            return new ResponseEntity(projectService.deleteProjetctById(id),HttpStatus.OK);
        }else{
            return new ResponseEntity("projet introuvable",HttpStatus.NOT_FOUND);
        }
    }
@PutMapping("projetTermine/{id}")
public ProjectEntity updateProjetTermine(@PathVariable(name = "id") long id){
    return projectService.updateCompteurNotif(id);

}
    @DeleteMapping("projet/{id}/user/{userId}")
    public ResponseEntity<ProjectEntity> deleteUserFromProject(@PathVariable(name = "id")long id,@PathVariable(name = "userId")long userId){
        System.out.println(id+" "+userId);
      return projectService.deleteUserFromProject(id,userId);
    }
}
