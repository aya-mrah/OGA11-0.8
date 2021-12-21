package com.oga.stockservice.repository;

import com.oga.stockservice.entity.CommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeClientRepository extends JpaRepository<CommandeClient,Long> {
    List<CommandeClient> findByetatCommande(String etatCommande);
}
