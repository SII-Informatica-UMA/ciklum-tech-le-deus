package ciklumtechledeus.entidades.exceptions;

    public class AccesoProhibido extends RuntimeException{
        public AccesoProhibido(String message) {super(message);}
        public AccesoProhibido() {super();}
    }

