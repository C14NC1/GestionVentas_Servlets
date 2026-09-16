package modelo;

// Representa un usuario del sistema
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String correo;
    private String contraseña;
    private String rol;

    public Usuario() {}

    // Crear un usuario con sus credenciales y rol
    public Usuario(int idUsuario, String nombre, String correo, String contraseña, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
