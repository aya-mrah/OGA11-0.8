package com.oga.stockservice.services;

import com.oga.stockservice.entity.LigneDeCommande;
import com.oga.stockservice.entity.Product;
import com.oga.stockservice.repository.LigneDeCommandeRepository;
import com.oga.stockservice.repository.ProductRepository;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LigneDeCommandeServiceImp implements LigneDeCommandeService {

    private ProductRepository productRepository;
    private LigneDeCommandeRepository ligneDeCommandeRepository;

    @Autowired
    public LigneDeCommandeServiceImp(ProductRepository productRepository, LigneDeCommandeRepository ligneDeCommandeRepository) {
        this.productRepository = productRepository;
        this.ligneDeCommandeRepository = ligneDeCommandeRepository;
    }

    @Override
    public LigneDeCommande addLigneDeCommande(LigneDeCommande ligneDeCommande) {
        Product product = productRepository.findByTitle(ligneDeCommande.getProduitTitre());
        ligneDeCommande.setPrixAvecTva(product.getPrice()-(product.getPrice()* ligneDeCommande.getTva())/100);
        ligneDeCommande.setProduitQuantityPrix(ligneDeCommande.getPrixAvecTva()*ligneDeCommande.getQuantity());
        ligneDeCommande.setProduct(product);
        return ligneDeCommandeRepository.save(ligneDeCommande);
    }
}
