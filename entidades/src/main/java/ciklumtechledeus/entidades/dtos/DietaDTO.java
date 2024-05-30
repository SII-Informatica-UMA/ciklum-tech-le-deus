package ciklumtechledeus.entidades.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class DietaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String observaciones;
    private String objetivo;
    private int duracionDias;
    private List<String> alimentos;
    private String recomendaciones;

    // Getters y Setters
    /*
    @JsonProperty("alimentos")
    public List<String> getAlimentos(){
        return alimentos;
    }

    
    @JsonProperty("alimentos")
    public void setAlimentos(List<String> alimentos){
        this.alimentos =  alimentos;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("nombre")
    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @JsonProperty("descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    @JsonProperty("descripcion")
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonProperty("observaciones")
    public String getObservaciones() {
        return observaciones;
    }

    @JsonProperty("observaciones")
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @JsonProperty("objetivo")
    public String getObjetivo() {
        return objetivo;
    }

    @JsonProperty("objetivo")
    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    @JsonProperty("duracionDias")
    public int getDuracionDias() {
        return duracionDias;
    }

    @JsonProperty("duracionDias")
    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    @JsonProperty("recomendaciones")
    public String getRecomendaciones() {
        return recomendaciones;
    }

    @JsonProperty("recomendaciones")
    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    } */
}
