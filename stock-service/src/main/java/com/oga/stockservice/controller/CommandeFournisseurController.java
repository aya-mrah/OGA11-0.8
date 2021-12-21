//package com.oga.stockservice.controller;
//
//import com.oga.stockservice.entity.CommandeClient;
//import com.oga.stockservice.entity.CommandeFournisseur;
//import com.oga.stockservice.entity.Product;
//import com.oga.stockservice.services.CommandeClientService;
//import com.oga.stockservice.services.CommandeFournisseurService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("api")
//public class CommandeFournisseurController {
//
//    private CommandeFournisseurService commandeFournisseurService;
//
//
//
//    @GetMapping("commandeClient")
//    public List<CommandeFournisseur> getAllCommandeClient(){
//        return commandeFournisseurService.getAllCommandeFournisseur();
//    }
//
//    @GetMapping("commandeClientLivree")
//    public List<CommandeFournisseur> getAllCommandeClientLivreé(){
//        return commandeFournisseurService.getAllCommandeFournisseurLivree();
//    }
//
//    @GetMapping("commandeClientValideeEnAttenteLivraison")
//    public List<CommandeFournisseur> getAllCommandeClientValidee(){
//        return commandeFournisseurService.getAllCommandeFournisseurValidee();
//    }
//
//    @GetMapping("commandeClientEnCours")
//    public List<CommandeFournisseur> getAllCommandeClientEnCours(){
//        return commandeFournisseurService.getAllCommandeFournisseurEnAttente();
//    }
//
//    @GetMapping("commandeClient/{id}")
//    public Optional<CommandeFournisseur> getCommandeClientById(@PathVariable(name = "id")long id){
//        return commandeFournisseurService.getCommandeById(id);
//    }
//
//
//    @PostMapping("Projet/{projetId}/loggedUser/{logeduser}/commande")
//    public CommandeFournisseur addCommande(@PathVariable(name = "projetId")long projetId,@PathVariable(name = "logeduser")long logeduser,@RequestBody CommandeFournisseur commandeClient){
//        return commandeFournisseurService.addCommande( projetId,logeduser,commandeClient);
//    }
//
//    @PostMapping("commande/{id}/produit")
//    public Product addProduitToCommande(@PathVariable(name = "id")long id, @RequestBody Product produit){
//        return commandeFournisseurService.addProductToCommande(id,produit);
//    }
//
//    @PutMapping("commande/{id}")
//    public CommandeFournisseur updateCommande(@PathVariable(name = "id")long id, @RequestBody CommandeFournisseur newCommandeClient){
//        return commandeFournisseurService.updateCommandeFournisseur(id,newCommandeClient);
//    }
//
//    @DeleteMapping("Commande/{id}")
//    public boolean deleteCommandeClient(@PathVariable(name = "id")long id){
//        return commandeFournisseurService.deleteCommande(id);
//    }
//}
