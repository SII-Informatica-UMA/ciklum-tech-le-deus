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
}
