package tutoralerta.servicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import tutoralerta.modelo.Evaluacion;
import tutoralerta.modelo.Nota;
import tutoralerta.persistencia.RepositorioEvaluaciones;
import tutoralerta.persistencia.RepositorioNotas;

public class ServicioNotas {

    private static final Pattern PATRON_NOTA = Pattern.compile("^\\d{1,2}([.,]\\d{1,2})?$");
    private static final BigDecimal NOTA_MIN = BigDecimal.ZERO;
    private static final BigDecimal NOTA_MAX = new BigDecimal("20.00");

    private final RepositorioEvaluaciones repositorioEvaluaciones;
    private final RepositorioNotas repositorioNotas;
    private List<Evaluacion> evaluaciones;
    private List<Nota> notas;

    public ServicioNotas() {
        this.repositorioEvaluaciones = new RepositorioEvaluaciones();
        this.repositorioNotas = new RepositorioNotas();
        recargarDesdeArchivo();
    }

    public void recargarDesdeArchivo() {
        this.evaluaciones = repositorioEvaluaciones.cargar();
        this.notas = repositorioNotas.cargar();
    }

    public List<Evaluacion> listarEvaluaciones() {
        evaluaciones.sort(Comparator.comparing(Evaluacion::getFecha).reversed()
                .thenComparing(Evaluacion::getNombre, String.CASE_INSENSITIVE_ORDER));
        return evaluaciones;
    }

    public String crearEvaluacion(String nombre, String fechaTexto) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre de la evaluación no puede estar vacío.";
        }
        String nombreLimpio = nombre.trim();
        if (nombreLimpio.contains(";")) {
            return "El nombre de la evaluación no puede contener punto y coma (;).";
        }

        if (fechaTexto == null || fechaTexto.trim().isEmpty()) {
            return "La fecha de la evaluación es obligatoria (AAAA-MM-DD).";
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaTexto.trim());
        } catch (DateTimeParseException e) {
            return "Formato de fecha inválido. Use AAAA-MM-DD (ejemplo: 2026-11-26).";
        }

        for (Evaluacion ev : evaluaciones) {
            if (ev.getNombre().equalsIgnoreCase(nombreLimpio) && ev.getFecha().equals(fecha)) {
                return "Ya existe una evaluación '" + nombreLimpio + "' en la fecha " + fecha + ".";
            }
        }

        Evaluacion nueva = new Evaluacion(generarNuevoId(), nombreLimpio, fecha);
        evaluaciones.add(nueva);
        repositorioEvaluaciones.guardar(evaluaciones);
        return null;
    }

    private String generarNuevoId() {
        int max = 0;
        for (Evaluacion ev : evaluaciones) {
            String id = ev.getIdEvaluacion();
            if (id != null && id.startsWith("EV-")) {
                try {
                    int num = Integer.parseInt(id.substring(3));
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("EV-%03d", max + 1);
    }

    public Map<String, Double> obtenerNotasPorEvaluacion(String idEvaluacion) {
        Map<String, Double> mapa = new HashMap<>();
        for (Nota n : notas) {
            if (n.getIdEvaluacion().equals(idEvaluacion)) {
                mapa.put(n.getCedula(), n.getCalificacion());
            }
        }
        return mapa;
    }

    public String validarCalificacion(String textoNota, String nombreEstudiante) {
        if (textoNota == null || textoNota.trim().isEmpty()) {
            return "La calificación de " + nombreEstudiante + " no puede estar vacía.";
        }

        String limpio = textoNota.trim();
        if (!PATRON_NOTA.matcher(limpio).matches()) {
            return "Calificación inválida para " + nombreEstudiante + " ('" + limpio + "'). "
                    + "Ingrese un número entre 0 y 20 con máximo 2 decimales (sin letras).";
        }

        BigDecimal valor = new BigDecimal(limpio.replace(',', '.'));
        if (valor.compareTo(NOTA_MIN) < 0 || valor.compareTo(NOTA_MAX) > 0) {
            return "Calificación fuera de rango para " + nombreEstudiante + " (" + valor + "). "
                    + "Debe estar entre 0 y 20.";
        }

        return null;
    }

    public double parsearCalificacion(String textoNota) {
        double valor = Double.parseDouble(textoNota.trim().replace(',', '.'));
        return Math.round(valor * 100.0) / 100.0;
    }

    public String guardarNotasDeEvaluacion(String idEvaluacion, List<Nota> nuevasNotas) {
        if (idEvaluacion == null || idEvaluacion.trim().isEmpty()) {
            return "Debe seleccionar o crear una evaluación antes de guardar.";
        }
        if (nuevasNotas == null || nuevasNotas.isEmpty()) {
            return "No hay calificaciones para guardar.";
        }

        List<Nota> notasActualizadas = new ArrayList<>();
        for (Nota n : notas) {
            if (!n.getIdEvaluacion().equals(idEvaluacion)) {
                notasActualizadas.add(n);
            }
        }
        notasActualizadas.addAll(nuevasNotas);

        this.notas = notasActualizadas;
        repositorioNotas.guardar(this.notas);
        return null;
    }
}