package es.cifpp.gestion_inventario_cifpp.controlador;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.cifpp.gestion_inventario_cifpp.entidades.Tipo;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioTipo;
import es.cifpp.gestion_inventario_cifpp.utilidades.UtilidadesFormarHTML;

@Controller
public class ControladorTipo {
    @Autowired
        ServicioTipo servicio;
    
    @Autowired
        private UtilidadesFormarHTML generarHTML;

    @PostMapping("/anyadirTipo")
    public String anyadirTipo(@ModelAttribute Tipo tipo /* Crea el objeto Objeto con los datos del formulario*/, Model modelo) {
        System.out.println("TIPO RECIBIDO: " + tipo);
        
        //enviamos el objeto al servicio
        Tipo objetoGuardado = servicio.AnyadirTipo(tipo);
        
        String html = CrearTabla1SoloElemento(objetoGuardado);
        
        modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el Tipo se ha añadido corectamente: </p>" + html + "</div>");
        
        return "index"; // vuelve a index.html    
    }

    @GetMapping("/mostrarTipos")
    public String MostarTipo(Model modelo) {
        ArrayList<Tipo> tipos = servicio.ListarTipos();
        System.out.println(tipos);
        
        String html = generarHTML.PrepararTablaHTML("Tipo", tipos);
        modelo.addAttribute("tabla",html);

        return "Tipos";
    }

    @PostMapping("/buscarTipoPorID")
    public String buscarPorID(@RequestParam String codigoTipo /* guarda el id en un string introducido por formulario*/, Model modelo) {
        System.out.println("ID RECIBIDO: " + codigoTipo);
        
        //Buscamos el el objeto por su id y lo guardamos, si no existe devuelve null
        Tipo tipo = servicio.BuscarTipoPorId(codigoTipo);
        
        if (tipo != null){    
            String html = CrearTabla1SoloElemento(tipo);
            modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el tipo ha sido encontrado: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("anyadir", "<p>el tipo no se ha encontrado: </p>"); 
        }
        
        return "index";    
    }

    @PostMapping("/eliminarTipo")
    public String eliminarTipo(@RequestParam String id, Model modelo) {
        //antes de eliminar guardo el objeto para mostarlo después
        Tipo tipo = servicio.BuscarTipoPorId(id);
        
        //llamamos a elminar
        boolean seHaPodidoEliminar = servicio.EliminarTipo(id);
        
        //lo mostramos
        String html = CrearTabla1SoloElemento(tipo);
        
        if (seHaPodidoEliminar){
            modelo.addAttribute("eliminar", "<div class=\"ocultarBotones\"><p>el tipo ha sido eliminado: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("eliminar", "<p>el tipo no existe</p>");
        }
        
        return "index";
    }

    @PostMapping("/modificarTipo")
    public String modificarTipo(@RequestParam String id, @RequestParam (required = false) String nuevoCodigo, @RequestParam (required = false) String nuevoNombre, Model modelo) {
        //Guardamos llamamos al servicio para modificar
        Tipo tipoModificado = servicio.ModificarTipo(id, nuevoNombre);
        
        String html = CrearTabla1SoloElemento(tipoModificado);
        if (tipoModificado != null){
            modelo.addAttribute("modificar", "<div class=\"ocultarBotones\"><p>el tipo ha sido modificado: </p>" + html + "</div>");
        }

        else {
            modelo.addAttribute("modificar", "<p>el tipo no existe</p>");
        }
        
        return "index";
    }

    @GetMapping("/confirmarEliminarTipo")
    public String confirmarEliminar(@RequestParam String id, Model modelo) {
        Tipo tipo = servicio.BuscarTipoPorId(id);
        
        String html = CrearTabla1SoloElemento(tipo);
        modelo.addAttribute("tabla",html);
        
        // Aquí defines el action dinámico
        modelo.addAttribute("action", "/eliminarTipo");
        
        //añadimos el id ya que no lo métera por el post del formulario
        modelo.addAttribute("id", id);
        
        return "ConfirmarEliminar";
    }

    @GetMapping("/confirmarModificarTipo")
    public String confirmarModificar(@RequestParam String id, Model modelo) {
        Tipo tipo = servicio.BuscarTipoPorId(id);
        String html = CrearTabla1SoloElemento(tipo);
        modelo.addAttribute("tabla",html);
        
        // Aquí defines el action dinámico
        modelo.addAttribute("action", "/modificarTipo");
        
        //añadimos el id ya que no lo métera por el post del formulario
        modelo.addAttribute("id", id);
        String cuerpoFormulario = generarHTML.CrearFormularioTipo(tipo);
        
        modelo.addAttribute("cuerpoFormulario", cuerpoFormulario);
        
        modelo.addAttribute("formulario", "tipo");

        return "ConfirmarModificar";
    }

    public String CrearTabla1SoloElemento(Tipo tipo){
        ArrayList <Tipo> tipoEncontrado = new ArrayList<>();
        
        //Añadimos el resultado al arrayList
        tipoEncontrado.add(tipo);
        
        //Pasamos el nombre de la lista y el array con el objeto a buscar
        String html = generarHTML.PrepararTablaHTML("Tipo", tipoEncontrado);
        
        return html;
    }
}
