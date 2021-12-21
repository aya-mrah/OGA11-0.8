package com.oga.supportservice.services;


import com.oga.supportservice.entity.SupportEntity;
import com.oga.supportservice.entity.UserEntity;
import com.oga.supportservice.repository.SupportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SupportServiceImp implements SupportService {
    private RestTemplate restTemplate;
    private SupportRepository supportRepository;


    @Autowired
    public SupportServiceImp(RestTemplate restTemplate, SupportRepository supportRepository)
                              {
        this.restTemplate = restTemplate;
        this.supportRepository = supportRepository;

    }

    @Override
    public ResponseEntity<SupportEntity> addTicket(long userId, SupportEntity supportEntity) {
        UserEntity user = restTemplate.getForObject("http://USER-SERVICE/api/user/"+userId,UserEntity.class);


        if (user != null){
            supportEntity.setIdEmployee(user.getId());
            supportEntity.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

            return new ResponseEntity<SupportEntity>(supportRepository.save(supportEntity), HttpStatus.CREATED);
        }else{
            return new ResponseEntity("this user  dos not exist",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<SupportEntity> getAllTickets() {
        return supportRepository.findAll();
    }



    @Override
    public Optional<SupportEntity> getTicketById(long ticketId) {
        return supportRepository.findById(ticketId);
    }

    @Override
    public SupportEntity updateTicket(long id, SupportEntity newSupportEntity) {
        SupportEntity supportEntity = getTicketById(id).get();
        if (supportEntity!=null){
            supportEntity.setReponse(newSupportEntity.getReponse());
            supportEntity.setStatus(newSupportEntity.isStatus());
        }

        return supportEntity;
    }

    @Override
    public List<SupportEntity> getAllTicketByIdUser(long id) {
        List<SupportEntity> list = new ArrayList<>();
        List<SupportEntity> listTicketByUserId = new ArrayList<>();
        list=getAllTickets();
        for (Iterator<SupportEntity> u = list.iterator(); u.hasNext(); ) {
            SupportEntity supportEntity = u.next();
            if (supportEntity.getIdEmployee()==id){
                listTicketByUserId.add(supportEntity);
            }
        }
        return listTicketByUserId;
    }

}
