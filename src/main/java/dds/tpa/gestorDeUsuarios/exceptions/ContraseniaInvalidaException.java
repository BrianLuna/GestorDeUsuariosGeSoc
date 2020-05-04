package dds.tpa.gestorDeUsuarios.exceptions;

public class ContraseniaInvalidaException extends RuntimeException{

    public ContraseniaInvalidaException(String mensaje){
        super(mensaje);
    }
}