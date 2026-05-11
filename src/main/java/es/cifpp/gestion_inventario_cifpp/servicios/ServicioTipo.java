package es.cifpp.gestion_inventario_cifpp.servicios;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cifpp.gestion_inventario_cifpp.entidades.Tipo;
import es.cifpp.gestion_inventario_cifpp.repositorios.RepositorioTipo;

@Service
public class ServicioTipo {
    //Aceso al repositorio
    @Autowired
        RepositorioTipo repositorio;

    public Tipo AnyadirTipo(Tipo tipo) {
        return repositorio.save(tipo);  
    }

    public ArrayList<Tipo> ListarTipos() {
        ArrayList<Tipo> listaTipos = (ArrayList<Tipo>) repositorio.findAll(); 
        return listaTipos;
    }

    public Tipo BuscarTipoPorId(String id) {
        Tipo tipoABuscar = repositorio.findById(id).orElse(null);
        return tipoABuscar;
    }

    public boolean EliminarTipo(String id) {
        Tipo tipoAEliminar = BuscarTipoPorId(id);

        if (tipoAEliminar == null){
            return false;
        }

        else{
            repositorio.deleteById(tipoAEliminar.getId());
            return true;
        } 
    }

    public Tipo ModificarTipo(String id, String nuevoNombre) {
        Tipo tipo = BuscarTipoPorId(id); 
        if (tipo != null){
            if (nuevoNombre !=null && !nuevoNombre.isBlank()){
                tipo.setNombre(nuevoNombre);
            }

            //Una vez cambiado los atributos del tipo lo añadimos
            AnyadirTipo(tipo);

            return tipo;
        }

        else return null;
    }
}
