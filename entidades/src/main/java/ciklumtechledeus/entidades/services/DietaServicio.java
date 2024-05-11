package ciklumtechledeus.entidades.services;

import java.util.List;
import java.util.Optional;

import ciklumtechledeus.entidades.entities.Dieta;
import ciklumtechledeus.entidades.exceptions.DietaNoExisteException;
import ciklumtechledeus.entidades.repositories.DietaRepository;

public class DietaServicio {
    private DietaRepository repo;

    public DietaServicio(DietaRepository dietaRepository){
        this.repo = dietaRepository;
    }

    public List<Dieta> dietasDeEntrenador(Long idEntrenador) {
      return this.repo.findAllByIdEntrenador(idEntrenador);
   }

   public List<Dieta> dietasDeCliente(Long idCliente) {
      return this.repo.findByIdClientesContaining(idCliente);
   }

   public Dieta actualizarDieta(Dieta g) {
      return (Dieta)this.repo.save(g);
   }

   public Optional<Dieta> getDieta(Long id) {
      return this.repo.findById(id);
   }

   public void deleteDieta(Long id) {
    this.repo.deleteById(id);
   }

   public void putDieta(Long idDieta, Long idCliente) {
      Optional<Dieta> d = this.getDieta(idDieta);
      d.ifPresent((dieta) -> {
         dieta.getIdClientes().add(idCliente);
         this.repo.save(dieta);
      });
      d.orElseThrow(DietaNoExisteException::new);
   }
    
}
