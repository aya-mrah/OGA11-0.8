package com.oga.supportservice.repository;


import com.oga.supportservice.entity.SupportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportRepository extends JpaRepository<SupportEntity,Long> {


}
