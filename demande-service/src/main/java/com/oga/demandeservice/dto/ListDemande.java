package com.oga.demandeservice.dto;

import com.oga.demandeservice.entity.DemandeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDemande {
    List<DemandeEntity> demandes;
}
