package es.cifpp.gestion_inventario_cifpp.entidades;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Objeto")
public class Objeto {
    @Id
        private String codigo;
    private String nombre;
    private Tipo tipo;

    //En Spring boot es necesario el constructor vacio ya que rellena los datos con los métodos set mediante un formulario HTML
    public Objeto(){

    }

    //Métodos GETTERS y SETTERS
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Objeto objeto = (Objeto) o;
        return Objects.equals(codigo, objeto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
    
    @Override
    public String toString() {
        return "Objeto; código: " + getCodigo() + ", Nombre: " + getNombre() + " Tipo de objeto: " + getTipo().getNombre();
    }
}
