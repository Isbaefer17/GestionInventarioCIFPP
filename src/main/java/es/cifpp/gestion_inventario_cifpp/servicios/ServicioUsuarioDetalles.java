package es.cifpp.gestion_inventario_cifpp.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.cifpp.gestion_inventario_cifpp.entidades.Usuario;
import es.cifpp.gestion_inventario_cifpp.repositorios.RepositorioUsuario;

@Service
public class ServicioUsuarioDetalles implements UserDetailsService{
    @Autowired
        private RepositorioUsuario repositorioUsuario;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username = ID del usuario
        Usuario usuario = repositorioUsuario.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        return usuario; //el usuario implementa UserDetails
    }
}
