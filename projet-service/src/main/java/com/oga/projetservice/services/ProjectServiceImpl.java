package com.oga.projetservice.services;

import com.oga.projetservice.entity.TacheEntity;
import com.oga.projetservice.entity.UserList;
import com.oga.projetservice.exeption.NotFoundExeption;
import com.oga.projetservice.entity.ProjectEntity;
import com.oga.projetservice.entity.UserEntity;
import com.oga.projetservice.repository.ProjectRepository;
import com.oga.projetservice.repository.TacheRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;
    private RestTemplate restTemplate;
    private TacheService tacheService;
    private TacheRepository tacheRepository;


    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository,RestTemplate restTemplate,
                               TacheService tacheService,TacheRepository tacheRepository) {
        this.projectRepository = projectRepository;
        this.restTemplate = restTemplate;
        this.tacheService = tacheService;
        this.tacheRepository = tacheRepository;

    }


    @Override
    public List<ProjectEntity> getAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public ProjectEntity addProject(long id, ProjectEntity project) {
        UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+id,UserEntity.class);

        List<Long> userList = new ArrayList<>();
        userList.add(user.getId());

        project.setUsers((ArrayList<Long>) userList);


        ProjectEntity persistedProject = projectRepository.save(project);
        List<Long> projectsId = new ArrayList<>();
        projectsId.add(persistedProject.getId());
        if (user.getProjects()==null){
            user.setProjects(projectsId);

        }else{
            user.getProjects().add(project.getId());
        }


        restTemplate.put("http://USER-SERVICE/api/updateUserProjects/"+user.getId(),user,UserEntity.class);



        return persistedProject;
    }

    @Override
    @SneakyThrows
    public ProjectEntity getProjectById(long id) {


        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("user not found" + id));

        return project;

    }
//
    @Override
    public ProjectEntity updateproject(long id, ProjectEntity project) {
        ProjectEntity oldProject = getProjectById(id);

        oldProject.setTitle(project.getTitle());
        oldProject.setDescription(project.getDescription());
        oldProject.setDateDebut(project.getDateDebut());

        return projectRepository.save(oldProject);
    }

    @Override
    public ResponseEntity<ProjectEntity> deleteProjetctById(long id) {

        ProjectEntity project = getProjectById(id);
        projectRepository.delete(project);

        return new ResponseEntity("le projet a bien été supprimer", HttpStatus.OK);
    }

    @Override
    public Optional<ProjectEntity> getprojectByIdForResponse(long id) {
        return projectRepository.findById(id);
    }
//
//    @Override
//    public List<UserDto> getAllUsersOfProject(long id) {
//        ProjectEntity project = getProjectById(id);
//
//        List<UserDto> userList = new ArrayList<>();
//        for (Iterator<UserEntity> i = project.getUsers().iterator(); i.hasNext(); ) {
//            UserEntity user = i.next();
//            if (user != null) {
//                UserDto userDto = new UserDto();
//                userDto.setId(user.getId());
//                userDto.setUserName(user.getUserName());
//                userDto.setEmail(user.getEmail());
//                userDto.setDepartement(userDto.getDepartement());
//                userDto.setNom(user.getNom());
//                userDto.setPrenom(user.getPrenom());
//                userDto.setTelephone(user.getTelephone());
//                userDto.setImage(user.getImage());
//                userDto.setProfileImage(user.getProfileImage());
//                userDto.setSex(user.getSex());
//                userDto.setRole(user.getRole());
//                userDto.setDisabled(user.isDisabled());
//
//                userList.add(userDto);
//            }
//
//        }
//        return userList;
//    }
//
    @Override
    public ProjectEntity addUsersToProject(long id, ArrayList<Long> users) {
        ProjectEntity project = getProjectById(id);

        List<Long> usersInProject = project.getUsers();

        if (project != null) {
            long i = 0;
            for (Iterator<Long> u = users.iterator(); u.hasNext(); ) {
                long user = u.next();
                if (user != 0) {
                    UserEntity utilisateur = restTemplate.getForObject("http://USER-SERVICE/api/user/"+user,UserEntity.class);

                    for (Long s : usersInProject){
                        if (s==utilisateur.getId()){
                            return project;
                        }

                    }
                    if (utilisateur!=null){
                        usersInProject.add(utilisateur.getId());
                    }

                    List<Long> projectsId = new ArrayList<>();
                    projectsId.add(project.getId());
                    if (utilisateur.getProjects()==null){
                        utilisateur.setProjects(projectsId);

                    }else{
                        utilisateur.getProjects().add(project.getId());
                    }


                    restTemplate.put("http://USER-SERVICE/api/updateUserProjects/"+user,utilisateur,UserEntity.class);


                }

            }
            project.setUsers((ArrayList<Long>) usersInProject);
            projectRepository.save(project);
        }
        return project;
    }
//
    @Override
    public List<TacheEntity> getTachesByUserIdAndProjectId(long userId, long projectId) {
        List<TacheEntity> listTache = new ArrayList<>();
        UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+userId,UserEntity.class);
       List<TacheEntity> listTacheInProject = getProjectById(projectId).getTaches();

       for (TacheEntity t : listTacheInProject){
           for (Long u : t.getUsersList()){
               if (user.getId()==u){
                   listTache.add(t);
               }
           }
       }

        return listTache;
    }
//
    @Override
    public List<UserEntity> getAllNonAffectedUserTopPoject(long projectId) {
        ArrayList<Long> usersInProject = getProjectById(projectId).getUsers();
        List<UserEntity> usersToRemove = new ArrayList<>();
        UserList users =  restTemplate.getForObject("http://USER-SERVICE/api/users/",UserList.class);


        for (Iterator<UserEntity> u = users.getUsers().iterator(); u.hasNext(); ) {
            UserEntity user = u.next();

            for (Iterator<Long> k = usersInProject.iterator(); k.hasNext(); ) {
                long userInProject = k.next();

                if (userInProject == user.getId()) {
                    usersToRemove.add(user);
                }
            }

        }
        users.getUsers().removeAll(usersToRemove);
        return users.getUsers();
    }
    @Override
    public List<UserEntity> getAllNonAffectedUserToTache(long tacheId) {
        TacheEntity tache = tacheService.getTacheById(tacheId);
        long projectId = tache.getProject().getId();
        List<Long> users = getProjectById(projectId).getUsers();
        List<UserEntity> usersToRemove = new ArrayList<>();
        List<UserEntity> usersToReturn = new ArrayList<>();


//        for (Iterator<Long> u = users.iterator(); u.hasNext(); ) {
//            long user = u.next();
//
//            usersToRemove.addAll(exist(user, tacheId));
//        }


        users.removeAll(usersToRemove);
        for (Iterator<Long> u = users.iterator(); u.hasNext(); ) {
            long user = u.next();
            UserEntity user1 =  restTemplate.getForObject("http://USER-SERVICE/api/user/"+user,UserEntity.class);
            usersToReturn.add(user1);
        }


        return usersToReturn;


    }
//
    @Override
    public ResponseEntity<ProjectEntity> deleteUserFromProject(long projectId, long userId) {
     ProjectEntity project = getProjectById(projectId);
        UserEntity user =restTemplate.getForObject("http://USER-SERVICE/api/user/"+userId,UserEntity.class);




             project.getUsers().remove(user.getId());
        projectRepository.save(project);
             user.getProjects().remove(project.getId());
             restTemplate.put("http://USER-SERVICE/api/updateUser/"+userId,user,UserEntity.class);






        return new ResponseEntity("user deleted from project",HttpStatus.OK);
    }

@Override
public ResponseEntity<TacheEntity> addTache(long id, TacheEntity tache) {
    ProjectEntity project = getProjectById(id);

    if (project != null){
        tache.setProject(project);
        return new ResponseEntity<TacheEntity>(tacheRepository.save(tache), HttpStatus.CREATED);
    }else{
        return new ResponseEntity("this project dos not exist",HttpStatus.NOT_FOUND);
    }
}

    @Override
    public ResponseEntity<List<ProjectEntity>> getProjectsByUserId(long id) {
        UserEntity user =  restTemplate.getForObject("http://USER-SERVICE/api/user/"+id,UserEntity.class);
        List<Long> projectsIdsOfUser = user.getProjects();
        List<ProjectEntity> userProjects = new ArrayList<>();

        if (projectsIdsOfUser!=null){

            for (Long p : projectsIdsOfUser){
               ProjectEntity projectEntity = getProjectById(p);
               userProjects.add(projectEntity);

            }
            return new ResponseEntity(userProjects,HttpStatus.OK);
        }else{
            return  new ResponseEntity("this user is not affected to any project",HttpStatus.NOT_FOUND);
        }




    }

    @Override
    public ProjectEntity updateCompteurNotif(long id) {
        ProjectEntity projet = getProjectById(id);
        projet.setTermine(true);
        return projet;
    }
//
//
}
