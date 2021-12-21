package com.oga.stockservice.controller;


import com.oga.stockservice.dto.NotificationEntity;
import com.oga.stockservice.dto.UserEntity;
import com.oga.stockservice.dto.UserList;
import com.oga.stockservice.entity.Product;
import com.oga.stockservice.repository.CategoryRepository;
import com.oga.stockservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The Class ProductResource.
 *
 * @author devrobot
 * @version 1.0
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProductResource {

    /**
     * The product repository.
     */

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private RestTemplate restTemplate;

    @Autowired
    public ProductResource(ProductRepository productRepository, CategoryRepository categoryRepository, RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Gets all products.
     *
     * @return all products
     */
    @GetMapping("/categorie/{categoryId}/product")
    public List<Product> getProductsByCategory(@PathVariable(value = "categoryId") long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @GetMapping("/product/{productId}")
    public Product getProductId(
            @PathVariable(value = "productId") long productId) throws DosNotExistExeption {
        Product user = productRepository.findById(productId)
                .orElseThrow(() -> new DosNotExistExeption("Product not found :: " + productId));
        return user;
    }


    @PostMapping("/product")
    public Product Createproduct(@RequestBody final Product product) {
        return productRepository.save(product);

    }

    @PostMapping("/categorie/{categoryId}/product")
    public Product createProduct(@PathVariable(value = "categoryId") long categoryId,
                                 @Valid @RequestBody Product product) throws DosNotExistExeption {
        Product persistedProduct = categoryRepository.findById(categoryId).map(category -> {
            product.setCategory(category);
            return productRepository.save(product);
        }).orElseThrow(() -> new DosNotExistExeption("category not found"));

        if (persistedProduct!=null){

            UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+persistedProduct.getEmployeeId(),UserEntity.class);
            NotificationEntity notification = new NotificationEntity();
            notification.setNotifContenu(user.getPrenom()+" "+user.getNom()+" "+"a ajouter le produit "+persistedProduct.getTitle());
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
        return persistedProduct;
    }


    @DeleteMapping(value = "/product")
    public List<Product> delete(@PathVariable long id) {
        productRepository.deleteById(id);
        return productRepository.findAll();
    }

    @PutMapping("/categorie/{categorieId}/product/{productId}")
    public Product updateProduct(@PathVariable(value = "categorieId") long categoryId,
                                 @PathVariable(value = "productId") long productId, @Valid @RequestBody Product productRequest)
            throws DosNotExistExeption {
        if (!categoryRepository.existsById(categoryId)) {
            throw new DosNotExistExeption("categoryId not found");
        }

        return productRepository.findById(productId).map(product -> {
            product.setTitle(productRequest.getTitle());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setWeight(productRequest.getWeight());
            product.setQuantity(productRequest.getQuantity());
            return productRepository.save(product);
        }).orElseThrow(() -> new DosNotExistExeption("product id not found"));
    }

    @DeleteMapping("/categorie/{categorieId}/product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "categorieId") int categoryId,
                                           @PathVariable(value = "productId") int productId) throws DosNotExistExeption {
        return productRepository.findByIdAndCategoryId(productId, categoryId).map(product -> {
            productRepository.delete(product);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new DosNotExistExeption(
                "product not found with id " + productId + " and categoryId " + categoryId));

    }
}
