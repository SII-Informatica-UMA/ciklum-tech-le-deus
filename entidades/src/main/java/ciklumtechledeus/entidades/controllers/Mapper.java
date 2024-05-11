package ciklumtechledeus.entidades.controllers;

import ciklumtechledeus.entidades.dtos.DietaDTO;
import ciklumtechledeus.entidades.dtos.DietaNuevaDTO;
import ciklumtechledeus.entidades.entities.Dieta;

public class Mapper {
    public static DietaDTO toDietaDTO(Dieta dieta) {
        return DietaDTO.builder()
            .id(dieta.getId())
            .build();
    }

    
    public static Dieta toDieta(DietaNuevaDTO dietaNuevaDTO) {
        return ciklumtechledeus.entidades.entities.Dieta.builder()
            .nombre(dietaNuevaDTO.getNombre())
            .descripcion(dietaNuevaDTO.getDescripcion())
            .observaciones(dietaNuevaDTO.getObservaciones())
            .objetivo(dietaNuevaDTO.getObjetivo())
            .duracionDias(dietaNuevaDTO.getDuracionDias())
            .alimentos(dietaNuevaDTO.getAlimentos())
            .recomendaciones(dietaNuevaDTO.getRecomendaciones())
            .build();
    }

    public static Dieta toDieta2(DietaDTO dietaDTO) {
        return ciklumtechledeus.entidades.entities.Dieta.builder()
            .id(dietaDTO.getId())
            .build();
    }


}
