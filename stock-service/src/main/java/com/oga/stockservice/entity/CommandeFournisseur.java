package com.oga.stockservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CommandeFournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    @Column(name = "code")
    private String code; //code référence commande

    @Column(name = "datecommande")
    private Instant dateCommande; //date de la création de commande

    @Column(name = "etatcommande")

    private String etatCommande; //livrée , en attente , .....

    private long idProjet; //commande de matiére primére pour le projet x

    private long employeeId; //l'employée qui a ajouté cette commande pr notif

    private double totalPrice; //prix total de la commande

    private double totalPriceTva; //prix total de la commande

    //  @OneToMany(mappedBy = "commandeClient",
//          cascade = CascadeType.ALL,
//          orphanRemoval = true)
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_commande")
    private List<Product> produits = new ArrayList<Product>();
}
