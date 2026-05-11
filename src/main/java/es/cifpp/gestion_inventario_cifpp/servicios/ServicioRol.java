package es.cifpp.gestion_inventario_cifpp.servicios;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cifpp.gestion_inventario_cifpp.entidades.Rol;
import es.cifpp.gestion_inventario_cifpp.repositorios.RepositorioRol;

@Service
public class ServicioRol {
    //Acesso a los repositorios
    @Autowired
        private RepositorioRol repositorio;

    public ArrayList<Rol> ListarRoles(){
        ArrayList<Rol> listaRol = (ArrayList<Rol>) repositorio.findAll(); 
        return listaRol;
    }

    public Rol BuscarRolPorId(String id){
        Rol rolABuscar = repositorio.findById(id).orElse(null);
        return rolABuscar;
    }
}
