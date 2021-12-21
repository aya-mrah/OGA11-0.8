package com.oga.supportservice.services;



import com.oga.supportservice.entity.SupportEntity;
import com.oga.supportservice.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface SupportService {
    ResponseEntity<SupportEntity> addTicket(long userId, SupportEntity supportEntity);
    List<SupportEntity> getAllTickets();

    Optional<SupportEntity> getTicketById(long ticketId);

    SupportEntity updateTicket(long id,SupportEntity newSupportEntity);

    List<SupportEntity> getAllTicketByIdUser(long id);


}
