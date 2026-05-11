package es.cifpp.gestion_inventario_cifpp.controlador;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.cifpp.gestion_inventario_cifpp.entidades.Objeto;
import es.cifpp.gestion_inventario_cifpp.entidades.Tipo;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioObjeto;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioTipo;
import es.cifpp.gestion_inventario_cifpp.utilidades.UtilidadesFormarHTML;

@Controller
public class ControladorObjetos {
    @Autowired
        private ServicioObjeto servicio;

    @Autowired
        private ServicioTipo servicioTipo;

    @Autowired
        private UtilidadesFormarHTML generarHTML;

    @PostMapping("/anyadirObjeto")
    public String anyadirObjeto(@ModelAttribute Objeto objeto, /* Crea el objeto Objeto con los datos del formulario*/
                                @RequestParam String tipo, Model modelo) {
        System.out.println("OBJETO RECIBIDO: " + objeto);
        
        //Buscamos el tipo y se lo añado al objeto
        Tipo tipoABuscar = servicioTipo.BuscarTipoPorId(tipo);
        objeto.setTipo(tipoABuscar);
        
        //enviamos el objeto al servicio
        Objeto objetoGuardado = servicio.AnyadirObjeto(objeto);
        
        String html = CrearTabla1SoloElemento(objetoGuardado);
        modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el objeto se ha añadido corectamente: </p>" + html + "</div>");
        
        return "index"; // vuelve a index.html    
    }

    @GetMapping("/mostrarObjetos")
    public String MostarObjetos(Model modelo){
        ArrayList<Objeto> objetos = servicio.ListarObjetos();
        
        String html = generarHTML.PrepararTablaHTML("Objetos", objetos);
        
        modelo.addAttribute("tabla",html);

        //como el seleccionable se carga al cargar la tabla lo añadimos desde aquí
        //Obtenemos el arrayList de tipos:
        ArrayList<Tipo> tipos = servicioTipo.ListarTipos();
        String cuerpoSelecionable = generarHTML.crearSelecionableDeTipos(tipos);
        
        modelo.addAttribute("selecionarTipos",cuerpoSelecionable);
        
        return "Objetos";
    }
    
    @PostMapping("/buscarObjetoPorID")
    public String buscarPorID(@RequestParam String codigoObjeto /* guarda el id en un string introducido por formulario*/, Model modelo) {
        System.out.println("ID RECIBIDO: " + codigoObjeto);
        
        //Buscamos el el objeto por su id y lo guardamos, si no existe devuelve null
        Objeto objeto = servicio.BuscarObjetoPorId(codigoObjeto);
        
        if (objeto != null){    
            String html = CrearTabla1SoloElemento(objeto);
            modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el objeto ha sido encontrado: </p>" + html + "</div>");
        }
        
        else{
           modelo.addAttribute("anyadir", "<p>el objeto no se ha encontrado: </p>"); 
        
        }
        return "index";    
    }

    @PostMapping("/modificarObjeto")
    public String modificarObjeto(@RequestParam String id, @RequestParam (required = false) String nuevoCodigo, 
                                @RequestParam (required = false) String nuevoNombre, 
                                @RequestParam (required = false) String nuevoTipo, Model modelo) {
        
        //Buscamos el tipo
        Tipo TipoAModificar = null;
        if (!nuevoTipo.equals("No modificar")){
            TipoAModificar = servicioTipo.BuscarTipoPorId(nuevoTipo);
        }
        
        //Guardamos y llamamos al servicio para modificar
        Objeto objetoModificado = servicio.ModificarObjeto(id, nuevoCodigo, nuevoNombre, TipoAModificar);
        
        String html = CrearTabla1SoloElemento(objetoModificado);
        
        if (objetoModificado != null){
            modelo.addAttribute("modificar", "<div class=\"ocultarBotones\"><p>el objeto ha sido modificado: </p>" + html + "</div>");
        }

        else {
            modelo.addAttribute("modificar", "<p>el objeto no existe</p>");
        }
        return "index";
    }

    @PostMapping("/eliminarObjeto")
    public String eliminarObjeto(@RequestParam String id, Model modelo) {
        //antes de eliminar guardo el objeto para mostarlo después
        Objeto objeto = servicio.BuscarObjetoPorId(id);
        
        //llamamos a elminar
        boolean seHaPodidoEliminar = servicio.EliminarObjeto(id);
        
        //lo mostramos
        String html = CrearTabla1SoloElemento(objeto);
        
        if (seHaPodidoEliminar){
            modelo.addAttribute("eliminar", "<div class=\"ocultarBotones\"><p>el objeto ha sido eliminado: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("eliminar", "<p>el objeto no existe</p>");
        }
        return "index";
    }

    @GetMapping("/confirmarModificarObjeto")
    public String confirmarModificar(@RequestParam String id, Model modelo) {
        Objeto objeto = servicio.BuscarObjetoPorId(id);
        
        String html = CrearTabla1SoloElemento(objeto);
        modelo.addAttribute("tabla",html);
        
        // Aquí defines el action dinámico
        modelo.addAttribute("action", "/modificarObjeto");
        
        //añadimos el id ya que no lo métera por el post del formulario
        modelo.addAttribute("id", id);
        
        //Obtenemos el arrayList de tipos:
        ArrayList<Tipo> tipos = servicioTipo.ListarTipos();
        
        String cuerpoFormulario = generarHTML.CrearFormularioObjeto(tipos);
        modelo.addAttribute("cuerpoFormulario", cuerpoFormulario);

        modelo.addAttribute("formulario", "objeto");

        return "ConfirmarModificar";
    }
    
    @GetMapping("/confirmarEliminarObjeto")
    public String confirmarEliminar(@RequestParam String id, Model modelo) {
        Objeto objeto = servicio.BuscarObjetoPorId(id);
        
        String html = CrearTabla1SoloElemento(objeto);
        modelo.addAttribute("tabla",html);
        
        // Aquí defines el action dinámico
        modelo.addAttribute("action", "/eliminarObjeto");
        
        //añadimos el id ya que no lo métera por el post del formulario
        modelo.addAttribute("id", id);
        
        return "ConfirmarEliminar";
    }


    public String CrearTabla1SoloElemento(Objeto objeto){
        ArrayList <Objeto> objetoEncontrado = new ArrayList<>();
        
        //Añadimos el resultado al arrayList
        objetoEncontrado.add(objeto);
        
        //Pasamos el nombre de la lista y el array con el objeto a buscar
        String html = generarHTML.PrepararTablaHTML("Objeto", objetoEncontrado);
        
        return html;
    }
}
