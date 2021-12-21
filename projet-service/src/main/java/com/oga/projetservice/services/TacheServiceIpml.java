package com.oga.projetservice.services;

import com.oga.projetservice.dto.TacheDto;
import com.oga.projetservice.entity.ProjectEntity;
import com.oga.projetservice.entity.TacheEntity;
import com.oga.projetservice.entity.UserEntity;
import com.oga.projetservice.entity.UserList;
import com.oga.projetservice.repository.ProjectRepository;
import com.oga.projetservice.repository.TacheRepository;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TacheServiceIpml implements TacheService{

    public TacheRepository tacheRepository;
//    public  ProjectService projectService;
    private RestTemplate restTemplate;
    private ProjectRepository projectRepository;


    @Autowired
    public TacheServiceIpml(TacheRepository tacheRepository,
                            RestTemplate restTemplate,
                             ProjectRepository projectRepository) {
        this.tacheRepository = tacheRepository;
        this.restTemplate = restTemplate;
        this.projectRepository = projectRepository;

    }

    @Override
    public List<TacheEntity> getAllTaches() {
        return tacheRepository.findAll();
    }
//
    @Override
    @SneakyThrows
    public TacheEntity getTacheById(long id) {
        return tacheRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Not found"));
    }




    @Override
    public ResponseEntity<TacheEntity> updateTache(long id, TacheEntity newTache) {
        TacheEntity tache = getTacheById(id);
        if (tache != null){
            tache.setTitre(newTache.getTitre());
            tache.setDescription(newTache.getDescription());
            tache.setStatus(newTache.getStatus());
            tache.setAvancement(newTache.getAvancement());
            tache.setDificulte(newTache.getDificulte());
            tache.setDateDebut(newTache.getDateDebut());
            tache.setDateFin(newTache.getDateFin());

            tacheRepository.save(tache);

            return new ResponseEntity(tache,HttpStatus.OK);

        }else{
            return new ResponseEntity("This Tache does not exist",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Boolean deleteTache(long id) {
        TacheEntity tache = getTacheById(id);
        Boolean result = false;
         tacheRepository.delete(tache);
         result = true;
         return result;
    }

    @Override
    public Optional<TacheEntity> getTacheByIdForResponse(long id) {
        return tacheRepository.findById(id);
    }

    @Override
    public TacheEntity addUsersToTache(long id, ArrayList<Long> users) {
        TacheEntity tache = getTacheById(id);

        List<Long> usersInTache = tache.getUsersList();

        if (tache != null) {
            long i = 0;
            for (Iterator<Long> u = users.iterator(); u.hasNext(); ) {
                long user = u.next();
                if (user != 0) {
                    UserEntity utilisateur = restTemplate.getForObject("http://USER-SERVICE/api/user/"+user,UserEntity.class);

                    for (Long s : usersInTache){
                        if (s==utilisateur.getId()){
                            return tache;
                        }

                    }
                    if (utilisateur!=null){
                        usersInTache.add(utilisateur.getId());
                    }

                    List<Long> tachesIds = new ArrayList<>();
                    tachesIds.add(tache.getId());
                    if (utilisateur.getTaches()==null){
                        utilisateur.setTaches(tachesIds);

                    }else{
                        utilisateur.getTaches().add(tache.getId());
                    }


                    restTemplate.put("http://USER-SERVICE/api/updateUser/"+user,utilisateur,UserEntity.class);
                }

            }
            tache.setUsersList((ArrayList<Long>) usersInTache);
            tacheRepository.save(tache);
        }
        return tache;
    }

    @Override
    public List<TacheEntity> getTachesByUserId(long id,long projectId) {
        UserEntity user =  restTemplate.getForObject("http://USER-SERVICE/api/user/"+id,UserEntity.class);
        List<TacheEntity> taches = projectRepository.findById(projectId).get().getTaches();

//        for (Iterator<TacheEntity> u = taches.iterator(); u.hasNext();){
//            TacheEntity tache = u.next();
//            for (Iterator<Long> i = tache.getUsersList().iterator(); i.hasNext();){
//                long userId = i.next();
//                if (userId == id){
//                    tacheOfUserId.add(tache);
//                }
//            }
//
//        }
        for (TacheEntity t : taches){
            for (Long u : t.getUsersList()){
                if (u!= user.getId()){
                    taches.remove(t);
                }else{
                    TacheEntity tache = getTacheById(t.getId());

                    taches.add(tache);
                }
            }
        }



        return taches;
    }

    @Override
    public ResponseEntity<TacheEntity> deleteUserFromTache(long id, long userId) {
        TacheEntity tache = getTacheById(id);
        UserEntity user =restTemplate.getForObject("http://USER-SERVICE/api/user/"+userId,UserEntity.class);

        tache.getUsersList().remove(user.getId());
        tacheRepository.save(tache);
        user.getTaches().remove(tache.getId());
        restTemplate.put("http://USER-SERVICE/api/updateUser/"+userId,user,UserEntity.class);






        return new ResponseEntity("user deleted from project",HttpStatus.OK);
    }

    @Override
    public TacheEntity updateAvancementTache(long id, int avancement) {
        TacheEntity tache = getTacheById(id);

        tache.setAvancement(avancement);
        tacheRepository.save(tache);
        return tache;
    }

    @Override
    public TacheEntity updateProblem(long id, String problem) {
        TacheEntity tache = getTacheById(id);

        tache.setProblem(problem);
        tache.setBlocke(true);
        tacheRepository.save(tache);
        return tache;

    }


//    @Override
//    public List<UserEntity> getUsersByTacheId(long id) {
//        List<UserEntity> users = new ArrayList<>();
//
//        TacheEntity tache = getTacheById(id);
//
//        users = tache.getUsers();
//
//        return users;
//    }
//
    //from project

    //end from projct

//    public List<UserEntity> exist(long userId, long tacheId){
//
//        List<UserEntity> listUsers = new ArrayList<>();
//        UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+userId,UserEntity.class);
//
//        boolean result=false;
//        for (Iterator<TacheEntity> u = user.getTaches().iterator(); u.hasNext(); ) {
//            TacheEntity tache = u.next();
//            if (tache.getId() == tacheId){
//                listUsers.add(user);
//            }
//        }
//        return listUsers;
//
//    }
}
