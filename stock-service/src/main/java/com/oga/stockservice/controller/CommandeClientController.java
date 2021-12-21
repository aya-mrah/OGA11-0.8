package com.oga.stockservice.controller;

import com.oga.stockservice.entity.CommandeClient;
import com.oga.stockservice.entity.LigneDeCommande;
import com.oga.stockservice.entity.Product;
import com.oga.stockservice.services.CommandeClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class CommandeClientController {

    private CommandeClientService commandeClientService;

    public CommandeClientController(CommandeClientService commandeClientService) {
        this.commandeClientService = commandeClientService;
    }

    @GetMapping("commandeClient")
    public List<CommandeClient> getAllCommandeClient(){
        return commandeClientService.getAllCommandeClient();
    }

    @GetMapping("commandeClientLivree")
    public List<CommandeClient> getAllCommandeClientLivreé(){
        return commandeClientService.getAllCommandeClientLivree();
    }

    @GetMapping("commandeClientValideeEnAttenteLivraison")
    public List<CommandeClient> getAllCommandeClientValidee(){
        return commandeClientService.getAllCommandeClientValidee();
    }

    @GetMapping("commandeClientEnCours")
    public List<CommandeClient> getAllCommandeClientEnCours(){
        return commandeClientService.getAllCommandeClientEnAttente();
    }

    @GetMapping("commandeClient/{id}")
    public Optional<CommandeClient> getCommandeClientById(@PathVariable(name = "id")long id){
        return commandeClientService.getCommandeClientById(id);
    }


    @PostMapping("Projet/{projetId}/loggedUser/{logeduser}/commande")
    public CommandeClient addCommande(@PathVariable(name = "projetId")long projetId,@PathVariable(name = "logeduser")long logeduser,@RequestBody CommandeClient commandeClient,@RequestBody  List<LigneDeCommande> ligneDeCommandes){
        return commandeClientService.addCommande( projetId,logeduser,commandeClient,ligneDeCommandes);
    }

//    @PostMapping("commande/{id}/produit")
//    public Product addProduitToCommande(@PathVariable(name = "id")long id,@RequestBody Product produit){
//        return commandeClientService.addProductToCommande(id,produit);
//    }

    @PutMapping("commande/{id}")
    public CommandeClient updateCommande(@PathVariable(name = "id")long id,@RequestBody CommandeClient newCommandeClient){
        return commandeClientService.updateCommandeClient(id,newCommandeClient);
    }

    @DeleteMapping("Commande/{id}")
    public boolean deleteCommandeClient(@PathVariable(name = "id")long id){
        return commandeClientService.deleteCommande(id);
    }

}
