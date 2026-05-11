package es.cifpp.gestion_inventario_cifpp.utilidades;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import es.cifpp.gestion_inventario_cifpp.entidades.Estado;
import es.cifpp.gestion_inventario_cifpp.entidades.Objeto;
import es.cifpp.gestion_inventario_cifpp.entidades.Tipo;
import es.cifpp.gestion_inventario_cifpp.entidades.Usuario;
import es.cifpp.gestion_inventario_cifpp.manejoFechas.FormatoFechas;

@Component
public class UtilidadesFormarHTML {
    
    public String PrepararTablaHTML(String nombreLista, ArrayList<?> lista){
        if (lista == null || lista.isEmpty()) {
            return "<p>No hay datos</p>";
        }

        //Recojo los los atributos de la entidad
        Object ejemplo = lista.get(0);
        Field[] campos = lista.get(0).getClass().getDeclaredFields();

        ArrayList<String> nombresAtributos = new ArrayList<String>();
        ArrayList<Method> metodosGet = new ArrayList<>();

        // Emparejar atributo/getter en el mismo orden
        for (int i = 0; i < campos.length; i++) {
            String nombre = campos[i].getName();
            String NombreGet = "";

            if (!nombre.equalsIgnoreCase("Contrasenya")){ //Si es Usuario.getContraseña() lo omitimos porque eso no se puede mostrar
                nombresAtributos.add(nombre);
                NombreGet = "get" + Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1);
                try {
                    Method getter = ejemplo.getClass().getMethod(NombreGet);
                    metodosGet.add(getter);
                } catch (NoSuchMethodException e) {
                    System.out.println("No existe getter para: " + nombre);
                    metodosGet.add(null);
                }
            } 
        }

        try {
            return "<h1>Lista de " + nombreLista + "</h1>" +
                    generarTablaHtml(nombresAtributos, lista, metodosGet);
        } catch (Exception e) {
            e.printStackTrace();
            return "<p>Error generando tabla</p>";
        }
    }


    public String generarTablaHtml(ArrayList<String> nombresAtributos, ArrayList<?> lista, ArrayList<Method> metodosGet)
            throws IllegalAccessException, InvocationTargetException {

        StringBuilder html = new StringBuilder("<table><thead><tr>");

        // Encabezados
        for (String atributo : nombresAtributos) {
            //Con esto imprimimos de forma más natural y sin faltas de ortografía como son las tildes y las ñ
            switch (atributo){
                case "codigo":
                    html.append("<th>").append("código").append("</th>");
                    continue;
                case "usuarioQueReporta":
                    html.append("<th>").append("usuario que reportó").append("</th>");
                    continue;
                case "objetoReportado":
                    html.append("<th>").append("objeto que reporta el daño").append("</th>");
                    continue;
                case "descripcion":
                    html.append("<th>").append("descripción").append("</th>");
                    continue;
                case "fechaCreacion":
                    html.append("<th>").append("fecha de creación").append("</th>");
                    continue;
                case "ususarioQueSolicitaElPrestamo":
                    html.append("<th>").append("usuario que solicitó el préstamo").append("</th>");
                    continue;
                case "objetoPrestado":
                    html.append("<th>").append("objeto prestado").append("</th>");
                    continue;
                case "fechaLimite":
                    html.append("<th>").append("fecha límite").append("</th>");
                    continue;
                case "fechaDevolucion":
                    html.append("<th>").append("fecha del objeto devuelto").append("</th>");
                    continue;
            }
            //impirmir de forma normal
            html.append("<th>").append(atributo).append("</th>");
        }

        html.append("<th>Eliminar</th><th>Modificar</th>");
        html.append("</tr></thead><tbody>");

        // Filas
        for (Object obj : lista) {
            html.append("<tr>");

            String id = "";

            for (int j = 0; j < metodosGet.size(); j++) {
                Method getter = metodosGet.get(j);
                String nombreGetter = getter.getName();

                Object valor = (getter != null) ? getter.invoke(obj) : "";

                switch (nombreGetter) {

                    case "getId":
                    case "getCodigo":
                        id = String.valueOf(valor);
                        break;

                    case "getUsusarioQueSolicitaElPrestamo":
                    case "getUsuarioQueReporta":
                        Usuario usuario = (Usuario) valor;
                        html.append("<td>")
                            .append(usuario.getNombre())
                            .append(" ")
                            .append(usuario.getApellido())
                            .append("</td>");
                        continue;

                    case "getObjetoPrestado":
                    case "getObjetoReportado": 
                        Objeto objeto = (Objeto) valor;
                        html.append("<td>")
                            .append(objeto.getNombre())
                            .append("; Tipo ")
                            .append(objeto.getTipo())
                            .append("</td>");
                        continue;

                    case "getFechaDevolucion":
                        if (valor == null) {
                            html.append("<td>No se ha devuelto el objeto</td>");
                            continue;
                        }
                        break; // si no es null, entonces ya se ha devuelto el objeto. La fecha ira por el if de abajo
                }

                //Si es una fecha damos formato
                if (valor instanceof LocalDateTime){
                    String fechaConFormato = FormatoFechas.enviarFechaFormateada((LocalDateTime)valor);
                    html.append("<td>").append(fechaConFormato).append("</td>");
                    continue;
                } else if (valor instanceof LocalDate){
                    String fechaConFormato = FormatoFechas.enviarFechaFormateada((LocalDate)valor);
                    html.append("<td>").append(fechaConFormato).append("</td>");
                    continue;
                }
                //impresión normal
                html.append("<td>").append(valor).append("</td>");
            }

            String nombreClase = obj.getClass().getSimpleName();

            html.append("<td><a href=\"/confirmarEliminar")
                    .append(nombreClase).append("?id=").append(id)
                    .append("\"><button>Eliminar</button></a></td>");

            html.append("<td><a href=\"/confirmarModificar")
                    .append(nombreClase).append("?id=").append(id)
                    .append("\"><button>Modificar</button></a></td>");

            html.append("</tr>");
    }

    html.append("</tbody></table>");
    return html.toString();
    }

    public String CrearFormularioObjeto(ArrayList<Tipo> tipos){
        String html =   "<label for=\"nombreObjeto\">Modificar el código del objeto: </label>" +
                        "<input type=\"text\" name=\"nuevoCodigo\" placeholder=\"Código a modificar (opcional)\"><br/><br/>" +
                        "<label for=\"nombreObjeto\">Modificar el nombre del objeto: </label>" +
                        "<input type=\"text\" name=\"nuevoNombre\" placeholder=\"Nombre a modificar (opcional)\"><br/><br/>" +
                        "<label for=\"opcionesTipo\">Modificar el tipo de Objeto: </label>"+ 
                        "<select name=\"nuevoTipo\" id=\"opcionesTipo\">" + crearSelecionableDeTipos(tipos) +
                        "<option value=\"No modificar\" selected>No modificar</option>";
        return html;
    }

    public String crearSelecionableDeTipos(ArrayList<Tipo> tipos){
        //En este bucle se pone en el formulario para que selecione el tipo del objeto
        String html = "";
        for (int i = 0; i < tipos.size(); i++){
            html += "<option value=\"" + tipos.get(i).getId() + "\">" + tipos.get(i).getNombre() + "</option>";
        }

        return html;
    }

    public String CrearFormularioUsuario() {
        String html = "<label for=\"nuevoCodigo\">Introduce el nuevo codigo usuario:</label>" + 
                      "<input type=\"text\" name=\"nuevoCodigo\" placeholder=\"Id (opcional)\"><br/>" +
                      
                      "<label for=\"nuevoNombre\">Introduce el nuevo nombre del usuario: </label>" +
                      "<input type=\"text\" name=\"nuevoNombre\" placeholder=\"Nombre (opcional)\"><br/>" +
            
                      "<label for=\"nuevoApellido\">Introduce el nuevo apellido del usuario: </label>" +
                      "<input type=\"text\" name=\"nuevoApellido\" placeholder=\"Apellido (opcional)\"><br/>" +

                      "<label for=\"nuevoCorreo\">Introduce la nueva dirección de correo del usuario: </label>" + 
                      "<input type=\"email\" name=\"nuevoCorreo\" placeholder=\"Correo (opcional)\"><br/>" +

                      "<label for=\"nuevaContrasenya\">Introduce la nueva contraseña del usuario: </label>" +
                      "<input type=\"text\" name=\"nuevaContrasenya\" placeholder=\"Contraseña (opcional)\"><br/>" +

                      "<label for=\"confirmarContrasenya\">Confirma tu contraseña: </label>" + 
                      "<input type=\"text\" name=\"confirmarContrasenya\" placeholder=\"Confirmar contraseña\"><br/> ";
        return html;    
    }

    public String CrearFormularioTipo(Tipo tipo) {
        String html = "<label for=\"nombreTipo\">Modificar el nombre del Tipo: </label>" + //
                      "<input type=\"text\" name=\"nuevoNombre\" placeholder=\"Nombre a modificar (opcional)\"><br/><br/>";
        return html;
    }


    public String CrearFormularioPrestamo() {
        String html = "<label for=\"ususarioQueSolicitaElPrestamo\">Introduce el codigo del usuario que solicita el préstamo que quieres modificar:</label>" +
                      "<input type=\"text\" name=\"nuevoUsuuario\" placeholder=\"Id Usuario (opcional)\"><br/>" +
                      "<label for=\"objetoPrestado\">Introduce el codigo objeto que quieres cambiar: </label>" +
                      "<input type=\"text\" name=\"nuevoObjeto\" placeholder=\"Id Objeto (opcional)\"><br/>" +
                      "<label for=\"motivo\">Escribe nuevo motivo del préstamo: </label>" +
                      "<input type=\"text\" name=\"nuevoMotivo\" placeholder=\"Motivo del préstamo (opcional)\"><br/>" +
                      "<label for=\"motivo\">Introduce la nueva fecha límite del préstamo: </label>" +
                      "<input type=\"date\" name=\"nuevaFechaLimite\" placeholder=\"Fecha límite del préstamo (opcional)\"><br/>";
        return html;
    }


    public String CrearFormularioIncidencia(ArrayList<Estado> estados) {
        String html = "<label for=\"ususarioQueReporta\">Introduce el codigo del usuario que reporta la incidencia:</label>" +
                      "<input type=\"text\" name=\"nuevoUsuario\" placeholder=\"Id Usuario\"><br/>" +
                      "<label for=\"objetoReportado\">Introduce el codigo del objeto reportado en la incidencia: </label>" +
                      "<input type=\"text\" name=\"nuevoObjeto\" placeholder=\"Id Objeto\"><br/>" +
                      "<label for=\"motivo\">Escribe la descripción de la incidencia: </label>" +
                      "<input type=\"text\" name=\"nuevaDescripcion\" placeholder=\"Descripción de la incidencia\"><br/>" +
                      "<label for=\"opcionesEstado\">Modificar el estado de la incidencia: </label>"+ 
                      "<select name=\"nuevoEstado\" id=\"opcionesTipo\">" + crearSelecionableDeEstado(estados) +
                      "<option value=\"No modificar\" selected>No modificar</option>" +
                      "</select>";
        return html;
    }

    public String crearSelecionableDeEstado(ArrayList<Estado> estados){
        //En este bucle se pone en el formulario para que selecione el tipo del objeto
        String html = "";
        for (int i = 0; i < estados.size(); i++){
            html += "<option value=\"" + estados.get(i).getId() + "\">" + estados.get(i).getNombre() + "</option>";
        }

        return html;
    }
}
