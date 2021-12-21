package com.oga.stockservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LigneDeCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

   private double Quantity;
   private double tva;
   private double prixAvecTva;
   private double ProduitQuantityPrix;
   private String produitTitre;


    @ManyToOne
    @JoinColumn(name = "id_commande")
    @JsonIgnore
    private CommandeClient commandeClient;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;


}

