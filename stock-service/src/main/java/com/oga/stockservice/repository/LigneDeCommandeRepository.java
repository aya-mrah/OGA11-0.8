package com.oga.stockservice.repository;

import com.oga.stockservice.entity.LigneDeCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneDeCommandeRepository extends JpaRepository<LigneDeCommande,Long> {
}
