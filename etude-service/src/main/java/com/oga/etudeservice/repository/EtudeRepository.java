package com.oga.etudeservice.repository;

import com.oga.etudeservice.entity.EtudeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtudeRepository extends JpaRepository<EtudeEntity, Long> {
}
