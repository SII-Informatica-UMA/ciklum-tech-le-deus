package ciklumtechledeus.entidades.repositories;

import ciklumtechledeus.entidades.entities.*;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DietaRepository extends JpaRepository<Dieta, Long> {
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Dieta d WHERE d.id = :id AND d.entrenadorId = :entrenadorId")
    void deleteByIdAndEntrenadorId(@Param("id") Long id, @Param("entrenadorId") Long entrenadorId);
    
    List<Dieta> findAllByEntrenadorId(Long idEntrenador);
   
    // Define una consulta JPQL que selecciona todas las entidades Dieta (d) 
    // donde el valor idCliente es un miembro del conjunto d.clienteId.
   
    List<Dieta> findAllByClienteId( Long idCliente);

    List<Dieta> findAllByNombre(String nombre);

    List<Dieta> findAll();
   
    List<Dieta> findByClienteId(Long clienteId);
    
    List<Dieta> findByEntrenadorId(Long entrenadorid);
    Optional<Dieta> findByNombre(String nombre);
}