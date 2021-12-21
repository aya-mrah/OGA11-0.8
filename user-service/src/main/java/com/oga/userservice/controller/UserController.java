package com.oga.userservice.controller;

import com.oga.userservice.Entity.DemandeEntity;
import com.oga.userservice.Entity.UserEntity;
import com.oga.userservice.dto.NotificationEntity;
import com.oga.userservice.dto.UserCreationDto;
import com.oga.userservice.dto.UserList;
import com.oga.userservice.exeption.NotFoundExeption;
import com.oga.userservice.services.ImageService;
import com.oga.userservice.services.UserService;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
@NoArgsConstructor
public class UserController {



    private UserService userService;


    private RestTemplate restTemplate;
    private ImageService imageService;

//    private WebClient.Builder webClient;

    @Autowired
    public UserController(UserService userService, ImageService imageService,RestTemplate restTemplate) {
        this.userService = userService;
        this.imageService = imageService;
        this.restTemplate = restTemplate;

    }

    @GetMapping("/home")
    public String greeting(){

        return "Hello mouez";
    }

    @GetMapping("users")
    public UserList getAllUsers(){

        return new UserList(userService.getAllUsers());
    }

//   @GetMapping("getUserValidatedConjé)
//    public List<UserEntity> getUserValidaedDemande(){
//       UserEntity user = userService.getUserValidatedConjé();
//
//       return user;
//       }

    @GetMapping("user/{id}")
    public UserEntity getUserById(@PathVariable(value = "id") long id) {

        return userService.getUserByID(id);
    }



    @GetMapping("allChefDeProjet")
    public List<UserEntity> getAllChefDeProjet(){
        return userService.getAllChedDeProjet();
    }

    @GetMapping("disabledusers")
    public List<UserEntity> getAllDisabledUser(){
        return userService.getAllDisabledUsers();
    }

    @GetMapping("actifusers")
    public List<UserEntity> getAllActifUsers(){
        return userService.getAllActifUsers();
    }

    @GetMapping("allOtherUsers/{id}")
    public List<UserEntity> getAllotherUserThanThisUserId(@PathVariable(value = "id") long id){
        return userService.getAllOtherUsers(id);
    }


    @GetMapping("listedemandesemployeeenattente/{id}")
    public List<DemandeEntity> getAllDemandeEnAttenteByEmployeeId(@PathVariable(value = "id") long id) throws NotFoundExeption, IOException {

        return userService.getListdemandeEmployeeEnAttente(id);
    }
    @GetMapping("listedemandesemployeevalider/{id}")
    public List<DemandeEntity> getAllDemandeValidéesByEmployeeId(@PathVariable(value = "id") long id) throws NotFoundExeption, IOException {

        return userService.getUserValidatedConjé(id);
    }
    @GetMapping("listedemandesemployee/{id}")
    public List<DemandeEntity> getAllUserDemande(@PathVariable(value = "id") long id) throws NotFoundExeption, IOException {

        return userService.getAllUserDemande(id);
    }



    @GetMapping(value = "/image")
    public @ResponseBody byte[] getImage() throws IOException {
        FileInputStream in = new FileInputStream("file:///C://Users//Mouez//Downloads//ERPCurentAjourdemo//ERPCurentAjour//ERPCurent//ERP//images//manel.PNG");
//        InputStream in = getClass()
//                .getResourceAsStream("file:///C://Users//Mouez//Downloads//ERPCurentAjourdemo//ERPCurentAjour//ERPCurent//ERP//images//manel.PNG");
        return IOUtils.toByteArray(in);
    }



    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity<UserCreationDto> addUser(@ModelAttribute   UserEntity user, MultipartFile file){

//        if (userService.getUserByUserName(user.getUserName())!=null){
//            return new ResponseEntity(new UserCreationDto(user,"null","this "),HttpStatus.BAD_REQUEST);
//        }
        String fileName = imageService.storeFile(file);
//        String profileImageName = imageService.storeFile(profileImageFile);

//        String profileImageName = null;

        user.setImage(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(fileName)
                .toUriString());
//        user.setProfileImage(ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/api/downloadFile/")
//                .path(profileImageName)
//                .toUriString());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity(new UserCreationDto(userService.addUser(user),"user succsusfully added to database"),HttpStatus.CREATED);
    }
    @GetMapping("downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        // Load file as Resource
        Resource resource = imageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;

            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("user/{userId}/demande")
    public ResponseEntity<DemandeEntity> ajouterDemande(@PathVariable(value = "userId") long userId, @RequestBody DemandeEntity demande) {

        
        UserEntity user = userService.getUserByID(userId);


        demande.setIdEmploye(user.getId());
        demande.setCommentaireGrh("azertyujgfd");
       DemandeEntity demandePersisted = restTemplate.postForObject("http://DEMANDE-SERVICE/api/demande",demande,DemandeEntity.class);
        if(demandePersisted!=null){
            NotificationEntity notification = new NotificationEntity();
            notification.setNotifContenu(user.getPrenom()+" "+user.getNom()+" "+" a crée une demande");
            notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            notification.setImageUrl(user.getProfileImage());
            notification.setType("Demande");
            notification.setUserId(user.getId());
            notification.setType2("Creation");

            System.out.println(notification);

//            Movie movie1 =  webClient.build()
//                    .get()
//                    .uri("http://localhost:8080/api/movie/"+rating.getMovieId())
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();


            restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification,NotificationEntity.class);

            List<UserEntity> users = getAllActifUsers();
            for (UserEntity u: users){
                if (u.getRole().equalsIgnoreCase("Responsable")){
                    userService.updateCompteurNotifIncrementation(u.getId());
                }
            }
        }




        return new ResponseEntity<DemandeEntity>(demandePersisted,HttpStatus.CREATED);
        //return new ResponseEntity(new DemandeDto(demande,"la Demande pour l'utilisateur "+user.getUserName()+" est crée avec succéss"),HttpStatus.CREATED);
//    DemandeEntity demandeuser = demandeService.ajouterDemande(demande);
//    ResponseEntity notification = new ResponseEntity<> (new DemandeDto(demandeService.ajouterDemande(demande),notificationsService.sendNotification(user.getId())), HttpStatus.OK);
//    System.out.println(notification);
//    return notification;
    }




    @RequestMapping(value = "user/{id}/updateCin", method = RequestMethod.PUT, consumes = { "multipart/form-data" })
    public UserEntity updateUserCin(@PathVariable(value = "id") long id, @ModelAttribute MultipartFile file){

        UserEntity foundUser = userService.getUserByID(id);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(imageService.storeFile(file))
                .toUriString();


        foundUser.setImage(fileDownloadUri);
        return userService.addUser(foundUser);
    }
    @RequestMapping(value = "user/{id}/updateProfileImage", method = RequestMethod.PUT, consumes = { "multipart/form-data" })
    public UserEntity updateUserProfileImage(@PathVariable(value = "id") long id, @ModelAttribute MultipartFile file){

        UserEntity foundUser = userService.getUserByID(id);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(imageService.storeFile(file))
                .toUriString();


        foundUser.setProfileImage(fileDownloadUri);
        return userService.addUser(foundUser);
    }

    @DeleteMapping("user/{id}")
    public UserEntity disableUser(@PathVariable(value = "id") long id){

        return userService.disableUser(id);
    }
    @PutMapping("compteurNotifAzero/{id}")
    public UserEntity updateNotifCompteur(@PathVariable(name = "id") long id){
        return userService.updateCompteurNotif(id);

    }
    @PutMapping("updateUserProjects/{id}")
    public UserEntity updateProjectsAffectationOfUser(@PathVariable(name = "id") long id,@RequestBody UserEntity user){
        return userService.updateProjectsAffectationOfUser(id,user).getBody();

    }
    @PutMapping("compteurNotifIncrementation/{id}")
    public UserEntity updateNotifCompteurIncrementation(@PathVariable(name = "id") long id){
        return userService.updateCompteurNotifIncrementation(id);

    }

    @PutMapping("updateUser/{id}")
    public UserEntity updateUser(@PathVariable(name = "id") long id,@RequestBody UserEntity user){
        return userService.updateUser(user,id);

    }
//    @DeleteMapping("deleteUseruser/{id}")
//    public ResponseEntity<UserEntity> deleeteUser(@PathVariable(value = "id") long id){
//
//        userService.deleteUser(id);
//        return new ResponseEntity("user is feleted",HttpStatus.OK);
//    }




}