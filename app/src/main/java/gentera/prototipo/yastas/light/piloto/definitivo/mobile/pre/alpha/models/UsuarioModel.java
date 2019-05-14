package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models;

public class UsuarioModel {
    private String nombreUsuario, numeroDeUsuario, roll, token;

    public UsuarioModel() {

    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNumeroDeUsuario() {
        return numeroDeUsuario;
    }

    public void setNumeroDeUsuario(String numeroDeUsuario) {
        this.numeroDeUsuario = numeroDeUsuario;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
