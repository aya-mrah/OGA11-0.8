package com.oga.stockservice.repository;

import com.oga.stockservice.entity.CommandeClient;
import com.oga.stockservice.entity.CommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur,Long> {

    List<CommandeFournisseur> findByetatCommande(String etatCommande);
}
