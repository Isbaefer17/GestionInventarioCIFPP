package es.cifpp.gestion_inventario_cifpp.controlador;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.cifpp.gestion_inventario_cifpp.entidades.Estado;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioEstado;
import es.cifpp.gestion_inventario_cifpp.utilidades.UtilidadesFormarHTML;

@Controller
public class ControladorEstado {
    @Autowired
        private ServicioEstado servicio;

    @Autowired
        private UtilidadesFormarHTML generarHTML;

    @PostMapping("/buscarEstadoPorID")
    public String buscarPorID(@RequestParam String codigoEstado /* guarda el id en un string introducido por formulario*/, Model modelo) {
        System.out.println("ID RECIBIDO: " + codigoEstado);
        
        //Buscamos el el objeto por su id y lo guardamos, si no existe devuelve null
        Estado estado = servicio.BuscarEstadoPorId(codigoEstado);
        
        if (estado != null){    
            String html = CrearTabla1SoloElemento(estado);
            modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el Estado ha sido encontrado: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("anyadir", "<p>el Estado no se ha encontrado: </p>"); 
        }
        
        return "index";    
    }

    public String CrearTabla1SoloElemento(Estado estado){
        ArrayList <Estado> estadoEncontrado = new ArrayList<>();
        
        //Añadimos el resultado al arrayList
        estadoEncontrado.add(estado);
        
        //Pasamos el nombre de la lista y el array con el objeto a buscar
        String html = generarHTML.PrepararTablaHTML("Estado", estadoEncontrado);
        
        return html;
    }
}
