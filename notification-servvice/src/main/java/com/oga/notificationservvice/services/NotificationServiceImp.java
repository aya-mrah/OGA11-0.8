package com.oga.notificationservvice.services;

import com.oga.notificationservvice.Repository.NotificationRepository;
import com.oga.notificationservvice.dto.NotifsAndCompteur;
import com.oga.notificationservvice.dto.TacheEntity;
import com.oga.notificationservvice.entity.NotificationEntity;
import com.oga.notificationservvice.entity.UserEntity;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImp implements NotificationService{
    @Autowired
    private NotificationRepository notificatioRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public NotificationEntity sendNotification(NotificationEntity notificationEntity) {
        return notificatioRepository.save(notificationEntity);
    }

    @Override
    public List<NotificationEntity> getAllNotification() {
        return notificatioRepository.findAll();
    }

    @Override
    @SneakyThrows
    public Optional<NotificationEntity> getNotificationById(long id) {
        return notificatioRepository.findById(id);
    }

    @Override
    public NotifsAndCompteur getAllNotificationByUserId(long userId) {


        UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+userId,UserEntity.class);
//        List<NotificationEntity> listNotificationByUserId = new ArrayList<>();
//        List<NotificationEntity> allnotificationlist = getAllNotification();
//        List<NotificationEntity> allNotificationsForAdmin = new ArrayList<>();



        if (user.getRole().equalsIgnoreCase("Chef de projet")){

            List<NotificationEntity> notifs3 =   notificatioRepository.findByType2("CreationProject");
            List<NotificationEntity> notifsTache = notificatioRepository.findByType2("CreationTache");
            List<NotificationEntity> notifAffectedUsersToProject = notificatioRepository.findByTypeAndType2("Project","Validation");
            List<NotificationEntity> notifAffectedUsersToTache = notificatioRepository.findByTypeAndType2("Tache","ValidationTache");

          List<NotificationEntity> notifValidationDemandeChef =  notificatioRepository.findByUserIdAndType2(user.getId(),"Validation");
            for (NotificationEntity n : notifs3){
                UserEntity user1 = restTemplate.getForObject("http://USER-SERVICE/api/user/"+userId,UserEntity.class);
                n.setNotifContenu(user1.getPrenom()+" "+user1.getNom()+" vous étes affecté au projet "+n.getNomProjet());
                updateNotification(n.getId(),n);
            }

            for (NotificationEntity n : notifAffectedUsersToProject){
                UserEntity user1 = restTemplate.getForObject("http://USER-SERVICE/api/user/"+n.getUserId(), UserEntity.class);

                    n.setNotifContenu(user1.getPrenom() + " " + user1.getNom() + " est affecté au projet " + n.getNomProjet());
                    updateNotification(n.getId(), n);


            }

            for (NotificationEntity n : notifAffectedUsersToTache){
                UserEntity user1 = restTemplate.getForObject("http://USER-SERVICE/api/user/"+n.getUserId(), UserEntity.class);

                    n.setNotifContenu(user1.getPrenom() + " " + user1.getNom() + " est affecté a la tache " + n.getNomTache());
                    updateNotification(n.getId(), n);


            }

            notifs3.addAll(notifValidationDemandeChef);
            notifs3.addAll(notifsTache);
            notifs3.addAll(notifAffectedUsersToProject);
            notifs3.addAll(notifAffectedUsersToTache);

            List<NotificationEntity> sortedList =  notifs3.stream()
                    .sorted(Comparator.comparing(NotificationEntity::getId))
                    .collect(Collectors.toList());

            return new NotifsAndCompteur(sortedList,user.getCompteurNotif()) ;

        }


        if (user.getRole().equalsIgnoreCase("Responsable")&&user.getDepartement().equalsIgnoreCase("Rh")){
            return new NotifsAndCompteur(notificatioRepository.findByTypeAndType2("Demande","Creation"), user.getCompteurNotif());
        }

        if (!user.getRole().equalsIgnoreCase("Responsable")){


            if (user.getDepartement().equalsIgnoreCase("Etude")){
                List<NotificationEntity> listAppelDoffreNotifs = notificatioRepository.findByTypeAndType2("AppelDoffre","Validation");
                listAppelDoffreNotifs.removeIf(n -> n.isAccepted() == false);
                return new NotifsAndCompteur(listAppelDoffreNotifs, user.getCompteurNotif());
            }


            return new NotifsAndCompteur(notificatioRepository.findByUserIdAndType2(user.getId(),"Validation"),user.getCompteurNotif());

        }


//


        List<NotificationEntity> listToRemove = new ArrayList<>();
        List<NotificationEntity> notifs1 = notificatioRepository.findByType2("Creation");

        List<NotificationEntity> notif2 =  notificatioRepository.findByType2("CreationProject");
        List<NotificationEntity> notifsTache = notificatioRepository.findByType2("CreationTache");
        List<NotificationEntity> notifAffectedUsersToProject = notificatioRepository.findByTypeAndType2("Project","Validation");
        List<NotificationEntity> notifAffectedUsersToTache = notificatioRepository.findByTypeAndType2("Tache","ValidationTache");

        for (NotificationEntity n : notifAffectedUsersToProject){
            UserEntity user1 = restTemplate.getForObject("http://USER-SERVICE/api/user/"+n.getUserId(), UserEntity.class);
            if (user.getRole().equalsIgnoreCase("Responsable") && user.getDepartement().equalsIgnoreCase("Administration") ) {
                n.setNotifContenu(user1.getPrenom() + " " + user1.getNom() + " est affecté au projet " + n.getNomProjet());
                updateNotification(n.getId(), n);
            }

        }


        for (NotificationEntity n : notif2) {
            UserEntity user1 = restTemplate.getForObject("http://USER-SERVICE/api/user/"+n.getUserId(), UserEntity.class);
            if (user.getRole().equalsIgnoreCase("Responsable") && user.getDepartement().equalsIgnoreCase("Administration")) {
                n.setNotifContenu(user1.getPrenom() + " " + user1.getNom() + " a crée le projet " + n.getNomProjet());
                updateNotification(n.getId(), n);
            }


        }
        for (NotificationEntity n : notifAffectedUsersToTache){
            UserEntity user1 = restTemplate.getForObject("http://USER-SERVICE/api/user/"+n.getUserId(), UserEntity.class);

            n.setNotifContenu(user1.getPrenom() + " " + user1.getNom() + " est affecté a la tache " + n.getNomTache());
            updateNotification(n.getId(), n);


        }
        notifs1.addAll(notif2);
        notifs1.addAll(notifsTache);
        notifs1.addAll(notifAffectedUsersToProject);
        notifs1.addAll(notifAffectedUsersToTache);
//        for (NotificationEntity n : notifs1){
//            if (n.getUserId()==userId){
//                listToRemove.add(n);
//            }
//        }


        List<NotificationEntity> sortedList =  notifs1.stream()
                .sorted(Comparator.comparing(NotificationEntity::getId))
                .collect(Collectors.toList());

        return new NotifsAndCompteur(sortedList, user.getCompteurNotif());
    }

    private NotificationEntity updateNotification(long id, NotificationEntity n) {
        Optional<NotificationEntity> notif = getNotificationById(id);

        return notificatioRepository.save(notif.get());


    }

//    @Override
//    public NotificationEntity updateClikedStatus(long id) {
//        Optional<NotificationEntity> notif = getNotificationById(id);
//        notif.get().setClicked(true);
//        return notif.get();
//    }
}
