package ciklumtechledeus.entidades.exceptions;

public class DietaEmptyException extends RuntimeException {
    public DietaEmptyException(String mensaje){
        super(mensaje);
    }

    public DietaEmptyException(){

    }
}
