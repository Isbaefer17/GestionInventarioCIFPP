package es.cifpp.gestion_inventario_cifpp.servicios;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cifpp.gestion_inventario_cifpp.entidades.Estado;
import es.cifpp.gestion_inventario_cifpp.repositorios.RepositorioEstado;

@Service
public class ServicioEstado {
    //Acesso a los repositorios
    @Autowired
        private RepositorioEstado repositorio;

    public ArrayList<Estado> ListarEstado(){
        ArrayList<Estado> listaEstado = (ArrayList<Estado>) repositorio.findAll(); 
        return listaEstado;
    }

    public Estado BuscarEstadoPorId(String id){
        Estado estadoABuscar = repositorio.findById(id).orElse(null);
        return estadoABuscar;
    }
}

