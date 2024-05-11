package ciklumtechledeus.entidades.exceptions;

public class DietaExistenteException extends RuntimeException {
    public DietaExistenteException(String mensaje){
        super(mensaje);
    }

    public DietaExistenteException(){

    }
}
