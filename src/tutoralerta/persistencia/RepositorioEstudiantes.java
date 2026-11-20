package tutoralerta.persistencia;

import java.util.ArrayList;
import java.util.List;
import tutoralerta.modelo.Estudiante;

public class RepositorioEstudiantes {
    private static final String ARCHIVO = "estudiantes.csv";
    private static final String ENCABEZADO = "cedula;nombres;apellidos;correo";

    public List<Estudiante> cargar(){
        List<Estudiante> estudiantes = new ArrayList<>();
        List<String> lineas = GestorCSV.leerLineas(ARCHIVO);
        for (int i=0; i < lineas.size(); i++){
            String[] partes = lineas.get(i).split(";");
            if(partes.length >= 4){
                Estudiante est = new Estudiante(
                    partes[0].trim(), 
                    partes[1].trim(),
                    partes[2].trim(),
                    partes[3].trim()
                );
                estudiantes.add(est);
            }
        }
        return estudiantes;
    }

    public void guardar(List<Estudiante> estudiantes){
        List<String> filas = new ArrayList<>();
        for(int i=0; i < estudiantes.size(); i++){
            Estudiante e = estudiantes.get(i);
            String fila = e.getCedula() + ";" + e.getNombres() + ";" + e.getApellidos() + ";" + e.getCorreo();
            filas.add(fila);
        }
        GestorCSV.escribirLineas(ARCHIVO, ENCABEZADO, filas);
    }
}
