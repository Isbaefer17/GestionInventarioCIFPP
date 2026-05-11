package es.cifpp.gestion_inventario_cifpp.controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.cifpp.gestion_inventario_cifpp.entidades.Objeto;
import es.cifpp.gestion_inventario_cifpp.entidades.Prestamo;
import es.cifpp.gestion_inventario_cifpp.entidades.Usuario;
import es.cifpp.gestion_inventario_cifpp.manejoFechas.FormatoFechas;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioObjeto;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioPrestamo;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioUsuario;
import es.cifpp.gestion_inventario_cifpp.utilidades.UtilidadesFormarHTML;



@Controller
public class ControladorPrestamo {
    @Autowired 
        ServicioPrestamo servicio;

    @Autowired
        ServicioObjeto servicioObjeto;

    @Autowired
        ServicioUsuario servicioUsuario;

    @Autowired
        UtilidadesFormarHTML generarHTML;

    @PostMapping("/anyadirPrestamo")
    public String anyadirPrestamo(@ModelAttribute Prestamo prestamo, /* Crea el objeto Objeto con los datos del formulario*/
                                @RequestParam String objetoId, @RequestParam String usuarioId, Model modelo) {
        
        System.out.println("idUsuario: " + usuarioId + " idObjeto: " + objetoId);
        
        //Buscamos el Objeto
        Objeto objetoABuscar = servicioObjeto.BuscarObjetoPorId(objetoId);

        //verificamos si el objeto está disponible
        Prestamo posiblePrestamoConObjetoQueQueremos = buscarPrestamoMedianteObjeto(objetoABuscar);
        
        // si es null significa que no hay un prestamo activo con ese objeto
        if (posiblePrestamoConObjetoQueQueremos == null){
            //  y se lo añado al Prestamo
            if (objetoABuscar != null){
                prestamo.setObjetoPrestado(objetoABuscar);
            }  
            else{
                modelo.addAttribute("anyadir", "<p>El Objeto no existe</p>");
                return "index"; 
            }      
        }

        else{
            modelo.addAttribute("anyadir", "<p>El Objeto no esta disponiblle para prestarse</p>");
            return "index"; 
        }

        //Buscamos el Usuario y se lo añado al Prestamo
        Usuario usuarioABuscar = servicioUsuario.BuscarUsuarioPorCodigo(usuarioId);
        
        if (usuarioABuscar != null){
            prestamo.setUsusarioQueSolicitaElPrestamo(usuarioABuscar);
        }
        else{
            modelo.addAttribute("anyadir", "<p>El Usuario no existe</p>");
            return "index";
        }
        
        System.out.println("PRÉSTAMO RECIBIDO: " + prestamo);
        
        //enviamos el prestamo al servicio
        Prestamo prestamoGuardado = servicio.AnyadirPrestamo(prestamo);
        
        String html = CrearTabla1SoloElemento(prestamoGuardado);
        modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el préstamo se ha añadido corectamente: </p>" + html + "</div>");
        
        return "index"; // vuelve a index.html    
    }

    @GetMapping("/mostrarPrestamos")
    public String MostarPrestamos(Model modelo) {
        ArrayList<Prestamo> prestamos = servicio.ListarPrestamos();

        System.out.println("lista de prestamos" + prestamos);
        
        String html = generarHTML.PrepararTablaHTML("Prestamos", prestamos);
        
        modelo.addAttribute("tabla",html);
        
        return "Prestamos";
    }

    @PostMapping("/buscarPrestamoPorID")
    public String buscarPorID(@RequestParam String codigoPrestamo /* guarda el id en un string introducido por formulario*/, Model modelo) {
        System.out.println("ID RECIBIDO: " + codigoPrestamo);
        
        //Buscamos el el objeto por su id y lo guardamos, si no existe devuelve null
        Prestamo prestamo = servicio.BuscarPrestamoPorId(codigoPrestamo);
        
        if (prestamo != null){    
            String html = CrearTabla1SoloElemento(prestamo);
            modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el préstamo ha sido encontrado: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("anyadir", "<p>el préstamo no se ha encontrado: </p>"); 
        }
        
        return "index";    
    }

    @PostMapping("/modificarPrestamo")
    public String modificarPrestamo(@RequestParam String id, @RequestParam (required = false) String nuevoUsuuario, 
                                @RequestParam (required = false) String nuevoMotivo, 
                                @RequestParam (required = false) String nuevoObjeto,
                                @RequestParam (required = false) LocalDate nuevaFechaLimite, Model modelo) {
        
        System.out.println("id: " + id + " nuevoUsuario: " + nuevoUsuuario + " nuevoMotivo: " + nuevoMotivo + " nuevoObjeto" + nuevoObjeto + " FechaLímite: " + FormatoFechas.enviarFechaFormateada(nuevaFechaLimite));
        //Buscamos el usuario
        Usuario ususario = null;
        
        if (nuevoUsuuario != null){
            ususario = servicioUsuario.BuscarUsuarioPorCodigo(nuevoUsuuario);
        }
        
        //Buscamos el objeto
        Objeto objeto = null;
        if (nuevoObjeto != null){
            objeto = servicioObjeto.BuscarObjetoPorId(nuevoObjeto);
        }
        
        //Guardamos y llamamos al préstamo para modificar
        Prestamo prestamoModificado = servicio.ModificarPrestamo(id, ususario, nuevoMotivo, objeto, nuevaFechaLimite);
        String html = CrearTabla1SoloElemento(prestamoModificado);
        
        if (prestamoModificado != null){
            modelo.addAttribute("modificar", "<div class=\"ocultarBotones\"><p>el préstamo ha sido modificado: </p>" + html + "</div>");
        }
        else {
            modelo.addAttribute("modificar", "<p>el préstamo no existe</p>");
        }
        
        return "index";
    }

    @PostMapping("/eliminarPrestamo")
    public String eliminarPrestamo(@RequestParam String id, Model modelo) {
        //antes de eliminar guardo el prestamo para mostarlo después
        Prestamo prestamo = servicio.BuscarPrestamoPorId(id);
        
        //llamamos a elminar
        boolean seHaPodidoEliminar = servicio.EliminarPrestamo(id);
        
        //lo mostramos
        String html = CrearTabla1SoloElemento(prestamo);
        
        if (seHaPodidoEliminar){
            modelo.addAttribute("eliminar", "<div class=\"ocultarBotones\"><p>el préstamo ha sido eliminado: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("eliminar", "<p>el préstamo no existe</p>");
        }
        return "index";
    }

    @GetMapping("/confirmarModificarPrestamo")
    public String confirmarModificar(@RequestParam String id, Model modelo) {
        Prestamo prestamo = servicio.BuscarPrestamoPorId(id);
        
        String html = CrearTabla1SoloElemento(prestamo);
        modelo.addAttribute("tabla",html);
        
        // Aquí defines el action dinámico
        modelo.addAttribute("action", "/modificarPrestamo");
        
        //añadimos el id ya que no lo métera por el post del formulario
        modelo.addAttribute("id", id);
        String cuerpoFormulario = generarHTML.CrearFormularioPrestamo();
        
        modelo.addAttribute("cuerpoFormulario", cuerpoFormulario);

        modelo.addAttribute("formulario", "prestamo");
        
        return "ConfirmarModificar";
    }

    @GetMapping("/confirmarEliminarPrestamo")
    public String confirmarEliminar(@RequestParam String id, Model modelo) {
        Prestamo prestamo = servicio.BuscarPrestamoPorId(id);
        
        String html = CrearTabla1SoloElemento(prestamo);
        modelo.addAttribute("tabla",html);
        
        // Aquí defines el action dinámico
        modelo.addAttribute("action", "/eliminarPrestamo");
        
        //añadimos el id ya que no lo métera por el post del formulario
        modelo.addAttribute("id", id);
        
        return "ConfirmarEliminar";
    }

    @PostMapping("/mostrarPrestamosActivosUsuario")
    public String mostarPrestamosActivos(@RequestParam String idUsuario, Model modelo) {
        //Obtenemos todos los prestamos.
        ArrayList<Prestamo> prestamos = servicio.ListarPrestamos();
        
        //obtenemos el usuario
        Usuario usuario = servicioUsuario.BuscarUsuarioPorCodigo(idUsuario);
        System.out.println(usuario);

        //creamos un arrayList nuevo para los préstamos que coinciden con la busqueda
        ArrayList<Prestamo> prestamosActivosUsuario = new ArrayList<Prestamo>();
        
        //miramos uno por uno si son del usuario y están activos
        for (int i = 0; i < prestamos.size(); i++){
            if (prestamos.get(i).getUsusarioQueSolicitaElPrestamo().equals(usuario) 
                && prestamos.get(i).getFechaDevolucion() == null){
            System.out.println("dentro");
                    prestamosActivosUsuario.add(prestamos.get(i));
            }
        }

        //Realizamos la tabla
        String html = generarHTML.PrepararTablaHTML("Prestamos Activos del usuario" + usuario.getNombre() + " " + usuario.getApellido(), prestamosActivosUsuario);
        
        modelo.addAttribute("tabla",html);

        //Añadimos de nuevo los datos del usuario ya que estamos generando una petición nueva
        modelo.addAttribute("miId", idUsuario);
        modelo.addAttribute("nombreUsuario", "Bienvenido " + usuario.getNombre() + " " + usuario.getApellido());

        return "index";
    }

    @PostMapping("/devolverPrestamo")
    public String postMethodName(@RequestParam String idObjeto, Model modelo) {
        //Buscamos el objeto
        System.out.println(idObjeto);
        Objeto objeto = servicioObjeto.BuscarObjetoPorId(idObjeto);
        System.out.println(objeto);
        Prestamo prestamo = buscarPrestamoMedianteObjeto(objeto);
        if (prestamo != null){
            prestamo.setFechaDevolucion(LocalDateTime.now());

            servicio.AnyadirPrestamo(prestamo);

            String html = CrearTabla1SoloElemento(prestamo);
        
            modelo.addAttribute("tabla",html);
        }

        else{
            modelo.addAttribute("tabla", "<p>El objeto no existe</p>");
        }
        return "index";
    }
    
    @GetMapping("/BuscarPrestamosActivos")
    public String getMethodName(Model modelo) {
        //Buscamos todos los préstamos
        ArrayList<Prestamo> prestamos = servicio.ListarPrestamos();

        //Creamos un arrayList de prestamos activos
        ArrayList<Prestamo> prestamosActivos = new ArrayList<Prestamo>();

        for (int i = 0; i < prestamos.size(); i++){
            if (prestamos.get(i).getFechaDevolucion() == null){
                prestamosActivos.add(prestamos.get(i));
            }
        }

        //Realizamos la tabla
        String html = generarHTML.PrepararTablaHTML("Prestamos Activos", prestamosActivos);
        
        modelo.addAttribute("tabla",html);
        
        return "index";
    }
    
    public String CrearTabla1SoloElemento(Prestamo prestamo){
        ArrayList <Prestamo> prestamoEncontrado = new ArrayList<>();
        
        //Añadimos el resultado al arrayList
        prestamoEncontrado.add(prestamo);
        
        //Pasamos el nombre de la lista y el array con el objeto a buscar
        
        String html = generarHTML.PrepararTablaHTML("Prestamo", prestamoEncontrado);
        
        return html;
    }

    public Prestamo buscarPrestamoMedianteObjeto (Objeto objeto){
        //Obtenemos todos los prestamos.
        ArrayList<Prestamo> prestamos = servicio.ListarPrestamos();
        
        //miramos uno por uno si el objeto todavía esta prestado
        for (int i = 0; i < prestamos.size(); i++){
            if (prestamos.get(i).getObjetoPrestado().equals(objeto) 
                && prestamos.get(i).getFechaDevolucion() == null){
                    return prestamos.get(i);
            }
        }
        
        //devolvemos null si el objeto no está prestado actualmente
        return null;
    }
}
