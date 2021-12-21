package com.oga.projetservice.services;


import com.oga.projetservice.entity.ProjectEntity;
import com.oga.projetservice.entity.TacheEntity;
import com.oga.projetservice.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TacheService {

    public List<TacheEntity> getAllTaches();
//
    public TacheEntity getTacheById(long id);
//
//

//
    public ResponseEntity<TacheEntity> updateTache(long id, TacheEntity newTache);
//
    public Boolean deleteTache(long id);
//
    public Optional<TacheEntity> getTacheByIdForResponse(long id);
//
    public TacheEntity addUsersToTache(long id, ArrayList<Long> users);
//
//    public List<UserEntity> getUsersByTacheId(long id);
    List<TacheEntity> getTachesByUserId(long id,long projectId);

    ResponseEntity<TacheEntity> deleteUserFromTache(long id, long userId);

    TacheEntity updateAvancementTache(long id, int avancement);

    TacheEntity updateProblem(long id, String problem);

//

}
