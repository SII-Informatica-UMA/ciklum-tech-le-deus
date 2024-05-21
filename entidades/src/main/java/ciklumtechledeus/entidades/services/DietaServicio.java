package ciklumtechledeus.entidades.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ciklumtechledeus.entidades.entities.Dieta;
import ciklumtechledeus.entidades.exceptions.DietaExistenteException;
import ciklumtechledeus.entidades.exceptions.DietaNoExisteException;
import ciklumtechledeus.entidades.repositories.DietaRepository;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.List;


@Service
@Transactional
public class DietaServicio {
    private DietaRepository dietaRepo;

    public DietaServicio(DietaRepository dietaRepository){
        this.dietaRepo = dietaRepository;
    }

    public List<Dieta> dietasDeEntrenador(Long idEntrenador) {
      return this.dietaRepo.findAllByEntrenadorId(idEntrenador);
   }

   public List<Dieta> dietasDeCliente(Long idCliente) {
      return this.dietaRepo.findByClienteId(idCliente);
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
      var dieta = dietaRepo.findById(id);
      if(!dieta.isPresent()){

      }else{
            this.dietaRepo.deleteById(id);
         
      }
   }

   public void putDieta(Long idDieta, Long idCliente) {
      Optional<Dieta> d = this.getDieta(idDieta);
      d.ifPresent((dieta) -> {
         dieta.getClienteId().add(idCliente);
         this.dietaRepo.save(dieta);
      });
      d.orElseThrow(DietaNoExisteException::new);
   }
    

}