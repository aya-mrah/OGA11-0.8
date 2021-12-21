package com.oga.stockservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oga.stockservice.dto.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "commandeclient")
public class CommandeClient {

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

  @OneToMany(cascade = CascadeType.ALL,
          orphanRemoval = true)
  @JoinColumn(name = "id_commande")
  private List<LigneDeCommande> ligneDeCommandes = new ArrayList<LigneDeCommande>();

}
