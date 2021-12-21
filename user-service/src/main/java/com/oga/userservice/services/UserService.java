package com.oga.userservice.services;

import com.oga.userservice.Entity.DemandeEntity;
import com.oga.userservice.Entity.UserEntity;
import com.oga.userservice.exeption.NotFoundExeption;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface UserService {

    List<UserEntity> getAllUsers();

    List<UserEntity> getAllActifUsers();

    UserEntity getUserByID(long id);

//    List<UserEntity> getUserValidatedConjé();
//
    List<DemandeEntity> getUserValidatedConjé(long id);

    List<DemandeEntity> getAllUserDemande(long id);
//
//    UserEntity getUserEnAttendeConjé(long id);

List<DemandeEntity> getListdemandeEmployeeEnAttente(long id) throws NotFoundExeption, IOException;

    UserEntity addUser(UserEntity user);

    UserEntity updateUser(UserEntity updateuser, long id);

    UserEntity disableUser(long id);

    List<UserEntity> getAllDisabledUsers();

    String uploadImage(MultipartFile file);

    UserEntity getUserByUserName(String userName);
//
//
//
//    Optional<UserEntity> getUserByIdResponse(long id);



    List<UserEntity> getAllChedDeProjet();

    void deleteUser(long userId);

    public UserEntity updateCompteurNotif(long id);


    UserEntity updateCompteurNotifIncrementation(long id);

    List<UserEntity> getAllOtherUsers(long id);

    ResponseEntity<UserEntity> updateProjectsAffectationOfUser(long id, UserEntity user);
}
