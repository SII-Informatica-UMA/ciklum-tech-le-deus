package ciklumtechledeus.entidades.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToMany;
import java.util.List;
import java.util.Objects;

@Entity
public class Cliente {
    
    @Id
    @GeneratedValue
    private int id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String password;
    private String email;

    @ManyToMany
    private List<Dieta> dietas;

    // Constructor vacío necesario para JPA
    public Cliente() {
    }

    // Constructor con todos los campos
    public Cliente(int id, String nombre, String apellido1, String apellido2, String email,String password,List<Dieta> dietas){
        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.email = email;
        this.password = password;
        this.dietas = dietas;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Dieta> getDietas() {
        return dietas;
    }

    public void setDietas(List<Dieta> dietas) {
        this.dietas = dietas;
    }

    // hashCode , equals y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido1, apellido2, password, email, dietas);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", dietas=" + dietas +
                '}';
    }
}
