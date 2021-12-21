package com.oga.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeClientDto {
    private long id;
    private String code; //code référence commande
    private Instant dateCommande; //date de la création de commande
    private String etatCommande; //livrée , en attente , .....
    private long idProjet; //commande de matiére primére pour le projet x
    private long employeeId; //l'employée qui a ajouté cette commande pr notif
}
