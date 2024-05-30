package ciklumtechledeus.entidades.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
      return this.dietaRepo.findAllByEntrenadorId(idEntrenador);
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

   public void putDieta(Long idDieta, Long idCliente) {
      dietaRepo.findById(idDieta).ifPresentOrElse(dieta -> {
         dieta.getClienteId().add(idCliente);
         dietaRepo.save(dieta);
     }, () -> {
         throw new DietaNoExisteException("Dieta no encontrada");
     });
   }
    

}