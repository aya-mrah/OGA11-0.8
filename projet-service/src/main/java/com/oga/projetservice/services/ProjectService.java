package com.oga.projetservice.services;

import com.oga.projetservice.entity.ProjectEntity;
import com.oga.projetservice.entity.TacheEntity;
import com.oga.projetservice.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

    public List<ProjectEntity> getAllProject();

    public ProjectEntity addProject(long id,ProjectEntity project);

    public ProjectEntity getProjectById(long id);
//
    public ProjectEntity updateproject(long id, ProjectEntity project);
//
    public ResponseEntity<ProjectEntity> deleteProjetctById(long id);
//
    public Optional<ProjectEntity> getprojectByIdForResponse(long id);
//
//    List<UserDto> getAllUsersOfProject(long id);
//
    ProjectEntity addUsersToProject(long id, ArrayList<Long> users);
//
    List<TacheEntity> getTachesByUserIdAndProjectId( long projectId,long userId);
//
    List<UserEntity> getAllNonAffectedUserTopPoject(long projectId);


    List<UserEntity> getAllNonAffectedUserToTache(long tacheId);

    ResponseEntity<TacheEntity> addTache(long id, TacheEntity tache);
//
    ResponseEntity<ProjectEntity> deleteUserFromProject(long projectId, long userId);

    ResponseEntity<List<ProjectEntity>> getProjectsByUserId(long id);

    ProjectEntity updateCompteurNotif(long id);

}
