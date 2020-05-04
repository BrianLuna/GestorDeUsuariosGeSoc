package dds.tpa.gestorDeUsuarios.exceptions;

public class UsuarioExistenteException extends RuntimeException{

    public UsuarioExistenteException(){
        super("El usuario ya existe");
    }
}
