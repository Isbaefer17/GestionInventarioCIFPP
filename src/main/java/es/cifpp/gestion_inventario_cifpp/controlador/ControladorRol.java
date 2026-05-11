package es.cifpp.gestion_inventario_cifpp.controlador;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.cifpp.gestion_inventario_cifpp.entidades.Rol;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioRol;
import es.cifpp.gestion_inventario_cifpp.utilidades.UtilidadesFormarHTML;

@Controller
public class ControladorRol {
    @Autowired
        private ServicioRol servicio;

    @Autowired
        private UtilidadesFormarHTML generarHTML;

    @PostMapping("/buscarRolPorID")
    public String buscarPorID(@RequestParam String codigoRol /* guarda el id en un string introducido por formulario*/, Model modelo) {
        System.out.println("ID RECIBIDO: " + codigoRol);
        
        //Buscamos el el objeto por su id y lo guardamos, si no existe devuelve null
        Rol rol = servicio.BuscarRolPorId(codigoRol);
        
        if (rol != null){    
            String html = CrearTabla1SoloElemento(rol);
            modelo.addAttribute("anyadir", "<div class=\"ocultarBotones\"><p>el rol ha sido encontrado: </p>" + html + "</div>");
        }
        else{
           modelo.addAttribute("anyadir", "<p>el rol no se ha encontrado: </p>"); 
        }
        
        return "index";    
    }

    public String CrearTabla1SoloElemento(Rol rol){
        ArrayList <Rol> objetoEncontrado = new ArrayList<>();
        
        //Añadimos el resultado al arrayList
        objetoEncontrado.add(rol);
        
        //Pasamos el nombre de la lista y el array con el objeto a buscar
        String html = generarHTML.PrepararTablaHTML("Rol", objetoEncontrado);
        
        return html;
    }
}

