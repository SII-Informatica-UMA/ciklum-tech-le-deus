package ciklumtechledeus.entidades.controllers;



import java.net.URI;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ciklumtechledeus.entidades.exceptions.DietaNoExisteException;

@RestController
@RequestMapping({"/dieta"})
public class DietaRest {
    private final DietaServicio servicio;

    @Autowired
    public DietaRest(DietaServicio servicio){
        this.servicio = servicio;
    }

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
    }

    //Por aqui
    @PostMapping
    public ResponseEntity<DietaDTO> createDieta(@RequestParam(value = "entrenador",required = true) Long idEntrenador, @RequestBody DietaNuevaDTO dietaNuevoDTO, UriComponentsBuilder uriBuilder) {
      Dieta g = Mapper.toDieta(dietaNuevoDTO);
      g.setId((Long)null);
      g.setIdEntrenador(idEntrenador);
      g = this.servicio.actualizarDieta(g);
      return ResponseEntity.created((URI)this.generadorUri(uriBuilder.build()).apply(g)).body(Mapper.toDietaDTO(g));
   }

   public Function<Dieta , URI> generadorUri(UriComponents uriBuilder) {
        return (g) -> {
            return UriComponentsBuilder.newInstance().uriComponents(uriBuilder).path("/dieta").path(String.format("/%d", g.getId())).build().toUri();
        };
    }

    @PutMapping
   public ResponseEntity<DietaDTO> asociarDieta(@RequestParam(value = "cliente",required = true) Long idCliente, @RequestBody DietaDTO dietaDTO) {
      this.servicio.putDieta(dietaDTO.getId(), idCliente);
      return ResponseEntity.of(this.servicio.getDieta(dietaDTO.getId()).map(Mapper::toDietaDTO));
   }

   @GetMapping({"/{idDieta}"})
   public ResponseEntity<DietaDTO> getDieta(@PathVariable Long idDieta) {
      return ResponseEntity.of(this.servicio.getDieta(idDieta).map(Mapper::toDietaDTO));
   }

   @PutMapping({"/{idDieta}"})
   public DietaDTO actualizarDieta(@PathVariable Long idDieta, @RequestBody DietaDTO dieta) {
      this.servicio.getDieta(idDieta).orElseThrow(() -> {
         return new DietaNoExisteException();
      });
      dieta.setId(idDieta);
      Dieta g = this.servicio.actualizarDieta(Mapper.toDieta2(dieta));
      return Mapper.toDietaDTO(g);
   }

   @DeleteMapping({"/{idDieta}"})
   public void deleteDieta(@PathVariable Long idDieta) {
      this.servicio.getDieta(idDieta).orElseThrow(() -> {
         return new DietaNoExisteException();
      });
      this.servicio.deleteDieta(idDieta);
   }   
    
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(
      code = HttpStatus.BAD_REQUEST
    )
    public void handleIllegalArgumentException() {
    }

    @ExceptionHandler({DietaNoExisteException.class})
    @ResponseStatus(
      code = HttpStatus.NOT_FOUND
    )
    public void handleDietaNoExisteException() {
    }
}
