package es.cifpp.gestion_inventario_cifpp.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Tipo")
public class Tipo {
    @Id
    private String id;
    private String nombre;
    
    public Tipo() {

    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return getNombre();
    }

    
}
