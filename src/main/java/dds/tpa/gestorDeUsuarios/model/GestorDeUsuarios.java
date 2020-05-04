package dds.tpa.gestorDeUsuarios.model;

import dds.tpa.gestorDeUsuarios.exceptions.ContraseniaIncorrectaException;
import dds.tpa.gestorDeUsuarios.exceptions.ContraseniaInvalidaException;
import dds.tpa.gestorDeUsuarios.exceptions.UsuarioExistenteException;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


/**
 * La clase Validador es un singleton que implementa una serie de métodos para la gestión de SignUp y SignIn de usuarios
 * del sistema.
 *
 * @author Brian Luna
 */
public class GestorDeUsuarios {
    final private static GestorDeUsuarios INSTANCE = new GestorDeUsuarios();
    File archivoCredenciales;
    File archivoCommonPasswords;
    HashMap<String, String> credenciales;
    HashSet<String>  commonPasswords;
    //TODO: ¿La idea es que las condiciones de la password se ingresen por archivo de configuración al cargar el sistema (refactor to Enum) o que se pueda modificar en runtime (Clase)?
    final private List<Recomendacion> recomendaciones = Arrays.asList(new Recomendacion("(.*\\d.*)","Debe tener al menos un dígito numérico"),
                                                                new Recomendacion("(.*[a-z].*)", "Debe tener al menos una letra minúscula"),
                                                                new Recomendacion("(.*[A-Z].*)", "Debe tener al menos una letra minúscula"),
                                                                new Recomendacion("(.*[@#$%^&+=].*)", "Debe tener al menos un caracter especial"),
                                                                new Recomendacion(".{8,}", "Debe tener longitud de al menos ocho caracteres"));

    /**
     * El constructor creará el archivo de credenciales guardadas si no existe, leerá el archivo de credenciales y guardará el HashMap en la variable {@code credenciales}.
     */
    private GestorDeUsuarios() {
        this.archivoCredenciales = new File("src/main/resources/credenciales.ser");
        this.archivoCommonPasswords = new File("src/main/resources/commonPasswords.txt");
        this.levantarCommonPasswords();
        this.levantarCredenciales();
    }

    /**
     *  El método {@code levantarCredenciales()} se encarga de leer el archivo de credenciales,
     *  generar el HashMap con las credenciales almacenadas y asignarlas a la variable {@code credenciales}.
     *  En caso que el archivo esté vacío, asignará un nuevo HashMap a la variable {@code credenciales}.
     */
    @SuppressWarnings({"unchecked"})
    private void levantarCredenciales() {
        HashMap<String, String> credenciales;

        try {
            this.archivoCredenciales.createNewFile();
            FileInputStream fisCredenciales = new FileInputStream(this.archivoCredenciales);
            ObjectInputStream oisCredenciales = new ObjectInputStream(fisCredenciales);
            credenciales = (HashMap<String, String>) oisCredenciales.readObject();
            oisCredenciales.close();
            fisCredenciales.close();
            this.credenciales = credenciales;
        } catch(EOFException exception){
            this.credenciales = new HashMap<>();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     *  El método {@code levantarCommonPassword()} se encarga de leer el archivo de contraseñas comunes,
     *  generar el array con ellas y asignarlas a la variable {@code commonPasswords}.
     */
    private void levantarCommonPasswords() {
        HashSet<String> commonPasswords = new HashSet<>();

        try{
            FileReader frCommonPasswords = new FileReader(this.archivoCommonPasswords);
            BufferedReader brCommonPasswords = new BufferedReader(frCommonPasswords);
            String lineaLeida;

            while ((lineaLeida = brCommonPasswords.readLine()) != null){
                commonPasswords.add(lineaLeida);
            }
            brCommonPasswords.close();
            frCommonPasswords.close();
            this.commonPasswords = commonPasswords;

        } catch (IOException excepcion){
            System.out.println(excepcion.getMessage());
        }
    }

    /**
     * El método {@code obtenerHash(String)} genera un hashCode para el password con el algoritmo de cifrado SHA3.
     * @param password Es el password a cifrar.
     * @return String Es el hashCode SHA3.
     */
    private String obtenerHash(String password){
        SHA3.DigestSHA3 codigoHashSHA3 = new SHA3.DigestSHA3(256);
        codigoHashSHA3.update(password.getBytes());
        return Hex.toHexString(codigoHashSHA3.digest());
    }

    /**
     * El método {@code getInstance()} retorna la referencia al singleton Validador.
     * @return Validador Es la referencia al singleton Validador.
     */
    public static GestorDeUsuarios getInstance(){
        return INSTANCE;
    }

    /**
     * El método {@code validarNuevoUsuario(String) valida que el nombre de usuario elegido no esté en uso.}
     * @param usuario Es el nombre de usuario a validar.
     */
    public void validarNuevoUsuario(String usuario){
        if(credenciales != null && credenciales.containsKey(usuario)){
            throw new UsuarioExistenteException();
        }
    }

    /**
     * El método {@code validarNuevoPassword(String)} valida que el password pasado por parámetro cumpla con las recomendaciones de seguridad.
     * @param password Es el password a validar.
     */
    public void validarNuevoPassword(String password){

        //Valida si la contraseña pasada por parámetro está entre las 10.000 más fáciles.
        if(this.commonPasswords.contains(password)){
            throw new ContraseniaInvalidaException("La contraseña es fácil.");
        }

        //Valida si la contraseña pasada por parámetro cumple las recomendaciones de seguridad.
        for (Recomendacion recomendacione : recomendaciones) {
            if (!Pattern.matches(recomendacione.getRegex(), password)) {
                throw new ContraseniaInvalidaException(recomendacione.getRequerimientoDeSeguridad());
            }
        }
    }

    /**
     * El método {@code registrarUsuario(String, String)} agrega el usuario al HashMap {@code credenciales}. Para el {@code password} se almacena su hashCode SHA3.
     * @param usuario Es el usuario a almacenar.
     * @param password Es el password a almacenar.
     */
    public void registrarUsuario(String usuario, String password){
        credenciales.put(usuario, this.obtenerHash(password));
    }

    /**
     * El método {@code iniciarSesion(String, String)} inicia la sesión del usuario si las credenciales son correctas.
     * @param usuario Es el usuario a validar.
     * @param password Es el password a almacenar.
     */
    public void iniciarSesion(String usuario, String password) {
        if(!this.obtenerHash(password).equals(credenciales.get(usuario))){
            throw new ContraseniaIncorrectaException("Contraseña incorrecta");
        } else {
            System.out.println("Sesión iniciada con éxito");
        }
    }

    /**
     * El método {@code finalizarEjecucion()} ejecuta una serie de tareas previas al cierre del sistema y luego lo cierra.
     * Escribe las credeciales del HashMap {@code credenciales} en el archivo correspondiente.
     */
    public void finalizarEjecucion(){
        //TODO: refactorizar este método. No es cohesivo (escribe un archivo y cierra el sistema (Y es el VALIDADOR!!!))
        try {
            FileOutputStream fosCredenciales = new FileOutputStream(this.archivoCredenciales);
            ObjectOutputStream oosCredenciales = new ObjectOutputStream(fosCredenciales);
            oosCredenciales.writeObject(this.credenciales);
            oosCredenciales.close();
            fosCredenciales.close();
            System.exit(0);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}