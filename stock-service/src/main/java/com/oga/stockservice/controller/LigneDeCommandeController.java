package com.oga.stockservice.controller;

import com.oga.stockservice.entity.CommandeClient;
import com.oga.stockservice.entity.LigneDeCommande;
import com.oga.stockservice.entity.Product;
import com.oga.stockservice.services.CommandeClientService;
import com.oga.stockservice.services.LigneDeCommandeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class LigneDeCommandeController {

    private LigneDeCommandeService ligneDeCommandeService;

    public LigneDeCommandeController(LigneDeCommandeService ligneDeCommandeService) {
        this.ligneDeCommandeService = ligneDeCommandeService;
    }

//    @GetMapping("commandeClient")
//    public List<LigneDeCommande> getAllCommandeClient(){
//        return ligneDeCommandeService.getAllCommandeClient();
//    }



//    @GetMapping("commandeClient/{id}")
//    public Optional<LigneDeCommande> getCommandeClientById(@PathVariable(name = "id")long id){
//        return ligneDeCommandeService.getCommandeClientById(id);
//    }


//    @PostMapping("Projet/{projetId}/loggedUser/{logeduser}/commande")
//    public LigneDeCommande addCommande(@PathVariable(name = "projetId")long projetId,@PathVariable(name = "logeduser")long logeduser,@RequestBody LigneDeCommande commandeClient){
//        return ligneDeCommandeService.addCommande( projetId,logeduser,commandeClient);
//    }

    @PostMapping("ligneDeCommande")
    public LigneDeCommande addProduitToCommande(@RequestBody LigneDeCommande ligneDeCommande){

        return ligneDeCommandeService.addLigneDeCommande(ligneDeCommande);
    }


//    @PutMapping("commande/{id}")
//    public LigneDeCommande updateCommande(@PathVariable(name = "id")long id,@RequestBody LigneDeCommande newCommandeClient){
//        return ligneDeCommandeService.updateCommandeClient(id,newCommandeClient);
//    }
//
//    @DeleteMapping("Commande/{id}")
//    public boolean deleteCommandeClient(@PathVariable(name = "id")long id){
//        return ligneDeCommandeService.deleteCommande(id);
//    }
}
