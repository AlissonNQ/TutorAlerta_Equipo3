package tutoralerta.servicio;

import java.util.Comparator;
import java.util.List;
import tutoralerta.modelo.Estudiante;
import tutoralerta.persistencia.RepositorioEstudiantes;

/**
 * Lógica de negocio para la HU-B1: Registrar estudiantes.
 * Validaciones: cédula de 10 dígitos, unicidad, campos no vacíos.
 */
public class ServicioEstudiantes {

    private final RepositorioEstudiantes repositorio;
    private List<Estudiante> estudiantes;

    public ServicioEstudiantes() {
        this.repositorio = new RepositorioEstudiantes();
        this.estudiantes = repositorio.cargar();
    }

    /**
     * Retorna la lista de estudiantes ordenada por apellidos (A-Z).
     */
    public List<Estudiante> listar() {
        estudiantes.sort(Comparator.comparing(
                Estudiante::getApellidos, String.CASE_INSENSITIVE_ORDER));
        return estudiantes;
    }

    /**
     * Registra un nuevo estudiante después de validar todos los campos.
     * @return null si fue exitoso, o un mensaje de error.
     */
    public String registrar(String cedula, String nombres, String apellidos, String correo) {
        // --- Validar campos vacíos ---
        if (cedula == null || cedula.trim().isEmpty()) {
            return "La cédula no puede estar vacía.";
        }
        if (nombres == null || nombres.trim().isEmpty()) {
            return "Los nombres no pueden estar vacíos.";
        }
        if (apellidos == null || apellidos.trim().isEmpty()) {
            return "Los apellidos no pueden estar vacíos.";
        }
        if (correo == null || correo.trim().isEmpty()) {
            return "El correo no puede estar vacío.";
        }

        cedula    = cedula.trim();
        nombres   = nombres.trim();
        apellidos = apellidos.trim();
        correo    = correo.trim();

        // --- Validar formato de cédula (exactamente 10 dígitos) ---
        String errorCedula = validarCedula(cedula);
        if (errorCedula != null) {
            return errorCedula;
        }

        // --- Validar unicidad de cédula ---
        if (existeCedula(cedula)) {
            return "Ya existe un estudiante con la cédula " + cedula + ".";
        }

        // --- Registrar ---
        Estudiante nuevo = new Estudiante(cedula, nombres, apellidos, correo);
        estudiantes.add(nuevo);
        estudiantes.sort(Comparator.comparing(
                Estudiante::getApellidos, String.CASE_INSENSITIVE_ORDER));
        repositorio.guardar(estudiantes);
        return null;
    }

    /**
     * Edita los datos de un estudiante existente (la cédula NO se modifica).
     * @return null si fue exitoso, o un mensaje de error.
     */
    public String editar(String cedula, String nombres, String apellidos, String correo) {
        // --- Validar campos vacíos ---
        if (nombres == null || nombres.trim().isEmpty()) {
            return "Los nombres no pueden estar vacíos.";
        }
        if (apellidos == null || apellidos.trim().isEmpty()) {
            return "Los apellidos no pueden estar vacíos.";
        }
        if (correo == null || correo.trim().isEmpty()) {
            return "El correo no puede estar vacío.";
        }

        Estudiante est = buscarPorCedula(cedula);
        if (est == null) {
            return "No se encontró un estudiante con la cédula " + cedula + ".";
        }

        est.setNombres(nombres.trim());
        est.setApellidos(apellidos.trim());
        est.setCorreo(correo.trim());

        estudiantes.sort(Comparator.comparing(
                Estudiante::getApellidos, String.CASE_INSENSITIVE_ORDER));
        repositorio.guardar(estudiantes);
        return null;
    }

    /**
     * Valida que la cédula tenga exactamente 10 dígitos numéricos.
     * @return null si es válida, o un mensaje de error.
     */
    public String validarCedula(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return "La cédula debe tener exactamente 10 dígitos.";
        }
        for (int i = 0; i < cedula.length(); i++) {
            if (!Character.isDigit(cedula.charAt(i))) {
                return "La cédula debe contener solo dígitos numéricos.";
            }
        }
        return null;
    }

    /**
     * Verifica si ya existe un estudiante con la cédula dada.
     */
    public boolean existeCedula(String cedula) {
        return buscarPorCedula(cedula) != null;
    }

    private Estudiante buscarPorCedula(String cedula) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getCedula().equals(cedula)) {
                return estudiantes.get(i);
            }
        }
        return null;
    }
}
