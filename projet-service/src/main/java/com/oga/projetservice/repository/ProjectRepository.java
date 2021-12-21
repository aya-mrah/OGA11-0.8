package com.oga.projetservice.repository;

import com.oga.projetservice.entity.ProjectEntity;
import com.oga.projetservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {


}

