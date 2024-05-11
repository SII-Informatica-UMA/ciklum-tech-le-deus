package ciklumtechledeus.entidades.exceptions;

public class DietaNoExisteException extends RuntimeException {
    public DietaNoExisteException(String mensaje){
        super(mensaje);
    }

    public DietaNoExisteException(){
        super();
    }


}
