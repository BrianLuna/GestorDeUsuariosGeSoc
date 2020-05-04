package dds.tpa.gestorDeUsuarios.model;

public class Recomendacion {
    private String regex;
    private String requerimientoDeSeguridad;

    public Recomendacion(String regex, String requerimientoDeSeguridad){
        this.regex = regex;
        this.requerimientoDeSeguridad = requerimientoDeSeguridad;
    }

    public String getRegex() {
        return regex;
    }

    public String getRequerimientoDeSeguridad() {
        return requerimientoDeSeguridad;
    }
}
