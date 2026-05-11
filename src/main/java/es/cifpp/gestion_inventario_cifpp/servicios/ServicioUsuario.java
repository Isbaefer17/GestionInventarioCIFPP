package es.cifpp.gestion_inventario_cifpp.servicios;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import es.cifpp.gestion_inventario_cifpp.entidades.Rol;
import es.cifpp.gestion_inventario_cifpp.entidades.Usuario;
import es.cifpp.gestion_inventario_cifpp.repositorios.RepositorioUsuario;

@Service
public class ServicioUsuario {
    //Acceso al repositorio
    @Autowired
        private RepositorioUsuario repositorio;

    //Acceso al encriptador de contraseñas para el modificar
    @Autowired
        private BCryptPasswordEncoder codificador;

    @Autowired
        private ServicioRol servicioRol; 

    public Usuario AnyadirUsuario(Usuario usuario){
        return repositorio.save(usuario);  
    }

    public ArrayList<Usuario> ListarUsuario(){
        ArrayList<Usuario> listaUsuario = (ArrayList<Usuario>) repositorio.findAll(); 
        return listaUsuario;
    }

    public Usuario BuscarUsuarioPorCodigo(String codigo){
        Usuario UsuarioABuscar = repositorio.findById(codigo).orElse(null);
        return UsuarioABuscar;
    }

    public Usuario ModificarUsuario(String idActual, String nuevoCodigo, String nuevoNombre, String nuevoApellido, String nuevoCorreo, 
                                    String nuevaContrasenya, String confirmarContrasenya, ArrayList<String> rolesIds){

        Usuario usuario = BuscarUsuarioPorCodigo(idActual); 
        if (usuario != null){
            if (nuevoCodigo !=null && !nuevoCodigo.isBlank()){
                EliminarUsuario(idActual); //borramos el viejo. el update se hace guardando el usuario con un identificador ya establecido
                usuario.setCodigo(nuevoCodigo);
            }
            if (nuevoNombre !=null && !nuevoNombre.isBlank()){
                usuario.setNombre(nuevoNombre);
            }

            if (nuevoApellido !=null && !nuevoApellido.isBlank()){
                usuario.setApellido(nuevoApellido);
            }

            if (nuevoCorreo !=null && !nuevoCorreo.isBlank()){
                usuario.setCorreo(nuevoCorreo);
            }

            if (nuevaContrasenya !=null && !nuevaContrasenya.isBlank() && confirmarContrasenya !=null && !confirmarContrasenya.isBlank()){
                if (nuevaContrasenya.equals(confirmarContrasenya)) {
                    String contrasenyaEncriptada = codificador.encode(nuevaContrasenya);
                    usuario.setContrasenya(contrasenyaEncriptada);
                }
                else{
                    return null;
                }
            }

            if (rolesIds != null){
                for (int i = 0; i < rolesIds.size(); i++){
                    Rol rol = servicioRol.BuscarRolPorId(rolesIds.get(i));
                    if (!usuario.getRoles().contains(rol)){
                        usuario.anayadirRol(rol);
                    }
                    else{
                        System.out.println("rol duplicado" + rol);
                    }
                }
            }

            //Una vez cambiado los atributos del objeto lo añadimos
            AnyadirUsuario(usuario);

            return usuario;
        }

        else return null;
    }

    public boolean EliminarUsuario(String codigo){
        Usuario UsuarioAEliminar = BuscarUsuarioPorCodigo(codigo);

        if (UsuarioAEliminar == null){
            return false;
        }

        else{
            repositorio.deleteById(UsuarioAEliminar.getCodigo());
            return true;
        } 
    }
}
