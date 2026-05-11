package es.cifpp.gestion_inventario_cifpp.servicios;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cifpp.gestion_inventario_cifpp.entidades.Estado;
import es.cifpp.gestion_inventario_cifpp.entidades.Incidencia;
import es.cifpp.gestion_inventario_cifpp.entidades.Objeto;
import es.cifpp.gestion_inventario_cifpp.entidades.Usuario;
import es.cifpp.gestion_inventario_cifpp.repositorios.RepositorioIncidencia;

@Service
public class ServicioIncidencia {
    //Aceso al repositorio
    @Autowired
        RepositorioIncidencia repositorio;

    public Incidencia AnyadirIncidencia(Incidencia incidencia) {
        return repositorio.save(incidencia);  
    }

    public ArrayList<Incidencia> ListarIncidencia() {
        ArrayList<Incidencia> listaIncidencia = (ArrayList<Incidencia>) repositorio.findAll(); 
        System.out.println(listaIncidencia);
        return listaIncidencia;
    }

    public Incidencia BuscarIncidenciaPorId(String id) {
        Incidencia incidenciaABuscar = repositorio.findById(id).orElse(null);
        return incidenciaABuscar;
    }

    public boolean EliminarIcidencia(String id) {
        Incidencia incidenciaAEliminar = BuscarIncidenciaPorId(id);

        if (incidenciaAEliminar == null){
            return false;
        }

        else{
            repositorio.deleteById(incidenciaAEliminar.getCodigo());
            return true;
        } 
    }

    public Incidencia ModificarIncidencia(String id, Usuario nuevoUsuarioQueReporta, Objeto nuevoObjetoReportado,  String nuevaDescripcion, Estado nuevoEstado) {
        Incidencia incidencia = BuscarIncidenciaPorId(id); 
        System.out.println(incidencia);
        if (incidencia != null){
            if (nuevoUsuarioQueReporta != null){
                incidencia.setUsuarioQueReporta(nuevoUsuarioQueReporta);
            }

            if (nuevaDescripcion != null && !nuevaDescripcion.isBlank()){
                incidencia.setDescripcion(nuevaDescripcion);
            }

            if (nuevoObjetoReportado != null){
                incidencia.setObjetoReportado(nuevoObjetoReportado);
            }

            if (nuevoEstado != null){
                incidencia.setEstado(nuevoEstado);
            }

            //Una vez cambiado los atributos del tipo lo añadimos
            AnyadirIncidencia(incidencia);

            return incidencia;
        }

        else return null;
    }
}
