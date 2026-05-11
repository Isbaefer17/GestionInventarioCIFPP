package es.cifpp.gestion_inventario_cifpp.entidades;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import es.cifpp.gestion_inventario_cifpp.manejoFechas.FormatoFechas;

@Document ("Incidencia")
public class Incidencia {

    @Id
        private String codigo;
    private Usuario usuarioQueReporta;
    private Objeto objetoReportado;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Estado estado;
    
    public Incidencia() {
        this.fechaCreacion = LocalDateTime.now();
    }

    //Constructor para crear una Incidencia
    public Incidencia(String codigo, Usuario usuarioQueReporta, Objeto objetoReportado, String descripcion){
        this.codigo = codigo;
        this.usuarioQueReporta = usuarioQueReporta;
        this.objetoReportado = objetoReportado;
        this.descripcion = descripcion;
        this.fechaCreacion = LocalDateTime.now();
    }

    public String getCodigo() {
        return codigo;
    }

    public Usuario getUsuarioQueReporta() {
        return usuarioQueReporta;
    }

    public void setUsuarioQueReporta(Usuario usuarioQueReporta) {
        this.usuarioQueReporta = usuarioQueReporta;
    }

    public Objeto getObjetoReportado() {
        return objetoReportado;
    }

    public void setObjetoReportado(Objeto objetoReportado) {
        this.objetoReportado = objetoReportado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

     
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    // Setter protegido: Spring Data lo usa para rellenar la fecha desde MongoDB
    protected void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Incidencia [Código: " + getCodigo() + ", Usuario Que Reporta: " + getUsuarioQueReporta().getNombre() + " " +
                getUsuarioQueReporta().getApellido() + ", Objeto Reportado: " + getObjetoReportado().getNombre() + " Tipo: " +
                getObjetoReportado().getTipo() + ", Descripcion: " + getDescripcion() + ", Fecha De Creacion: " 
                + FormatoFechas.enviarFechaFormateada(getFechaCreacion()) + ", Estado: " + getEstado().getNombre() + "]";
    }  
}
