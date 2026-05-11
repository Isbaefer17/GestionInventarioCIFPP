package es.cifpp.gestion_inventario_cifpp.servicios;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cifpp.gestion_inventario_cifpp.entidades.Objeto;
import es.cifpp.gestion_inventario_cifpp.entidades.Prestamo;
import es.cifpp.gestion_inventario_cifpp.entidades.Usuario;
import es.cifpp.gestion_inventario_cifpp.repositorios.RepositorioPrestamo;

@Service
public class ServicioPrestamo {
    //Aceso al repositorio
    @Autowired
        RepositorioPrestamo repositorio;

    public Prestamo AnyadirPrestamo(Prestamo prestamo) {
        return repositorio.save(prestamo);  
    }

    public ArrayList<Prestamo> ListarPrestamos() {
        ArrayList<Prestamo> listaPrestamos = (ArrayList<Prestamo>) repositorio.findAll(); 
        System.out.println(listaPrestamos);
        return listaPrestamos;
    }

    public Prestamo BuscarPrestamoPorId(String id) {
        Prestamo prestamoABuscar = repositorio.findById(id).orElse(null);
        return prestamoABuscar;
    }

    public boolean EliminarPrestamo(String id) {
        Prestamo prestamoAEliminar = BuscarPrestamoPorId(id);

        if (prestamoAEliminar == null){
            return false;
        }

        else{
            repositorio.deleteById(prestamoAEliminar.getCodigo());
            return true;
        } 
    }

    public Prestamo ModificarPrestamo(String id, Usuario nuevoUsuarioAPrestar ,String nuevoMotivo, Objeto nuevoObjeto,
                                      LocalDate NuevaFechaLimite) {
        Prestamo prestamo = BuscarPrestamoPorId(id); 
        System.out.println(prestamo);
        if (prestamo != null){
            if (nuevoUsuarioAPrestar != null){
                prestamo.setUsusarioQueSolicitaElPrestamo(nuevoUsuarioAPrestar);
            }

            if (nuevoMotivo != null && !nuevoMotivo.isBlank()){
                prestamo.setMotivo(nuevoMotivo);
            }

            if (nuevoObjeto != null){
                prestamo.setObjetoPrestado(nuevoObjeto);
            }

            if (NuevaFechaLimite != null) {
                prestamo.setFechaLimite(NuevaFechaLimite);
            }

            //Una vez cambiado los atributos del tipo lo añadimos
            AnyadirPrestamo(prestamo);

            return prestamo;
        }

        else return null;
    }
}
