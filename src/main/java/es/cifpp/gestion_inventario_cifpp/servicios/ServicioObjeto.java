package es.cifpp.gestion_inventario_cifpp.servicios;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cifpp.gestion_inventario_cifpp.entidades.Objeto;
import es.cifpp.gestion_inventario_cifpp.entidades.Tipo;
import es.cifpp.gestion_inventario_cifpp.repositorios.RepositorioObjeto;

@Service
public class ServicioObjeto {
    //Acesso a los repositorios
    @Autowired
        private RepositorioObjeto repositorio;

    public Objeto AnyadirObjeto(Objeto objeto){
        return repositorio.save(objeto);  
    }

    public ArrayList<Objeto> ListarObjetos(){
        ArrayList<Objeto> listaObjetos = (ArrayList<Objeto>) repositorio.findAll(); 
        return listaObjetos;
    }

    public Objeto BuscarObjetoPorId(String id){
        Objeto objetoABuscar = repositorio.findById(id).orElse(null);
        return objetoABuscar;
    }

    public Objeto ModificarObjeto(String idActual, String nuevoCodigo, String nuevoNombre, Tipo nuevoTipo){
        Objeto objeto = BuscarObjetoPorId(idActual); 
        if (objeto != null){
            if (nuevoCodigo !=null && !nuevoCodigo.isBlank()){
                objeto.setCodigo(nuevoCodigo);
                EliminarObjeto(idActual);
            }

            if (nuevoNombre !=null && !nuevoNombre.isBlank()){
                objeto.setNombre(nuevoNombre);
            }

            if (nuevoTipo != null){
                objeto.setTipo(nuevoTipo);
            }

            //Una vez cambiado los atributos del objeto lo añadimos
            AnyadirObjeto(objeto);

            return objeto;
        }

        else return null;
    }

    public boolean EliminarObjeto(String id){
        Objeto objetoAEliminar = BuscarObjetoPorId(id);

        if (objetoAEliminar == null){
            return false;
        }

        else{
            repositorio.deleteById(objetoAEliminar.getCodigo());
            return true;
        } 
    }
}
