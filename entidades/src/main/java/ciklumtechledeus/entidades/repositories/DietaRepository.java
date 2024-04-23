package ciklumtechledeus.entidades.repositories;

import ciklumtechledeus.entidades.entities.*;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DietaRepository extends JpaRepository<Dieta, Integer> {
    
    @Query( "Select d from Dieta d where d.usuarioId = :nombre" )
    List<Dieta> findByIdUsuaraio(@Param("nombre") String nombre);

}