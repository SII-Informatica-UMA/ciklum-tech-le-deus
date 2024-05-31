package ciklumtechledeus.entidades.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ciklumtechledeus.entidades.dtos.DietaDTO;
import ciklumtechledeus.entidades.entities.Dieta;
import ciklumtechledeus.entidades.exceptions.DietaExistenteException;
import ciklumtechledeus.entidades.exceptions.DietaNoExisteException;
import ciklumtechledeus.entidades.repositories.DietaRepository;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class DietaServicio {
   // Ponemos final para prevenir la inmutabilidad y reasignacion despues de la inicializacion 
   private final DietaRepository dietaRepo;

    @Autowired
    public DietaServicio(DietaRepository dietaRepository){
        this.dietaRepo = dietaRepository;
    }

    public List<Dieta> dietasDeEntrenador(Long idEntrenador) {
      return this.dietaRepo.findByEntrenadorId(idEntrenador);
   }

   public List<Dieta> dietasDeCliente(Long idCliente) {
      return this.dietaRepo.findByClienteId(idCliente);
   }

   public Dieta postDieta(Dieta dieta){
    return dietaRepo.save(dieta);
   }

   public Dieta actualizarDieta(Dieta dieta) {
      if (dietaRepo.existsById(dieta.getId())) {
          return dietaRepo.save(dieta);
      } else {
          throw new DietaNoExisteException("Dieta no encontrada");
      }
  }
  
   public Optional<Dieta> getDieta(Long id) {
      return this.dietaRepo.findById(id);
   }

   public void deleteDietaById(Long id) {
      if (dietaRepo.existsById(id)) {
         dietaRepo.deleteById(id);
     } else {
         throw new DietaNoExisteException("Dieta no encontrada");
     }
   }

   public Dieta putDieta(Long idDieta, DietaDTO nuevaDietaDTO, Long idCliente) {
        return dietaRepo.findById(idDieta)
            .map(dieta -> {
                // Actualiza todos los campos de la dieta con los valores de nuevaDietaDTO
                dieta.setNombre(nuevaDietaDTO.getNombre());
                dieta.setDescripcion(nuevaDietaDTO.getDescripcion());
                dieta.setObservaciones(nuevaDietaDTO.getObservaciones());
                dieta.setObjetivo(nuevaDietaDTO.getObjetivo());
                dieta.setDuracionDias(nuevaDietaDTO.getDuracionDias());
                dieta.setRecomendaciones(nuevaDietaDTO.getRecomendaciones());
                // Actualiza cualquier otro campo que necesites

                // Asocia el cliente a la dieta
                if (dieta.getClienteId() == null) {
                    dieta.setClienteId(new HashSet<>());
                }
                dieta.getClienteId().add(idCliente);

                // Guarda la dieta actualizada
                return dietaRepo.save(dieta);
            })
            .orElseThrow(() -> new DietaNoExisteException("Dieta no encontrada"));
    }
    

}
