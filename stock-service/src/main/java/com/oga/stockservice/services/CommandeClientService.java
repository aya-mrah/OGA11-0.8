package com.oga.stockservice.services;

import com.oga.stockservice.entity.CommandeClient;
import com.oga.stockservice.entity.LigneDeCommande;
import com.oga.stockservice.entity.Product;

import java.util.List;
import java.util.Optional;

public interface CommandeClientService {

    List<CommandeClient> getAllCommandeClient();

    Optional<CommandeClient> getCommandeClientById(long id);

    CommandeClient addCommande(long projetId,long logeduser,CommandeClient commandeClient,List<LigneDeCommande> ligneDeCommandes);

    Product addProductToCommande(long idlogedUser,Product produit);



    CommandeClient updateCommandeClient(long id, CommandeClient newCommandeClient);

    boolean deleteCommande(long id);


    List<CommandeClient> getAllCommandeClientLivree();

    List<CommandeClient> getAllCommandeClientValidee();

    List<CommandeClient> getAllCommandeClientEnAttente();
}
