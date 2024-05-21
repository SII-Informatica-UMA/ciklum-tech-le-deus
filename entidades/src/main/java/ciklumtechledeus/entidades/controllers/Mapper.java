package ciklumtechledeus.entidades.controllers;

import ciklumtechledeus.entidades.dtos.DietaDTO;
import ciklumtechledeus.entidades.dtos.DietaNuevaDTO;
import ciklumtechledeus.entidades.entities.Dieta;

public class Mapper {

    public static DietaDTO toDietaDTO(Dieta dieta) {
        //Convierte una entidad dieta en un objeto DTO
        //Util para devolver informacion sobre una dieta 
        //desde la capa de servicio para la de presentacion
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

    public static Dieta toDietaNueva(DietaNuevaDTO dietaNuevaDTO) {
        // Convierte un objeto DietaNuevaDTO en una entidad Dieta
        // Útil para recibir datos desde la capa de presentación y convertirlos en una entidad manejable por la capa de servicio
        return Dieta.builder()
            .nombre(dietaNuevaDTO.getNombre())
            .descripcion(dietaNuevaDTO.getDescripcion())
            .observaciones(dietaNuevaDTO.getObservaciones())
            .objetivo(dietaNuevaDTO.getObjetivo())
            .duracionDias(dietaNuevaDTO.getDuracionDias())
            .alimentos(dietaNuevaDTO.getAlimentos())
            .recomendaciones(dietaNuevaDTO.getRecomendaciones())
            .build();
    }

    public static Dieta toDieta(DietaDTO dietaDTO) {
        // Convierte un objeto DietaDTO en una entidad Dieta
        // Útil para recibir datos desde la capa de presentación y convertirlos en una entidad manejable por la capa de servicio
        return Dieta.builder()
            .id(dietaDTO.getId())
            .build();
    }
    


}
