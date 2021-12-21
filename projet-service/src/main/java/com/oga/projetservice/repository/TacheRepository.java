package com.oga.projetservice.repository;

import com.oga.projetservice.entity.TacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacheRepository extends JpaRepository<TacheEntity,Long> {
}
