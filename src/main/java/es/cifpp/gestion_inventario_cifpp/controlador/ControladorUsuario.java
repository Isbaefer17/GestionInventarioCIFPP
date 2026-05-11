package es.cifpp.gestion_inventario_cifpp.controlador;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.cifpp.gestion_inventario_cifpp.entidades.Rol;
import es.cifpp.gestion_inventario_cifpp.entidades.Usuario;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioRol;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioUsuario;
import es.cifpp.gestion_inventario_cifpp.utilidades.UtilidadesFormarHTML;

@Controller
public class ControladorUsuario {
    @Autowired
        private ServicioUsuario servicio;

    @Autowired
        private ServicioRol servicioRol; 

    @Autowired
        private BCryptPasswordEncoder codificador;

    @Autowired
        private UtilidadesFormarHTML generarHTML;
    
    @PostMapping("/anyadirUsuario")
    public String anyadirUsuario(@ModelAttribute Usuario usuario, /* Crea el objeto Usuario con los datos del formulario*/
        @RequestParam("confirmarContrasenya") String confirmarContrasenya,
        @RequestParam(value = "roles") ArrayList<String> rolesIds ,Model modelo) {

        //Antes de empezar miramos si el usuario confirmo su contraseña, si no es así no añadimos el usuario
        if(!usuario.getContrasenya().equals(confirmarContrasenya)) {
            modelo.addAttribute("anyadir", "No se ha podido añadir el usuario, Las contraseñas no coinciden. Vuelve a intentarlo de nuevo");
            return "index"; // vuelve a index.html
        }

        //encriptamos la contraseña
        String encriptada = codificador.encode(confirmarContrasenya);
        // guardamos la contraseña encryptada
        usuario.setContrasenya(encriptada);

        //Como crea la lista y la pasa sin antes verificar nos cargamos la lista con uno nuevo
        usuario.setRoles(new ArrayList<>());
        
        //buscamos los roles del usuario
        if (rolesIds != null || !rolesIds.isEmpty()){
            for(int i = 0; i < rolesIds.size(); i++){
                //Buscamos el rol correspondiente
                Rol rol = servicioRol.BuscarRolPorId(rolesIds.get(i));
                //Lo añadimos si no es null
                if (rol != null){
                    usuario.anayadirRol(rol);
                }
                else{
                    modelo.addAttribute("anyadir", "no se ha podido añadir el usuario. Vuelve a intentarlo de nuevo");
                    return "index"; // vuelve a index.html
                }    
            }
        }

        System.out.println("USUARIO RECIBIDO: " + usuario);
        
        //enviamos el objeto al servicio
        Usuario UsuarioGuardado = servicio.AnyadirUsuario(usuario);
        
        String html = CrearTabla1SoloElemento(UsuarioGuardado);
        
        modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el objeto se ha añadido corectamente: </p>" + html + "</div>");
        
        return "index"; // vuelve a index.html    
    }

    @GetMapping("/mostrarUsuarios")
    public String MostarUsuarios(Model modelo) {
        ArrayList<Usuario> usuarios = servicio.ListarUsuario();
        
        String html = generarHTML.PrepararTablaHTML("Usuarios", usuarios);
        
        modelo.addAttribute("tabla",html);

        return "Usuarios";
    }

    @PostMapping("/buscarUsuarioPorID")
    public String buscarPorID(@RequestParam String codigoUsuario /* guarda el id en un string introducido por formulario*/, Model modelo) {
        System.out.println("ID RECIBIDO: " + codigoUsuario);
        
        //Buscamos el el usuario por su id y lo guardamos, si no existe devuelve null
        Usuario usuario = servicio.BuscarUsuarioPorCodigo(codigoUsuario);
        
        if (usuario != null){    
            String html = CrearTabla1SoloElemento(usuario);
            modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el usuario ha sido encontrado: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("anyadir", "<p>el ususario no se ha encontrado: </p>"); 
        }
        
        return "index";    
    }

    @PostMapping("/modificarUsuario")
    public String modificarUsuario(@RequestParam String id, @RequestParam (required = false) String nuevoCodigo,
        @RequestParam (required = false) String nuevoNombre, @RequestParam (required = false) String nuevoApellido, 
        @RequestParam (required = false) String nuevoCorreo, @RequestParam (required = false) String nuevaContrasenya, 
        @RequestParam (required = false) String confirmarContrasenya, 
        @RequestParam(value = "roles", required = false) ArrayList<String> rolesIds, Model modelo) {

        System.out.println(id + ";" + nuevoCodigo + ";" +nuevoNombre  + ";" +nuevoApellido + ";" +nuevoCorreo + ";" +nuevaContrasenya + ";" +confirmarContrasenya + ";" +rolesIds);
        
        //Guardamos y llamamos al servicio para modificar
        Usuario usuarioModificado = servicio.ModificarUsuario(id, nuevoCodigo ,nuevoNombre, nuevoApellido, nuevoCorreo, nuevaContrasenya, 
                                                              confirmarContrasenya, rolesIds);
        System.out.println(usuarioModificado);
        
        String html = CrearTabla1SoloElemento(usuarioModificado);
        
        if (usuarioModificado != null){
            modelo.addAttribute("modificar", "<div class=\"ocultarBotones\"><p>el usuario ha sido modificado: </p>" + html + "</div>");
        }

        else {
            modelo.addAttribute("modificar", "<p>el usuario no existe o no se ha cambiado la contraseña porque no coincidian</p>");
        }
        
        return "index";
    }

    @PostMapping("/eliminarUsuario")
    public String eliminarObjeto(@RequestParam String id, Model modelo) {
        //antes de eliminar guardo el objeto para mostarlo después
        Usuario objeto = servicio.BuscarUsuarioPorCodigo(id);
        
        //llamamos a elminar
        boolean seHaPodidoEliminar = servicio.EliminarUsuario(id);
        
        //lo mostramos
        String html = CrearTabla1SoloElemento(objeto);
        
        if (seHaPodidoEliminar){
            modelo.addAttribute("eliminar", "<div class=\"ocultarBotones\"><p>el usuario ha sido eliminado: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("eliminar", "<p>el usuario no existe</p>");
        }
        
        return "index";
    }

    @GetMapping("/confirmarModificarUsuario")
    public String confirmarModificar(@RequestParam String id, Model modelo) {
        Usuario usuario = servicio.BuscarUsuarioPorCodigo(id);
        
        String html = CrearTabla1SoloElemento(usuario);
        modelo.addAttribute("tabla",html);
        
        // Aquí defines el action dinámico
        modelo.addAttribute("action", "/modificarUsuario");
        
        //añadimos el id ya que no lo métera por el post del formulario
        modelo.addAttribute("id", id);
        
        String cuerpoFormulario = generarHTML.CrearFormularioUsuario();
     
        modelo.addAttribute("cuerpoFormulario", cuerpoFormulario);

        modelo.addAttribute("formulario", "usuario");
        
        return "ConfirmarModificar";
    }

    @GetMapping("/confirmarEliminarUsuario")
    public String confirmarEliminar(@RequestParam String id, Model modelo) {
        Usuario usuario = servicio.BuscarUsuarioPorCodigo(id);
        
        String html = CrearTabla1SoloElemento(usuario);
        
        modelo.addAttribute("tabla",html);
        
        // Aquí defines el action dinámico
        modelo.addAttribute("action", "/eliminarUsuario");
        
        //añadimos el id ya que no lo métera por el post del formulario
        modelo.addAttribute("id", id);   

        return "ConfirmarEliminar";
    }    

    public String CrearTabla1SoloElemento(Usuario usuario){
        ArrayList <Usuario> usuarioEncontrado = new ArrayList<>();
        
        //Añadimos el resultado al arrayList
        usuarioEncontrado.add(usuario);
        
        //Pasamos el nombre de la lista y el array con el objeto a buscar
        String html = generarHTML.PrepararTablaHTML("Objeto", usuarioEncontrado);
        
        return html;
    }
}
