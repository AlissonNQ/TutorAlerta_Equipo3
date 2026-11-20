package tutoralerta.persistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import tutoralerta.modelo.Evaluacion;

public class RepositorioEvaluaciones {
    private static final String ARCHIVO = "evaluaciones.csv";
    private static final String ENCABEZADO = "id_evaluacion;nombre;fecha";

    public List<Evaluacion> cargar() {
        List<Evaluacion> evaluaciones = new ArrayList<>();
        List<String> lineas = GestorCSV.leerLineas(ARCHIVO);
        for (int i = 0; i < lineas.size(); i++) {
            String[] partes = lineas.get(i).split(";");
            if (partes.length >= 3) {
                String idEvaluacion = partes[0].trim();
                String nombre = partes[1].trim();
                LocalDate fecha = LocalDate.parse(partes[2].trim());
                Evaluacion evaluacion = new Evaluacion(idEvaluacion, nombre, fecha);
                evaluaciones.add(evaluacion);
            }
        }
        return evaluaciones;
    }

    public void guardar(List<Evaluacion> evaluaciones) {
        List<String> filas = new ArrayList<>();
        for (int i = 0; i < evaluaciones.size(); i++) {
            Evaluacion ev = evaluaciones.get(i);
            String fila = ev.getIdEvaluacion() + ";" + ev.getNombre() + ";" + ev.getFecha().toString();
            filas.add(fila);
        }
        GestorCSV.escribirLineas(ARCHIVO, ENCABEZADO, filas);
    }
}