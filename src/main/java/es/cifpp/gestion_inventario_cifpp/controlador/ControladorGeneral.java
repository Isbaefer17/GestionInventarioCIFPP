package es.cifpp.gestion_inventario_cifpp.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.cifpp.gestion_inventario_cifpp.entidades.Usuario;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioUsuario;



@Controller
public class ControladorGeneral {
    @Autowired
        private ServicioUsuario servicioUsuario;

    @GetMapping("/")
    public String raiz() {
        return "redirect:/IniciarSesion";
    }   

    @GetMapping("/volverAInicio")
    public String volverAlInicio(Model modelo , Authentication autentificacion) {
        String idUsuario = autentificacion.getName();
        modelo.addAttribute("miId", idUsuario);
        Usuario usuario = servicioUsuario.BuscarUsuarioPorCodigo(idUsuario);
        modelo.addAttribute("nombreUsuario", "Bienvenido " + usuario.getNombre() + " " + usuario.getApellido());
        return "index";
    }

    @GetMapping("/IniciarSesion")
    public String mostrarLogin() {
        return "IniciarSesion"; // nombre del HTML sin extensión
    }
}
