package com.oga.userservice.services;

import com.oga.userservice.Entity.DemandeEntity;
import com.oga.userservice.Entity.UserEntity;
import com.oga.userservice.dto.ListDemande;
import com.oga.userservice.exeption.NotFoundExeption;
import com.oga.userservice.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;


@Service
public class UserServiceImpl implements UserService{



    private final UserRepository userRepository;



    private PasswordEncoder encoder;
    private RestTemplate restTemplate;



    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder encoder,
                           RestTemplate restTemplate) {
        this.userRepository = userRepository;

        this.restTemplate = restTemplate;
        this.encoder = encoder;
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

//        for (UserEntity ob : users){
//            if (ob.isStatus()==true){
//                users.remove(users.indexOf(ob));
//            }
//        }
        // get all active user without disabled
//        for (Iterator<UserEntity> i = users.iterator();i.hasNext();){
//            UserEntity user = i.next();
//            if (user.isDisabled()==true){
//                i.remove();
//            }
//        }

        return users;
    }

    @Override
    public List<UserEntity> getAllActifUsers() {
        List<UserEntity> users = userRepository.findAll();

//        for (UserEntity ob : users){
//            if (ob.isStatus()==true){
//                users.remove(users.indexOf(ob));
//            }
//        }
//         get all active user without disabled
        for (Iterator<UserEntity> i = users.iterator();i.hasNext();){
            UserEntity user = i.next();
            if (user.isDisabled()==true){
                i.remove();
            }
        }

        return users;
    }


    @SneakyThrows
    public UserEntity getUserByID(long id) {
        UserEntity user =  userRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("user not found"+id));

        return  user;
    }

    @Override
    public List<DemandeEntity> getUserValidatedConjé(long id) {
            ListDemande demandes = restTemplate.getForObject("http://DEMANDE-SERVICE/api/demandeEmployee/"+id,ListDemande.class);
            List<DemandeEntity> demandeEntities = demandes.getDemandes();
            for (Iterator<DemandeEntity> i = demandeEntities.iterator();i.hasNext();){
                DemandeEntity demande = i.next();
                if (demande.getStatus()!=1){
                    i.remove();
                }
            }

            return demandes.getDemandes();
    }

    @Override
    public List<DemandeEntity> getAllUserDemande(long id) {
        ListDemande demandes = restTemplate.getForObject("http://DEMANDE-SERVICE/api/demandeEmployee/"+id,ListDemande.class);
        return demandes.getDemandes();
    }

    @Override
    public List<DemandeEntity> getListdemandeEmployeeEnAttente(long id) throws NotFoundExeption, IOException {
        ListDemande demandes = restTemplate.getForObject("http://DEMANDE-SERVICE/api/demandeEmployee/"+id,ListDemande.class);
        List<DemandeEntity> demandeEntities = demandes.getDemandes();
        for (Iterator<DemandeEntity> i = demandeEntities.iterator();i.hasNext();){
            DemandeEntity demande = i.next();
            if (demande.getStatus()!=0){
                i.remove();
            }
        }


        return demandes.getDemandes();
    }


//        UserEntity user = userRepository.findById(id);
//        long userId = user.getId();
//        DemandeEntity demande = demandeService.getDemandeByid(userId);
//










    public UserEntity addUser(UserEntity user) {

        user.setUserName(user.getEmail());
       // user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }


    public UserEntity updateUser(UserEntity updateuser, long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(()-> new NotAcceptableStatusException("user not found"));
        user.setNom(updateuser.getNom());
        user.setPrenom(updateuser.getPrenom());
        user.setTelephone(updateuser.getTelephone());
        user.setEmail(updateuser.getEmail());
        user.setPassword(updateuser.getPassword());
        user.setProjects(updateuser.getProjects());
        user.setProjects(updateuser.getProjects());
        user.setTaches(updateuser.getTaches());
        userRepository.save(user);

        return user;
    }


    public UserEntity disableUser(long id) {
        UserEntity user = getUserByID(id);
        user.setDisabled(true);
        userRepository.save(user);
        return user;
    }


    public List<UserEntity> getAllDisabledUsers() {
        List<UserEntity> users = userRepository.findAll();

        for (Iterator<UserEntity> i = users.iterator();i.hasNext();){
            UserEntity user = i.next();
            if (user.isDisabled()==false){
                i.remove();
            }
        }

        return users;
    }


    private static String storageDirectoryPath = System.getProperty("user.dir") + "/images/";
    @Override
    public String uploadImage(MultipartFile file) {
        makeDirectoryIfNotExist(storageDirectoryPath);
        Path storageDirectory = Paths.get(storageDirectoryPath);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path destination = Paths.get(storageDirectory.toString() + "\\" + fileName);

        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);// we are Copying all bytes from an input stream to a file

        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/images/getImage/")
                .path(fileName)
                .toUriString();
        // return the download image url as a response entity
        String imageLink = destination.toString();
        return imageLink;
    }

    @Override
    public UserEntity getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    @Override
    public List<UserEntity> getAllChedDeProjet() {
        List<UserEntity> users = userRepository.findAll();

        for (Iterator<UserEntity> i = users.iterator();i.hasNext();){
            UserEntity user = i.next();
            if (!user.getRole().equalsIgnoreCase("Chef de projet")){
                i.remove();
            }
        }

        return users;
    }

    @Override
    public void deleteUser(long userId) {

        userRepository.deleteById(userId);
    }

    @Override
    public UserEntity updateCompteurNotif(long id) {
        UserEntity user = getUserByID(id);
        user.setCompteurNotif(0);
        return userRepository.save(user);
    }

    @Override
    public UserEntity updateCompteurNotifIncrementation(long id) {
        UserEntity user = getUserByID(id);
        int number=0;
        number = user.getCompteurNotif()+1;
        user.setCompteurNotif(number);

        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllOtherUsers(long id) {
        return userRepository.findByIdNotLike(id);
    }

    @Override
    public ResponseEntity<UserEntity> updateProjectsAffectationOfUser(long id, UserEntity user) {
        UserEntity userInDataBase = getUserByID(id);
        if (userInDataBase!=null){
            userInDataBase.setProjects(user.getProjects());
            return new ResponseEntity(userRepository.save(userInDataBase),HttpStatus.OK) ;
        }else {
            return new ResponseEntity("user dos not exist", HttpStatus.OK);
        }
    }

    private void makeDirectoryIfNotExist(String imageDirectory) {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
