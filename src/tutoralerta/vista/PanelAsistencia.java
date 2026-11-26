package tutoralerta.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import tutoralerta.modelo.Estudiante;
import tutoralerta.modelo.Sesion;
import tutoralerta.persistencia.RepositorioSesiones;
import tutoralerta.servicio.ServicioEstudiantes;

public class PanelAsistencia extends JPanel {

    private final ServicioEstudiantes servicioEstudiantes;
    private final RepositorioSesiones repositorioSesiones;

    private JTextField txtFecha;
    private JButton btnCargarLista;
    private JButton btnGuardar;
    private JButton btnLimpiar;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public PanelAsistencia() {
        this.servicioEstudiantes = new ServicioEstudiantes();
        this.repositorioSesiones = new RepositorioSesiones();
        inicializarComponentes();
        configurarEventos();
        cargarEstudiantesPresentes();
    }

    private void inicializarComponentes() {
        Color fondoCrema = new Color(250, 248, 245);
        Color azulMarino = new Color(15, 23, 42, 255);
        Color lavandaPastel = new Color(220, 216, 242);
        Color textoOscuro = new Color(45, 52, 64);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(fondoCrema);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(fondoCrema);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(fondoCrema);
        JLabel lblTitulo = new JLabel("Toma de Asistencia");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(textoOscuro);
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo);

        JPanel panelFecha = new JPanel(new GridLayout(1, 3, 8, 8));
        panelFecha.setBorder(BorderFactory.createTitledBorder("Fecha de la Sesión (AAAA-MM-DD)"));
        panelFecha.setBackground(fondoCrema);

        txtFecha = new JTextField(LocalDate.now().toString(), 15);
        btnCargarLista = new JButton("Cargar Lista");
        btnCargarLista.setBackground(lavandaPastel);
        btnCargarLista.setForeground(Color.BLACK);

        panelFecha.add(new JLabel("  Fecha:"));
        panelFecha.add(txtFecha);
        panelFecha.add(btnCargarLista);

        panelSuperior.add(panelFecha);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        panelBotones.setBackground(fondoCrema);

        btnGuardar = new JButton("Guardar Asistencia");
        btnLimpiar = new JButton("Restablecer");

        btnGuardar.setBackground(azulMarino);
        btnGuardar.setForeground(Color.WHITE);
        btnLimpiar.setBackground(lavandaPastel);
        btnLimpiar.setForeground(Color.BLACK);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);

        panelSuperior.add(panelBotones);
        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"Cédula", "Apellidos", "Nombres", "Presente"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(24);

        Color rojoOscuro = new Color(139, 24, 36);
        tabla.setSelectionBackground(lavandaPastel);
        tabla.setSelectionForeground(textoOscuro);
        tabla.getTableHeader().setBackground(rojoOscuro);
        tabla.getTableHeader().setForeground(Color.WHITE);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(90);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBackground(fondoCrema);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Estudiantes del Curso (Presentes por defecto)"));

        add(scrollTabla, BorderLayout.CENTER);
    }

    private void configurarEventos() {
        btnCargarLista.addActionListener(e -> cargarEstudiantesPresentes());
        btnGuardar.addActionListener(e -> guardarAsistencia());
        btnLimpiar.addActionListener(e -> limpiarPantalla());
    }

    private LocalDate obtenerFechaValida() {
        String textoFecha = txtFecha.getText();
        if (textoFecha == null || textoFecha.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese una fecha válida (AAAA-MM-DD).",
                    "Fecha requerida", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            return LocalDate.parse(textoFecha.trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use AAAA-MM-DD.",
                    "Error de fecha", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void cargarEstudiantesPresentes() {
        LocalDate fecha = obtenerFechaValida();
        if (fecha == null) {
            return;
        }

        modeloTabla.setRowCount(0);
        List<Estudiante> lista = servicioEstudiantes.listar();
        for (int i = 0; i < lista.size(); i++) {
            Estudiante est = lista.get(i);
            modeloTabla.addRow(new Object[]{
                    est.getCedula(),
                    est.getApellidos(),
                    est.getNombres(),
                    Boolean.TRUE
            });
        }
    }

    private void guardarAsistencia() {
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }

        LocalDate fecha = obtenerFechaValida();
        if (fecha == null) {
            return;
        }

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay estudiantes registrados para tomar asistencia.",
                    "Sin estudiantes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Sesion> sesionesExistentes = repositorioSesiones.cargar();
        for (int i = 0; i < sesionesExistentes.size(); i++) {
            if (sesionesExistentes.get(i).getFecha().equals(fecha)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe una sesión registrada para la fecha " + fecha + ".",
                        "Sesión duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        List<Sesion> nuevasSesiones = new ArrayList<>(sesionesExistentes);
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            String cedula = (String) modeloTabla.getValueAt(fila, 0);
            Boolean presente = (Boolean) modeloTabla.getValueAt(fila, 3);
            String estado = Boolean.TRUE.equals(presente) ? "PRESENTE" : "AUSENTE";
            nuevasSesiones.add(new Sesion(fecha, cedula, estado));
        }

        repositorioSesiones.guardar(nuevasSesiones);
        JOptionPane.showMessageDialog(this,
                "Asistencia guardada para la fecha " + fecha + ".",
                "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarPantalla() {
        txtFecha.setText(LocalDate.now().toString());
        cargarEstudiantesPresentes();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("TutorAlerta - Toma de Asistencia");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new PanelAsistencia());
            frame.setSize(750, 520);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}