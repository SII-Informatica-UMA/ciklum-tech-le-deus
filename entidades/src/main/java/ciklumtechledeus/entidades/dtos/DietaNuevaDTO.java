package ciklumtechledeus.entidades.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class DietaNuevaDTO {
    private String nombre;
    private String descripcion;
    private String observaciones;
    private String objetivo;
    private Integer duracionDias;
    private List<String> alimentos;
    private String recomendaciones;
}
