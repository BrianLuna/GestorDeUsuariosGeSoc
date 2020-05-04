package dds.tpa.gestorDeUsuarios.exceptions;

public class ContraseniaIncorrectaException extends RuntimeException{

    public ContraseniaIncorrectaException(String mensaje){
        super(mensaje);
    }
}
