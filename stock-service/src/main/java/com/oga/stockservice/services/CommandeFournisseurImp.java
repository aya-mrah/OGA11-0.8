package com.oga.stockservice.services;

import com.oga.stockservice.entity.CommandeFournisseur;
import com.oga.stockservice.entity.Product;
import com.oga.stockservice.repository.CommandeClientRepository;
import com.oga.stockservice.repository.CommandeFournisseurRepository;
import com.oga.stockservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeFournisseurImp implements CommandeFournisseurService{

    private CommandeFournisseurRepository commandeFournisseurRepository;
    private RestTemplate restTemplate;
    private ProductRepository productRepository;

    public CommandeFournisseurImp(CommandeFournisseurRepository commandeFournisseurRepository,
                                  RestTemplate restTemplate,
                                  ProductRepository productRepository) {
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.restTemplate = restTemplate;
        this.productRepository = productRepository;
    }

    @Override
    public List<CommandeFournisseur> getAllCommandeFournisseur() {
        return commandeFournisseurRepository.findAll();
    }

    @Override
    public Optional<CommandeFournisseur> getCommandeById(long id) {
        return  commandeFournisseurRepository.findById(id);
    }

    @Override
    public CommandeFournisseur addCommande(long projetId, long logeduser, CommandeFournisseur commandeClient) {
        return null;
    }

    @Override
    public Product addProductToCommande(long idlogedUser, Product produit) {
        return null;
    }

    @Override
    public CommandeFournisseur updateCommandeFournisseur(long id, CommandeFournisseur newCommandeClient) {
        return null;
    }

    @Override
    public boolean deleteCommande(long id) {
        return false;
    }

    @Override
    public List<CommandeFournisseur> getAllCommandeFournisseurLivree() {
        return commandeFournisseurRepository.findByetatCommande("Livree");
    }

    @Override
    public List<CommandeFournisseur> getAllCommandeFournisseurValidee() {
        return commandeFournisseurRepository.findByetatCommande("Validee");
    }

    @Override
    public List<CommandeFournisseur> getAllCommandeFournisseurEnAttente() {
        return commandeFournisseurRepository.findByetatCommande("En attente");
    }
}
