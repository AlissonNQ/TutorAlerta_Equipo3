package tutoralerta.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import tutoralerta.modelo.Estudiante;
import tutoralerta.modelo.Evaluacion;
import tutoralerta.modelo.Nota;
import tutoralerta.servicio.ServicioEstudiantes;
import tutoralerta.servicio.ServicioNotas;

public class PanelNotas extends JPanel {

    private final ServicioNotas servicioNotas;

    private JTextField txtNombreEvaluacion;
    private JTextField txtFechaEvaluacion;
    private JComboBox<Evaluacion> cmbEvaluaciones;

    private JButton btnCrearEvaluacion;
    private JButton btnGuardarNotas;
    private JButton btnRecargar;
    private JButton btnLimpiar;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private boolean actualizandoCombo;

    public PanelNotas() {
        this.servicioNotas = new ServicioNotas();
        this.actualizandoCombo = false;
        inicializarComponentes();
        configurarEventos();
        recargarDatos();
    }

    private void inicializarComponentes() {
        Color fondoCrema = new Color(250, 248, 245);
        Color azulMarino = new Color(15, 23, 42, 255);
        Color lavandaPastel = new Color(220, 216, 242);
        Color textoOscuro = new Color(45, 52, 64);
        Color rojoOscuro = new Color(139, 24, 36);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(fondoCrema);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(fondoCrema);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(fondoCrema);
        JLabel lblTitulo = new JLabel("Registro de Notas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(textoOscuro);
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo);

        JPanel panelEvaluacion = new JPanel(new GridLayout(3, 2, 8, 8));
        panelEvaluacion.setBorder(BorderFactory.createTitledBorder("Datos de la Evaluación"));
        panelEvaluacion.setBackground(fondoCrema);

        txtNombreEvaluacion = new JTextField(20);
        txtFechaEvaluacion  = new JTextField(LocalDate.now().toString(), 20);
        cmbEvaluaciones     = new JComboBox<>();

        panelEvaluacion.add(new JLabel("  Nombre de la Evaluación:"));
        panelEvaluacion.add(txtNombreEvaluacion);
        panelEvaluacion.add(new JLabel("  Fecha (AAAA-MM-DD):"));
        panelEvaluacion.add(txtFechaEvaluacion);
        panelEvaluacion.add(new JLabel("  Evaluación Seleccionada:"));
        panelEvaluacion.add(cmbEvaluaciones);

        panelSuperior.add(panelEvaluacion);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        panelBotones.setBackground(fondoCrema);

        btnCrearEvaluacion = new JButton("Crear Evaluación");
        btnGuardarNotas    = new JButton("Guardar Notas");
        btnRecargar        = new JButton("Recargar Guardadas");
        btnLimpiar         = new JButton("Limpiar Celdas");

        btnCrearEvaluacion.setBackground(azulMarino);
        btnCrearEvaluacion.setForeground(Color.WHITE);
        btnGuardarNotas.setBackground(azulMarino);
        btnGuardarNotas.setForeground(Color.WHITE);
        btnRecargar.setBackground(lavandaPastel);
        btnRecargar.setForeground(Color.BLACK);
        btnLimpiar.setBackground(lavandaPastel);
        btnLimpiar.setForeground(Color.BLACK);

        panelBotones.add(btnCrearEvaluacion);
        panelBotones.add(btnGuardarNotas);
        panelBotones.add(btnRecargar);
        panelBotones.add(btnLimpiar);

        panelSuperior.add(panelBotones);
        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"Cédula", "Apellidos", "Nombres", "Nota (0 - 20)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(24);
        tabla.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        tabla.setSelectionBackground(lavandaPastel);
        tabla.setSelectionForeground(textoOscuro);
        tabla.getTableHeader().setBackground(rojoOscuro);
        tabla.getTableHeader().setForeground(Color.WHITE);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(110);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centrado);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBackground(fondoCrema);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
                "Calificaciones del Curso (Rango: 0 a 20, hasta 2 decimales)"));

        add(scrollTabla, BorderLayout.CENTER);
    }

    private void configurarEventos() {
        btnCrearEvaluacion.addActionListener(e -> crearEvaluacion());
        btnGuardarNotas.addActionListener(e -> guardarNotas());
        btnRecargar.addActionListener(e -> {
            recargarDatos();
            JOptionPane.showMessageDialog(this,
                    "Evaluaciones y nómina de estudiantes recargadas desde CSV.",
                    "Datos actualizados", JOptionPane.INFORMATION_MESSAGE);
        });
        btnLimpiar.addActionListener(e -> limpiarCeldasNotas());

        cmbEvaluaciones.addActionListener(e -> {
            if (!actualizandoCombo && cmbEvaluaciones.getSelectedItem() instanceof Evaluacion ev) {
                cargarTablaParaEvaluacion(ev);
            }
        });
    }

    public void recargarDatos() {
        Evaluacion previa = (Evaluacion) cmbEvaluaciones.getSelectedItem();
        String idPrevio = (previa != null) ? previa.getIdEvaluacion() : null;

        List<Estudiante> estudiantes = new ServicioEstudiantes().listar();
        servicioNotas.recargarDesdeArchivo();
        List<Evaluacion> evaluaciones = servicioNotas.listarEvaluaciones();

        actualizandoCombo = true;
        cmbEvaluaciones.removeAllItems();
        Evaluacion seleccionada = null;
        for (Evaluacion ev : evaluaciones) {
            cmbEvaluaciones.addItem(ev);
            if (idPrevio != null && ev.getIdEvaluacion().equals(idPrevio)) {
                seleccionada = ev;
            }
        }
        if (seleccionada != null) {
            cmbEvaluaciones.setSelectedItem(seleccionada);
        } else if (!evaluaciones.isEmpty()) {
            cmbEvaluaciones.setSelectedIndex(0);
            seleccionada = evaluaciones.get(0);
        }
        actualizandoCombo = false;

        cargarTablaConEstudiantes(estudiantes, seleccionada);
    }

    private void cargarTablaParaEvaluacion(Evaluacion evaluacion) {
        cargarTablaConEstudiantes(new ServicioEstudiantes().listar(), evaluacion);
    }

    private void cargarTablaConEstudiantes(List<Estudiante> estudiantes, Evaluacion evaluacion) {
        if (tabla.isEditing()) {
            tabla.getCellEditor().cancelCellEditing();
        }
        modeloTabla.setRowCount(0);

        Map<String, Double> mapaNotas = (evaluacion != null)
                ? servicioNotas.obtenerNotasPorEvaluacion(evaluacion.getIdEvaluacion())
                : Map.of();

        for (Estudiante est : estudiantes) {
            Double valorNota = mapaNotas.get(est.getCedula());
            String textoNota = (valorNota != null) ? String.format(Locale.US, "%.2f", valorNota) : "";
            modeloTabla.addRow(new Object[]{
                    est.getCedula(),
                    est.getApellidos(),
                    est.getNombres(),
                    textoNota
            });
        }
    }

    private void crearEvaluacion() {
        String nombre = txtNombreEvaluacion.getText();
        String fecha = txtFechaEvaluacion.getText();

        String error = servicioNotas.crearEvaluacion(nombre, fecha);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error,
                    "Error al crear evaluación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreCreado = nombre.trim();
        txtNombreEvaluacion.setText("");
        txtFechaEvaluacion.setText(LocalDate.now().toString());

        recargarDatos();
        for (int i = 0; i < cmbEvaluaciones.getItemCount(); i++) {
            Evaluacion ev = cmbEvaluaciones.getItemAt(i);
            if (ev.getNombre().equalsIgnoreCase(nombreCreado)) {
                cmbEvaluaciones.setSelectedIndex(i);
                break;
            }
        }

        JOptionPane.showMessageDialog(this,
                "Evaluación creada exitosamente. Ahora ingrese las notas en la tabla.",
                "Evaluación creada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void guardarNotas() {
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }

        if (!(cmbEvaluaciones.getSelectedItem() instanceof Evaluacion evaluacionActiva)) {
            JOptionPane.showMessageDialog(this,
                    "Primero debe crear o seleccionar una evaluación.",
                    "Evaluación requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay estudiantes registrados para asignar calificaciones.",
                    "Sin estudiantes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Nota> notasPorGuardar = new ArrayList<>();
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            String cedula = String.valueOf(modeloTabla.getValueAt(fila, 0)).trim();
            String apellidos = String.valueOf(modeloTabla.getValueAt(fila, 1)).trim();
            String nombres = String.valueOf(modeloTabla.getValueAt(fila, 2)).trim();
            Object celdaNota = modeloTabla.getValueAt(fila, 3);
            String textoNota = (celdaNota == null) ? "" : celdaNota.toString();

            String errorNota = servicioNotas.validarCalificacion(textoNota, apellidos + " " + nombres);
            if (errorNota != null) {
                tabla.setRowSelectionInterval(fila, fila);
                tabla.editCellAt(fila, 3);
                Component editor = tabla.getEditorComponent();
                if (editor != null) {
                    editor.requestFocusInWindow();
                }
                JOptionPane.showMessageDialog(this, errorNota,
                        "Nota inválida (Fila " + (fila + 1) + ")", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double calificacion = servicioNotas.parsearCalificacion(textoNota);
            notasPorGuardar.add(new Nota(evaluacionActiva.getIdEvaluacion(), cedula, calificacion));
        }

        String errorGuardado = servicioNotas.guardarNotasDeEvaluacion(
                evaluacionActiva.getIdEvaluacion(), notasPorGuardar);

        if (errorGuardado != null) {
            JOptionPane.showMessageDialog(this, errorGuardado,
                    "Error al guardar", JOptionPane.ERROR_MESSAGE);
        } else {
            cargarTablaParaEvaluacion(evaluacionActiva);
            JOptionPane.showMessageDialog(this,
                    "Calificaciones guardadas exitosamente para '" + evaluacionActiva.getNombre() + "'.",
                    "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarCeldasNotas() {
        if (tabla.isEditing()) {
            tabla.getCellEditor().cancelCellEditing();
        }
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            modeloTabla.setValueAt("", fila, 3);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("TutorAlerta - Registro de Notas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new PanelNotas());
            frame.setSize(750, 520);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}