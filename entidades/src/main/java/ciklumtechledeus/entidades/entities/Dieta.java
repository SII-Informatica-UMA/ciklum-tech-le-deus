package ciklumtechledeus.entidades.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//import java.util.ArrayList;
import java.util.List;
//import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity 
public class Dieta{

    private String nombre;
    private String descripcion;
    private String observaciones;
    private String objetivo;
    @Column(name="DURACION_DIAS")
    private Integer duracionDias;
    @ElementCollection
    private List<String> alimentos;
    private String recomendaciones;
    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection
    private Set<Long> clienteId;
    private Long entrenadorId;


}