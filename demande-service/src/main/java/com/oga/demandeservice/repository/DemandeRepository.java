package com.oga.demandeservice.repository;


import com.oga.demandeservice.entity.DemandeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<DemandeEntity,Long> {
    List<DemandeEntity> findAllByidEmploye(long idEmploye);
}
