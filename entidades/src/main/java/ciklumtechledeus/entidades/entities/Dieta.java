package ciklumtechledeus.entidades.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

@Entity 
public class Dieta{

    private String nombre;
    private String descripcion;
    private String observaciones;
    private String objetivo;
    private int duracionDias;
    @ElementCollection
    private ArrayList<String> alimentos;
    private String recomendaciones;
    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection
    private Set<Long> clienteId;
    private Long entrenadorId;

    /*
    //Definimos constructor
    public Dieta(String nombre, String descripcion, String observaciones, String objetivo, int durDias, ArrayList<String> alimentos, String recomendaciones, int id, int usuarioId, int creadorId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.objetivo = objetivo;
        this.duracionDias = durDias;
        this.alimentos = alimentos;
        this.recomendaciones = recomendaciones;
        this.id = id;
        this.usuarioId = usuarioId;
        this.creadorId = creadorId;     
    }*/

    //Getters y Setters 
    
    public String getNombre(){
        return nombre;
    }
    
    public String getDescripcion(){
        return descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public int getDuracionDias() {
        return duracionDias;
    }

    public ArrayList<String> getAlimentos() {
        return alimentos;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public Long getId() {
        return id;
    }

    public Set<Long> getCliente() {
        return clienteId;
    }

    public Long getEntrenadorId() {
        return entrenadorId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public void setAlimentos(ArrayList<String> alimentos) {
        this.alimentos = alimentos;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public void setCliente(Set<Long> IdCliente) {
        this.clienteId = IdCliente;
    }

    public void setEntrenador(Long IdEntrenador) {
        this.entrenadorId = IdEntrenador;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dieta other = (Dieta) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(nombre, descripcion, observaciones, objetivo, 
                duracionDias, alimentos, recomendaciones, id, clienteId , entrenadorId);
    }

    @Override
    public String toString(){
        return "La dieta es: "+nombre+ ", descripcion: "+descripcion+", observaciones: "+observaciones
            + ", objetivo: "+objetivo+ ", duracion: "+duracionDias+ "alimentos: "+alimentos.toString()
            +", recomendaciones:"+recomendaciones+", id: "+id+", IdEntrenador: "+entrenadorId+", IdCliente: "+ clienteId;
    }


}