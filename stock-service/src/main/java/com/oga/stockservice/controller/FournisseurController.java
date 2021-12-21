package com.oga.stockservice.controller;

import com.oga.stockservice.entity.Category;
import com.oga.stockservice.entity.Fournisseur;
import com.oga.stockservice.exeption.ResourceNotFoundException;
import com.oga.stockservice.repository.CategoryRepository;
import com.oga.stockservice.repository.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class FournisseurController {

    @Autowired
    private FournisseurRepository fournisseurRepository;


    @GetMapping("fournissseur")
    public List<Fournisseur> getAll() {
        return fournisseurRepository.findAll();
    }

    @GetMapping("fournissseur/{id}")
    public Optional<Fournisseur> getCategoryId(
            @PathVariable(value = "id") long fournisseurId)  {
        return fournisseurRepository.findById(fournisseurId);

    }

    @PostMapping("fournissseur")
    public Fournisseur createCategorie(@Valid @RequestBody Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    @DeleteMapping("api/fournissseur/{id}")
    public Map< String, Boolean > deleteUser(
            @PathVariable(value = "id") long fournisseurId) throws ResourceNotFoundException, DosNotExistExeption {

        Fournisseur category = fournisseurRepository.findById(fournisseurId)
                .orElseThrow(() -> new DosNotExistExeption("fournisseur not found :: " + fournisseurId));

        fournisseurRepository.delete(category);
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

//    @PutMapping("/categorie/{id}")
//    public ResponseEntity< Fournisseur > updateUser(
//            @PathVariable(value = "id") long categoryId,
//            @Valid @RequestBody Category userDetails) throws ResourceNotFoundException, DosNotExistExeption {
//        Category cat = fournisseurRepository.findById(categoryId)
//                .orElseThrow(() -> new DosNotExistExeption("Instructor not found :: " + categoryId));
//        cat.setName(userDetails.getName());
//        cat.setDescription(userDetails.getDescription());
//        final Category updatedUser = fournisseurRepository.save(cat);
//        return ResponseEntity.ok(updatedUser);
//    }
}
