package com.oga.etudeservice.repository;

import com.oga.etudeservice.entity.AppelDoffreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppelDoffreRepository extends JpaRepository<AppelDoffreEntity,Long> {
}
