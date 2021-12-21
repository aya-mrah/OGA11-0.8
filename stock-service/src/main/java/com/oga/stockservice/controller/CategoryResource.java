package com.oga.stockservice.controller;



import com.oga.stockservice.dto.NotificationEntity;
import com.oga.stockservice.dto.UserEntity;
import com.oga.stockservice.dto.UserList;
import com.oga.stockservice.entity.Category;
import com.oga.stockservice.exeption.ResourceNotFoundException;
import com.oga.stockservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The Class CategoryResource.
 *
 * @author devrobot
 * @version 1.0
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CategoryResource {

    /** The category repository. */

    private CategoryRepository categoryRepository;
    private RestTemplate restTemplate;

    @Autowired
    public CategoryResource(CategoryRepository categoryRepository, RestTemplate restTemplate) {
        this.categoryRepository = categoryRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Gets all categories.
     *
     * @return all categories
     */
    @GetMapping("categories")
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
    /**
     * Gets category.
     *
     * @return category if exists
     */
    @GetMapping("/categorie/{id}")
    public Optional<Category> getCategoryId(
            @PathVariable(value = "id") long categoryId)  {
        return categoryRepository.findById(categoryId);

    }

    @PostMapping("/categorie")
    public Category createCategorie(@Valid @RequestBody Category categorie) {
        Category persistedCategory = categoryRepository.save(categorie);

        if (persistedCategory!=null){

            UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+persistedCategory.getEmployeeId(),UserEntity.class);
            NotificationEntity notification = new NotificationEntity();
            notification.setNotifContenu(user.getPrenom()+" "+user.getNom()+" "+"a ajouter la catégorie "+persistedCategory.getName());
            notification.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            notification.setImageUrl(user.getImage());
            notification.setType("Stock");
            notification.setUserId(user.getId());
            notification.setType2("Creation");


//            Movie movie1 =  webClient.build()
//                    .get()
//                    .uri("http://localhost:8080/api/movie/"+rating.getMovieId())
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();


            restTemplate.postForObject("http://NOTIFICATION-SERVICE/api/notification/",notification,NotificationEntity.class);

          UserList users =  restTemplate.getForObject("http://USER-SERVICE/api/users",UserList.class);
          for (UserEntity u : users.getUsers()){
              if (u.getRole().equalsIgnoreCase("Responsable")&&u.getDepartement().equalsIgnoreCase("Administration")){
                  restTemplate.put("http://USER-SERVICE/api/compteurNotifIncrementation/"+u.getId(),UserEntity.class);
              }
          }
        }
        return persistedCategory;
    }
    /**
     * Delete category.
     *
     */
    @DeleteMapping("/categorie/{id}")
    public Map< String, Boolean > deleteUser(
            @PathVariable(value = "id") long categoryId) throws ResourceNotFoundException, DosNotExistExeption {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DosNotExistExeption("Category not found :: " + categoryId));

        categoryRepository.delete(category);
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    /**
     * Put category.
     */
    @PutMapping("/categorie/{id}")
    public ResponseEntity < Category > updateUser(
            @PathVariable(value = "id") long categoryId,
            @Valid @RequestBody Category userDetails) throws ResourceNotFoundException, DosNotExistExeption {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DosNotExistExeption("Instructor not found :: " + categoryId));
        cat.setName(userDetails.getName());
        cat.setDescription(userDetails.getDescription());
        final Category updatedUser = categoryRepository.save(cat);
        return ResponseEntity.ok(updatedUser);
    }
}
