package com.oga.projetservice.services;

import com.oga.projetservice.entity.ProjectEntity;
import com.oga.projetservice.entity.QualityEntity;
import com.oga.projetservice.entity.UserEntity;
import com.oga.projetservice.repository.QualityRepository;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
//import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class QualityServiceImpl implements QualityService{

    private final QualityRepository qualityRepository;

    private final ProjectService projectService;
    private RestTemplate restTemplate;

    @Autowired
    public QualityServiceImpl(QualityRepository qualityRepository,ProjectService projectService,
                              RestTemplate restTemplate) {
        this.qualityRepository = qualityRepository;
        this.projectService = projectService;
        this.restTemplate = restTemplate;
    }



    @Override
    public List<QualityEntity> getAllQualityRapport() {
        return qualityRepository.findAll();
    }

    @Override
    public ResponseEntity<QualityEntity> creerQualityRapport(QualityEntity qualityEntity, Long id, long projectId) {
        UserEntity user =  restTemplate.getForObject("http://USER-SERVICE/api/user/"+id,UserEntity.class);
        ProjectEntity project = projectService.getProjectById(projectId);

        if (user != null || project!= null){
            qualityEntity.setIdEmployee(user.getId());
            qualityEntity.setProject(project);
            return new ResponseEntity<QualityEntity>(qualityRepository.save(qualityEntity), HttpStatus.CREATED);
        }else{
            return new ResponseEntity("this user or project dos not exist ",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @SneakyThrows
    public QualityEntity getQualityRapport(Long id) {
        return qualityRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Not found"));
    }

    @Override
    public ResponseEntity<QualityEntity> updateQualityRapport(Long id, QualityEntity newQualityRapport) {
        QualityEntity qualityEntity = getQualityRapport(id);
        if (qualityEntity != null){
            qualityEntity.setTitle(newQualityRapport.getTitle());
            qualityEntity.setDescription(newQualityRapport.getDescription());
            qualityEntity.setProjectReference(newQualityRapport.getProjectReference());


            qualityRepository.save(qualityEntity);

            return new ResponseEntity(qualityEntity,HttpStatus.OK);

        }else{
            return new ResponseEntity("This Rapport Quality does not exist",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Optional<QualityEntity> getQualityRapportByIdForResponse(long id) {
        return qualityRepository.findById(id);
    }

    @Override
    public QualityEntity addQualityRapport(QualityEntity qualityEntity) {
        return qualityRepository.save(qualityEntity);
    }
}