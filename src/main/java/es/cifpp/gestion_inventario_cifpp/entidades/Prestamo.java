package es.cifpp.gestion_inventario_cifpp.entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import es.cifpp.gestion_inventario_cifpp.manejoFechas.FormatoFechas;

@Document("Prestamo")
public class Prestamo {
    @Id 
       private String codigo;
    private Usuario ususarioQueSolicitaElPrestamo; 
    private String motivo;
    private Objeto objetoPrestado;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaLimite;
    private LocalDateTime fechaDevolucion;
    
    //constructor para spring data
    public Prestamo() {

    }

     // Constructor para crear nuevos préstamos
    public Prestamo(Usuario usuario, String motivo, Objeto objeto, LocalDate fechaLimite) {
        this.ususarioQueSolicitaElPrestamo = usuario;
        this.motivo = motivo;
        this.objetoPrestado = objeto;
        this.fechaLimite = fechaLimite;
        this.fechaCreacion = LocalDateTime.now();
    }

    public String getCodigo() {
        return codigo;
    }

    public Usuario getUsusarioQueSolicitaElPrestamo() {
        return ususarioQueSolicitaElPrestamo;
    }

    public void setUsusarioQueSolicitaElPrestamo(Usuario ususarioQueSolicitaElPrestamo) {
        this.ususarioQueSolicitaElPrestamo = ususarioQueSolicitaElPrestamo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Objeto getObjetoPrestado() {
        return objetoPrestado;
    }

    public void setObjetoPrestado(Objeto objetoPrestado) {
        this.objetoPrestado = objetoPrestado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    } 

    public void setFechaCreacion(LocalDateTime fecha) {
        this.fechaCreacion = fecha;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    @Override
    public String toString() {
        return "Prestamo [codigo: " + getCodigo() + ", motivo: " + motivo + " Usuario que solicita el prestamo" +
                getUsusarioQueSolicitaElPrestamo().getNombre() + " " + getUsusarioQueSolicitaElPrestamo().getApellido() + 
                ", Objeto prestado: " + getObjetoPrestado().getNombre()
                + ", Fecha de registro del préstamo: " + FormatoFechas.enviarFechaFormateada(getFechaCreacion()) + ", Fecha límite: " 
                + FormatoFechas.enviarFechaFormateada(getFechaLimite()) + ", Fecha de devolución del préstamo: "
                + FormatoFechas.enviarFechaFormateada(getFechaDevolucion()) + "]";
    } 
}
