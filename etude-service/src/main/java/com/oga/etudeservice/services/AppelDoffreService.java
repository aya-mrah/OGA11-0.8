package com.oga.etudeservice.services;

import com.oga.etudeservice.entity.AppelDoffreEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AppelDoffreService {

    public ResponseEntity<AppelDoffreEntity> AjouterAppelDoffre(AppelDoffreEntity appelDoffre, long id);

    public List<AppelDoffreEntity> getAllAppelDoffre();
//
    public AppelDoffreEntity getAppelDoffre(long id);

    public ResponseEntity<AppelDoffreEntity> updateAppelDoffre(long id , AppelDoffreEntity newAppelDoffre);
//
    public ResponseEntity<AppelDoffreEntity> deleteAppelDoffre(long id);
//
    public ResponseEntity<AppelDoffreEntity> validerAppelDoffre(long id);
//
    public ResponseEntity<AppelDoffreEntity> refuserAppelDoffre(long id);
    public Optional<AppelDoffreEntity> getAppelDoffreByIdForResponse(long id);
}
