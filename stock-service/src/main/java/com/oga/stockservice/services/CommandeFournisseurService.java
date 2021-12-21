package com.oga.stockservice.services;


import com.oga.stockservice.entity.CommandeFournisseur;
import com.oga.stockservice.entity.Product;

import java.util.List;
import java.util.Optional;

public interface CommandeFournisseurService {

    List<CommandeFournisseur> getAllCommandeFournisseur();

    Optional<CommandeFournisseur> getCommandeById(long id);

    CommandeFournisseur addCommande(long projetId, long logeduser, CommandeFournisseur commandeClient);

    Product addProductToCommande(long idlogedUser, Product produit);



    CommandeFournisseur updateCommandeFournisseur(long id, CommandeFournisseur newCommandeClient);

    boolean deleteCommande(long id);


    List<CommandeFournisseur> getAllCommandeFournisseurLivree();

    List<CommandeFournisseur> getAllCommandeFournisseurValidee();

    List<CommandeFournisseur> getAllCommandeFournisseurEnAttente();
}
