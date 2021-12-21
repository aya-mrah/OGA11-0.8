package com.oga.stockservice.services;

import com.oga.stockservice.dto.ProjectEntity;
import com.oga.stockservice.entity.CommandeClient;
import com.oga.stockservice.entity.LigneDeCommande;
import com.oga.stockservice.entity.Product;
import com.oga.stockservice.repository.CommandeClientRepository;
import com.oga.stockservice.repository.ProductRepository;
import lombok.SneakyThrows;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeClienServiceImp implements CommandeClientService{

    private CommandeClientRepository commandeClientRepository;
    private RestTemplate restTemplate;
    private ProductRepository productRepository;

    @Autowired
    public CommandeClienServiceImp(CommandeClientRepository commandeClientRepository, RestTemplate restTemplate, ProductRepository productRepository) {
        this.commandeClientRepository = commandeClientRepository;
        this.restTemplate = restTemplate;
        this.productRepository = productRepository;
    }


    @Override
    public List<CommandeClient> getAllCommandeClient() {
        return commandeClientRepository.findAll();
    }

    @Override
    public List<CommandeClient> getAllCommandeClientLivree() {
        return commandeClientRepository.findByetatCommande("Livree");
    }

    @Override
    public List<CommandeClient> getAllCommandeClientValidee() {
        return commandeClientRepository.findByetatCommande("Validee");
    }

    @Override
    public List<CommandeClient> getAllCommandeClientEnAttente() {
        return  commandeClientRepository.findByetatCommande("En attente");
    }

    @Override
    @SneakyThrows
    public Optional<CommandeClient> getCommandeClientById(long id) {
        return commandeClientRepository.findById(id);
    }

    @Override
    public CommandeClient addCommande(long projetId, long logedUser, CommandeClient commandeClient,List<LigneDeCommande> ligneDeCommandes) {
        commandeClient.setEmployeeId(logedUser);
        double prixTotalSansTva=0;
        double prixTotalAvecTva=0;

       ProjectEntity projectEntity= restTemplate.getForObject("http://PROJET-SERVICE/api/project/"+projetId, ProjectEntity.class);
        if (projectEntity!=null){
            commandeClient.setIdProjet(projectEntity.getId());
        }
        commandeClient.setEmployeeId(logedUser);
       // commandeClient.setLigneDeCommandes();

        return commandeClientRepository.save(commandeClient);
    }

    @Override
    public Product addProductToCommande(long id,Product produit) {

        CommandeClient commandeClient = getCommandeClientById(id).get();
        if (commandeClient!=null){


        }
        return productRepository.save(produit);
    }



    @Override
    public CommandeClient updateCommandeClient(long id, CommandeClient newCommandeClient) {
        CommandeClient commandeClient = getCommandeClientById(id).get();
        if (commandeClient!=null){
            commandeClient.setCode(newCommandeClient.getCode());
            commandeClient.setDateCommande(newCommandeClient.getDateCommande());
            commandeClient.setEtatCommande(newCommandeClient.getEtatCommande());
        }
        return commandeClientRepository.save(commandeClient);
    }

    @Override
    public boolean deleteCommande(long id) {
        boolean result=false;
        CommandeClient commandeClient = getCommandeClientById(id).get();
        commandeClientRepository.delete(commandeClient);
        return true;
    }




}
