package tutoralerta.persistencia;

import java.util.ArrayList;
import java.util.List;
import tutoralerta.modelo.Nota;

public class RepositorioNotas {
    private static final String ARCHIVO = "notas.csv";
    private static final String ENCABEZADO = "id_evaluacion;cedula;calificacion";

    public List<Nota> cargar() {
        List<Nota> notas = new ArrayList<>();
        List<String> lineas = GestorCSV.leerLineas(ARCHIVO);
        for (int i = 0; i < lineas.size(); i++) {
            String[] partes = lineas.get(i).split(";");
            if (partes.length >= 3) {
                String idEvaluacion = partes[0].trim();
                String cedula = partes[1].trim();
                float calificacion = Float.parseFloat(partes[2].trim());
                Nota nota = new Nota(idEvaluacion, cedula, calificacion);
                notas.add(nota);
            }
        }
        return notas;
    }

    public void guardar(List<Nota> notas) {
        List<String> filas = new ArrayList<>();
        for (int i = 0; i < notas.size(); i++) {
            Nota n = notas.get(i);
            String fila = n.getIdEvaluacion() + ";" + n.getCedula() + ";" + n.getCalificacion();
            filas.add(fila);
        }
        GestorCSV.escribirLineas(ARCHIVO, ENCABEZADO, filas);
    }
}
