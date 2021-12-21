package com.oga.userservice.dto;

import com.oga.userservice.Entity.DemandeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDemande {
    List<DemandeEntity> demandes;
}
