package ciklumtechledeus.entidades.controllers;

import ciklumtechledeus.entidades.dtos.DietaDTO;
import ciklumtechledeus.entidades.dtos.DietaNuevaDTO;
import ciklumtechledeus.entidades.entities.Dieta;

public class Mapper {

    
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

    public static DietaDTO toDietaDTO(Dieta dieta) {
        return DietaDTO.builder()
            .id(dieta.getId())
            .nombre(dieta.getNombre())
            .descripcion(dieta.getDescripcion())
            .observaciones(dieta.getObservaciones())
            .objetivo(dieta.getObjetivo())
            .duracionDias(dieta.getDuracionDias())
            .alimentos(dieta.getAlimentos())
            .recomendaciones(dieta.getRecomendaciones())
            .build();
    }


}
