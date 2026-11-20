package tutoralerta.persistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import tutoralerta.modelo.Sesion;

public class RepositorioSesiones {
    private static final String ARCHIVO = "sesiones.csv";
    private static final String ENCABEZADO = "fecha;cedula;estado";
    
    public List<Sesion> cargar() {
        List<Sesion> sesiones = new ArrayList<>();
        List<String> lineas = GestorCSV.leerLineas(ARCHIVO);
        for (int i = 0; i < lineas.size(); i++) {
            String[] partes = lineas.get(i).split(";");
            if (partes.length >= 3) {
                LocalDate fecha = LocalDate.parse(partes[0].trim());
                String cedula = partes[1].trim();
                String estado = partes[2].trim();
                Sesion sesion = new Sesion(fecha, cedula, estado);
                sesiones.add(sesion);
            }
        }
        return sesiones;
    }

    public void guardar(List<Sesion> sesiones) {
        List<String> filas = new ArrayList<>();
        for (int i = 0; i < sesiones.size(); i++) {
            Sesion s = sesiones.get(i);
            String fila = s.getFecha().toString() + ";" + s.getCedula() + ";" + s.getEstado();
            filas.add(fila);
        }
        GestorCSV.escribirLineas(ARCHIVO, ENCABEZADO, filas);
    }
}
