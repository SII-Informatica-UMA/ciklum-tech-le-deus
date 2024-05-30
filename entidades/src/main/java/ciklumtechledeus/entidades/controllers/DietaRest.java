package ciklumtechledeus.entidades.controllers;



import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import ciklumtechledeus.entidades.exceptions.AccesoProhibido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import ciklumtechledeus.entidades.services.DietaServicio;
import ciklumtechledeus.entidades.dtos.DietaDTO;
import ciklumtechledeus.entidades.dtos.DietaNuevaDTO;
import ciklumtechledeus.entidades.entities.Dieta;
import ciklumtechledeus.entidades.exceptions.DietaExistenteException;
import ciklumtechledeus.entidades.exceptions.DietaNoExisteException;

@RestController
@RequestMapping(path = "/dieta")
public class DietaRest {

    public static final String dieta_path = "/dieta";
    private final DietaServicio servicio;

    @Autowired
    public DietaRest(DietaServicio servicio){
        this.servicio = servicio;
    }

    /*
    @GetMapping
    public List<DietaDTO> getDietas(@RequestParam(value = "entrenador",required = false) Long idEntrenador, @RequestParam(value = "cliente",required = false) Long idCliente) {
      if (idEntrenador != null && idCliente != null) {
         throw new IllegalArgumentException("Debes especificar exactamente un parametro de consulta: cliente o entrenador.");
      } else if (idEntrenador == null && idCliente == null) {
         throw new IllegalArgumentException("Debes especificar exactamente un parametro de consulta: cliente o entrenador.");
      } else {
         List<Dieta> dietas = null;
         if (idEntrenador != null) {
            dietas = this.servicio.dietasDeEntrenador(idEntrenador);
         } else {
            dietas = this.servicio.dietasDeCliente(idCliente);
         }
         return dietas.stream().map(Mapper::toDietaDTO).toList();
      }
    }*/
    @GetMapping
    @PreAuthorize("hasRole('ENTRENADOR') or hasRole('CLIENTE')")
    public ResponseEntity<List<DietaDTO>> getDietas(
            @RequestParam(value = "entrenador", required = false) Long idEntrenador,
            @RequestParam(value = "cliente", required = false) Long idCliente) {

        List<Dieta> dietas;

        if (idEntrenador != null && idCliente == null) {
            dietas = servicio.dietasDeEntrenador(idEntrenador);
            
            if(dietas.isEmpty()){
                return ResponseEntity.notFound().build();
            }
        } else if (idCliente != null && idEntrenador == null) {
            dietas = servicio.dietasDeCliente(idCliente);
            
            if(dietas.isEmpty()){
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }

        List<DietaDTO> dietaDTOs = dietas.stream()
                                         .map(Mapper::toDietaDTO)
                                         .collect(Collectors.toList());

        return ResponseEntity.ok(dietaDTOs);
    }

    @PostMapping
    @PreAuthorize("hasRole('ENTRENADOR')")
    public ResponseEntity<DietaDTO> createDieta(@RequestParam(value = "entrenador", required = true) Long idEntrenador,
                                                @RequestBody DietaNuevaDTO dietaNuevoDTO,
                                                UriComponentsBuilder uriBuilder) {
        Dieta g = Mapper.toDietaNueva(dietaNuevoDTO);
        g.setId(g.getId());
        g.setEntrenadorId(idEntrenador);
        g = this.servicio.postDieta(g);
        URI location = this.generadorUri(uriBuilder, g.getId());
        return ResponseEntity.created(location).body(Mapper.toDietaDTO(g));
    }

    private URI generadorUri(UriComponentsBuilder uriBuilder, Long idDieta) {
        return uriBuilder.path("/dieta/{id}")
                         .buildAndExpand(idDieta)
                         .toUri();
    }

    /*Revisar */
    @PutMapping
    public ResponseEntity<DietaDTO> asociarDieta(@RequestParam(value = "cliente", required = true) Long idCliente,
                                                  @RequestBody DietaDTO dietaDTO) {
        try {
            this.servicio.putDieta(dietaDTO.getId(), idCliente);
            return this.servicio.getDieta(dietaDTO.getId())
                                .map(dieta -> ResponseEntity.ok(Mapper.toDietaDTO(dieta)))
                                .orElseThrow(() -> new DietaNoExisteException("Dieta no encontrada"));
        } catch (DietaNoExisteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{idDieta}")
    public ResponseEntity<DietaDTO> getDieta(@PathVariable Long idDieta) {
        Optional<Dieta> dieta = servicio.getDieta(idDieta);

        if (dieta.isPresent()) {
            DietaDTO dietaDTO = Mapper.toDietaDTO(dieta.get());
            return ResponseEntity.ok(dietaDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idDieta}")
    @PreAuthorize("hasRole('ENTRENADOR')")
    public ResponseEntity<DietaDTO> updateDieta(@PathVariable Long idDieta,
                                                @RequestBody DietaDTO dietaDTO) {
        
        //Optional<Dieta> g = servicio.getDieta(idDieta);
        //Dieta g1 = g.get();

        Dieta dieta = Mapper.toDieta(dietaDTO);
        //dieta.setClienteId(g1.getClienteId());
        dieta.setId(idDieta);
        //dieta.setEntrenadorId(idDieta);*/
        Dieta actualizada = this.servicio.actualizarDieta(dieta);
        return ResponseEntity.ok(Mapper.toDietaDTO(actualizada));
    }

    @DeleteMapping("/{idDieta}")
    public ResponseEntity<Void> deleteDieta(@PathVariable Long idDieta) {
        this.servicio.deleteDietaById(idDieta);
        return ResponseEntity.ok().build();
    }   
    
    @ExceptionHandler(DietaNoExisteException.class)
    public ResponseEntity<String> handleDietaNoExisteException(DietaNoExisteException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AccesoProhibido.class)
    public ResponseEntity<String> handleAccesoProhibido(AccesoProhibido ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
     }

    @ExceptionHandler(DietaExistenteException.class)
    public ResponseEntity<String> handleDietaExistenteException(DietaExistenteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
