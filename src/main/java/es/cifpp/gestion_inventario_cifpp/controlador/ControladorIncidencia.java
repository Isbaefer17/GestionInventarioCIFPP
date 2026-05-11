package es.cifpp.gestion_inventario_cifpp.controlador;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.cifpp.gestion_inventario_cifpp.entidades.Estado;
import es.cifpp.gestion_inventario_cifpp.entidades.Incidencia;
import es.cifpp.gestion_inventario_cifpp.entidades.Objeto;
import es.cifpp.gestion_inventario_cifpp.entidades.Usuario;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioEstado;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioIncidencia;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioObjeto;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioUsuario;
import es.cifpp.gestion_inventario_cifpp.utilidades.UtilidadesFormarHTML;

@Controller
public class ControladorIncidencia {
    @Autowired 
        ServicioIncidencia servicio;

    @Autowired
        ServicioObjeto servicioObjeto;

    @Autowired
        ServicioUsuario servicioUsuario;

    @Autowired
        ServicioEstado servicioEstado;

    @Autowired
        UtilidadesFormarHTML generarHTML;

    @PostMapping("/anyadirIncidencia")
    public String anyadirIncidencia(@ModelAttribute Incidencia incidencia, /* Crea el objeto Objeto con los datos del formulario*/
                                @RequestParam String objetoId, @RequestParam String usuarioId, Model modelo) {
        
        System.out.println("idUsuario: " + usuarioId + " idObjeto: " + objetoId);
        
        //Buscamos el Objeto
        Objeto objetoABuscar = servicioObjeto.BuscarObjetoPorId(objetoId);
        
        //se lo añado a la incidencia
        if (objetoABuscar != null){
            incidencia.setObjetoReportado(objetoABuscar);
            }  
        else{
            modelo.addAttribute("anyadir", "<p>El Objeto no existe</p>");
            return "index"; 
        } 
        
        //Buscamos el Usuario y se lo añado al Prestamo
        Usuario usuarioABuscar = servicioUsuario.BuscarUsuarioPorCodigo(usuarioId);
        
        if (usuarioABuscar != null){
            incidencia.setUsuarioQueReporta(usuarioABuscar);
        }
        else{
            modelo.addAttribute("anyadir", "<p>El Usuario no existe</p>");
            return "index";
        }
        
        //le añadimos el Estado por defecto (Abierto)
        Estado estado = servicioEstado.BuscarEstadoPorId("6a009848d0cffcd67d8c3b78");
        incidencia.setEstado(estado);

        System.out.println("INCIDENCIA RECIBIDA: " + incidencia);
        
        //enviamos el prestamo al servicio
        Incidencia incidenciaGuardada = servicio.AnyadirIncidencia(incidencia);
        
        String html = CrearTabla1SoloElemento(incidenciaGuardada);
        modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el incidencia se ha añadido corectamente: </p>" + html + "</div>");
        
        return "index"; // vuelve a index.html    
    }
    @GetMapping("/mostrarIncidencias")
    public String MostarUsuarios(Model modelo) {
        ArrayList<Incidencia> incidencias = servicio.ListarIncidencia();
        
        String html = generarHTML.PrepararTablaHTML("Incidencias", incidencias);
        
        modelo.addAttribute("tabla",html);

        //como el seleccionable se carga al cargar la tabla lo añadimos desde aquí
        //Obtenemos el arrayList de Estados:
        ArrayList<Estado> estados = servicioEstado.ListarEstado();
        String cuerpoSelecionable = generarHTML.crearSelecionableDeEstado(estados);
        
        modelo.addAttribute("selecionarEstados",cuerpoSelecionable);

        return "Incidencias";
    }

    @PostMapping("/buscarIncidenciaPorID")
    public String buscarPorID(@RequestParam String codigoIncidencia /* guarda el id en un string introducido por formulario*/, Model modelo) {
        System.out.println("ID RECIBIDO: " + codigoIncidencia);
        
        //Buscamos el el objeto por su id y lo guardamos, si no existe devuelve null
        Incidencia incidencia = servicio.BuscarIncidenciaPorId(codigoIncidencia);
        
        if (incidencia != null){    
            String html = CrearTabla1SoloElemento(incidencia);
            modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>la incidencia ha sido encontrado: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("anyadir", "<p>la incidencia no se ha encontrado: </p>"); 
        }
        
        return "index";    
    }

    @PostMapping("/modificarIncidencia")
    public String modificarIncidencia(@RequestParam String id, @RequestParam (required = false) String nuevoUsuario, 
                                      @RequestParam (required = false) String nuevoObjeto,                            
                                      @RequestParam (required = false) String nuevaDescripcion, 
                                      @RequestParam (required = false) String nuevoEstado ,Model modelo) {
        
        System.out.println("id: " + id + " nuevoUsuario: " + nuevoUsuario + " nuevaDescripcion: " + nuevaDescripcion + " nuevoObjeto" + nuevoObjeto);
        //Buscamos el usuario
        Usuario ususario = null;
        
        if (nuevoUsuario != null){
            ususario = servicioUsuario.BuscarUsuarioPorCodigo(nuevoUsuario);
        }
        
        //Buscamos el objeto
        Objeto objeto = null;
        if (nuevoObjeto != null){
            objeto = servicioObjeto.BuscarObjetoPorId(nuevoObjeto);
        }

        //Buscamos el Estado
        Estado estadoAModificar = null;
        if (!nuevoEstado.equals("No modificar")){
            estadoAModificar = servicioEstado.BuscarEstadoPorId(nuevoEstado);
        }
        
        //Guardamos y llamamos al préstamo para modificar
        Incidencia incidenciaModificada = servicio.ModificarIncidencia(id, ususario, objeto, nuevaDescripcion, estadoAModificar);
        String html = CrearTabla1SoloElemento(incidenciaModificada);
        
        if (incidenciaModificada != null){
            modelo.addAttribute("modificar", "<div class=\"ocultarBotones\"><p>La incidencia ha sido modificada: </p>" + html + "</div>");
        }
        else {
            modelo.addAttribute("modificar", "<p>La incidencia no existe</p>");
        }
        
        return "index";
    }

    @PostMapping("/eliminarIncidencia")
    public String eliminarIncidencia(@RequestParam String id, Model modelo) {
        //antes de eliminar guardo el prestamo para mostarlo después
        Incidencia incidencia = servicio.BuscarIncidenciaPorId(id);
        
        //llamamos a elminar
        boolean seHaPodidoEliminar = servicio.EliminarIcidencia(id);
        
        //lo mostramos
        String html = CrearTabla1SoloElemento(incidencia);
        
        if (seHaPodidoEliminar){
            modelo.addAttribute("eliminar", "<div class=\"ocultarBotones\"><p>La incidencia ha sido eliminada: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("eliminar", "<p>La incidencia no existe</p>");
        }
        return "index";
    }

    @GetMapping("/confirmarModificarIncidencia")
    public String confirmarModificar(@RequestParam String id, Model modelo) {
        Incidencia incidencia = servicio.BuscarIncidenciaPorId(id);
        
        String html = CrearTabla1SoloElemento(incidencia);
        modelo.addAttribute("tabla",html);
        
        // Aquí defines el action dinámico
        modelo.addAttribute("action", "/modificarIncidencia");
        
        //añadimos el id ya que no lo meterá por el post del formulario
        modelo.addAttribute("id", id);

        //Busco todos los estados
        ArrayList<Estado> estados = servicioEstado.ListarEstado();
        String cuerpoFormulario = generarHTML.CrearFormularioIncidencia(estados);
        
        modelo.addAttribute("cuerpoFormulario", cuerpoFormulario);

        modelo.addAttribute("formulario", "incidencia");
        
        return "ConfirmarModificar";
    }

    @GetMapping("/confirmarEliminarIncidencia")
    public String confirmarEliminar(@RequestParam String id, Model modelo) {
        Incidencia incidencia = servicio.BuscarIncidenciaPorId(id);
        
        String html = CrearTabla1SoloElemento(incidencia);
        modelo.addAttribute("tabla",html);
        
        // Aquí defines el action dinámico
        modelo.addAttribute("action", "/eliminarIncidencia");
        
        //añadimos el id ya que no lo métera por el post del formulario
        modelo.addAttribute("id", id);
        
        return "ConfirmarEliminar";
    }

    @PostMapping("/buscarPorEstado")
    public String BuscarPorEstado(@RequestParam String estadoId, Model modelo) {
        //Busco el estado
        Estado estado = servicioEstado.BuscarEstadoPorId(estadoId);

        //Busco todas las incidencias
        ArrayList<Incidencia> incidencias = servicio.ListarIncidencia();

        //creo un arrayList para guardar las incidencias con el estado
        ArrayList<Incidencia> incidenciasPorEstado = new ArrayList<Incidencia>();    

        for (int i = 0; i < incidencias.size(); i++){
            if (incidencias.get(i).getEstado().equals(estado)){
                incidenciasPorEstado.add(incidencias.get(i));
            }
        }

        //Realizamos la tabla
        String html = generarHTML.PrepararTablaHTML("Incidencias", incidenciasPorEstado);
        
        modelo.addAttribute("tabla",html);
        
        return "index";

    }
    

    public String CrearTabla1SoloElemento(Incidencia incidencia){
        ArrayList <Incidencia> incidenciaEncontrado = new ArrayList<>();
        
        //Añadimos el resultado al arrayList
        incidenciaEncontrado.add(incidencia);
        
        //Pasamos el nombre de la lista y el array con el objeto a buscar
        
        String html = generarHTML.PrepararTablaHTML("Incidencia", incidenciaEncontrado);
        
        return html;
    }
}
