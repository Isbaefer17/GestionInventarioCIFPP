package es.cifpp.gestion_inventario_cifpp.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document("Usuario")
public class Usuario implements UserDetails /*Necesario para Security boot*/{
    @Id
    private String codigo;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasenya;
    private ArrayList<Rol> roles;
    
    //Spring boot rellena los datos automáticamente mediante un formulario
    public Usuario() {

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String id) {
        this.codigo = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correoElectronico) {
        this.correo = correoElectronico;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public ArrayList<Rol> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Rol> roles) {
        this.roles = roles;
    }

    public void anayadirRol(Rol rol){
        if (!getRoles().contains(rol)){
            getRoles().add(rol);
        }
    }
    //estos son lo métodos de la interfaz
    @Override
    public String getPassword(){
        return this.contrasenya;
    }

    @Override
    public String getUsername() {
        return this.codigo;
    }

    @Override //obtenemos los roles para Spring Security ya que no le sirven los roles como los tenemos
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toList());
    }   


    //Estos métodos son para la expiración de cuentas y sus datos. como nunca expiran siempre son true
    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }    
    
    @Override
    public boolean isAccountNonLocked() {
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return true; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(codigo, usuario.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }


    @Override
    public String toString() {
        String listaRoles = "";
        for (int i = 0; i<getRoles().size(); i++){
            listaRoles += getRoles().get(i);
        }
        return "Usuario [Codigo:" + getCodigo() + "Nombre: " + getNombre() + ", Apellido: "
                + getApellido() + ", Correo Electronico: " + getCorreo() + ", Contraseña: "
                + getContrasenya() + " Lista de roles: " + listaRoles +"]";
    }
}
