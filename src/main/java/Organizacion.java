import java.util.Scanner;

public class Organizacion {
    public static void main(String[] args) {
        int seleccion;

        do {
            System.out.println("Por favor, selecciones una opción");
            System.out.println("1) Iniciar Sesión\n2) Registrarse\n3) Salir");
            Scanner teclado = new Scanner(System.in);
            Scanner select = new Scanner(System.in);
            seleccion = select.nextInt();

            switch (seleccion) {
                case 1:
                    String usuarioExistente;
                    String passwordExistente;
                    System.out.println("Introduzca un usuario: ");
                    usuarioExistente = teclado.nextLine();
                    System.out.println("Introduzca una contraseña: ");
                    passwordExistente = teclado.nextLine();
                    GestorDeUsuarios.getInstance().iniciarSesion(usuarioExistente, passwordExistente);

                    break;
                case 2:
                    String usuarioNuevo;
                    String passwordNuevo;
                    System.out.println("Introduzca un usuario: ");
                    usuarioNuevo = teclado.nextLine();
                    GestorDeUsuarios.getInstance().validarNuevoUsuario(usuarioNuevo);
                    System.out.println("Introduzca una contraseña: ");
                    passwordNuevo = teclado.nextLine();
                    GestorDeUsuarios.getInstance().validarNuevoPassword(passwordNuevo);
                    GestorDeUsuarios.getInstance().registrarUsuario(usuarioNuevo, passwordNuevo);
                    System.out.println("Usuario registrado con éxito");
                    break;
                case 3:
                    GestorDeUsuarios.getInstance().finalizarEjecucion();
            }

        } while (seleccion != 4);
    }
}
