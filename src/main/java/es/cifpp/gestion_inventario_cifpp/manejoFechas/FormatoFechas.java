package es.cifpp.gestion_inventario_cifpp.manejoFechas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatoFechas {
    public static String enviarFechaFormateada(LocalDateTime fechaAFormatear){
        if (fechaAFormatear != null){
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale.of("es", "ES"));
            String fechaFormateada = fechaAFormatear.format(formato);
            return fechaFormateada;
        }
        return "Sin devolver";
    }

    public static String enviarFechaFormateada(LocalDate fechaAFormatear){
        if (fechaAFormatear != null){
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale.of("es", "ES"));
            String fechaFormateada = fechaAFormatear.format(formato);
            return fechaFormateada;
        }
        return "Sin devolver";
    }
}
