package es.cifpp.gestion_inventario_cifpp.controlador;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.cifpp.gestion_inventario_cifpp.entidades.Estado;
import es.cifpp.gestion_inventario_cifpp.entidades.Incidencia;
import es.cifpp.gestion_inventario_cifpp.entidades.Objeto;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioEstado;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioIncidencia;
import es.cifpp.gestion_inventario_cifpp.servicios.ServicioObjeto;
import es.cifpp.gestion_inventario_cifpp.utilidades.UtilidadesFormarHTML;


@Controller
public class ControladorInventario {

    @Autowired
        private ServicioObjeto servicioObjeto;

    @Autowired 
        ServicioIncidencia servicioIncidencia;

    @Autowired
        ServicioEstado servicioEstado;

    @Autowired
        private UtilidadesFormarHTML generarHTML;
     
    @GetMapping("/GenerarInventario")
    public String generarInventario(Model modelo) {
       
        //Primero mostramos los objetos
        ArrayList<Objeto> objetos = servicioObjeto.ListarObjetos();
        
        String htmlObjetos = generarHTML.PrepararTablaHTML("Objetos", objetos);
        
        modelo.addAttribute("tablaObjetos", htmlObjetos);

        //Luego mostramos las incidencias las cuales no están solucionadas:
        //Busco el estado que no queremos incluir
        Estado estado = servicioEstado.BuscarEstadoPorId("6a009591d0cffcd67d8c3b72");
        System.out.println(estado);

        //Busco todas las incidencias
        ArrayList<Incidencia> incidencias = servicioIncidencia.ListarIncidencia();

        //creo un arrayList para guardar las incidencias con el estado
        ArrayList<Incidencia> incidenciasPorEstado = new ArrayList<Incidencia>();    

        for (int i = 0; i < incidencias.size(); i++){
            if (incidencias.get(i).getEstado() != null && estado != null && !incidencias.get(i).getEstado().equals(estado)) {
                incidenciasPorEstado.add(incidencias.get(i));
            }
        }

        System.out.println("lista filtrada   :  " + incidenciasPorEstado);

        //Realizamos la tabla
        String htmlIncidencias = generarHTML.PrepararTablaHTML("Incidencias activas", incidenciasPorEstado);
        
        modelo.addAttribute("tablaIncidencias",htmlIncidencias);

        return "inventario";
    }
    
}
