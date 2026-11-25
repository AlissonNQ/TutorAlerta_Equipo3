package tutoralerta.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import tutoralerta.servicio.ServicioEstudiantes;

/**
 * Panel Swing para la HU-B1: Registrar estudiantes.
 * Permite registrar, listar (ordenado por apellido) y editar estudiantes.
 * La cédula no se puede modificar después de registrar.
 */
public class PanelEstudiantes extends JPanel {

    private final ServicioEstudiantes servicio;

    // --- Campos del formulario ---
    private JTextField txtCedula;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtCorreo;

    // --- Botones ---
    private JButton btnRegistrar;
    private JButton btnEditar;
    private JButton btnLimpiar;

    // --- Tabla ---
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private boolean modoEdicion;

    public PanelEstudiantes() {
        this.servicio = new ServicioEstudiantes();
        this.modoEdicion = false;
        inicializarComponentes();
        configurarEventos();
        actualizarTabla();
    }

    // =========================================================================
    //  INICIALIZACIÓN DE LA INTERFAZ
    // =========================================================================

    private void inicializarComponentes() {
        Color fondoCrema = new Color(250, 248, 245);
        Color azulMarino = new Color(15,23, 42, 255);
        //Color mentaPastel = new Color(196, 232, 214);
        Color lavandaPastel = new Color(220, 216, 242);
        Color textoOscuro = new Color(45, 52, 64);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(fondoCrema);

        // --- Panel superior: título + formulario + botones ---
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(fondoCrema);

        // Título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(fondoCrema);
        JLabel lblTitulo = new JLabel("Registro de Estudiantes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(textoOscuro);
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo);
        

        // Formulario de datos
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Estudiante"));
        panelFormulario.setBackground(fondoCrema);

        txtCedula    = new JTextField(20);
        txtNombres   = new JTextField(20);
        txtApellidos = new JTextField(20);
        txtCorreo    = new JTextField(20);

        panelFormulario.add(new JLabel("  Cédula (10 dígitos):"));
        panelFormulario.add(txtCedula);
        panelFormulario.add(new JLabel("  Nombres:"));
        panelFormulario.add(txtNombres);
        panelFormulario.add(new JLabel("  Apellidos:"));
        panelFormulario.add(txtApellidos);
        panelFormulario.add(new JLabel("  Correo:"));
        panelFormulario.add(txtCorreo);

        panelSuperior.add(panelFormulario);

        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        panelBotones.setBackground(fondoCrema);

        btnRegistrar = new JButton("Registrar");
        btnEditar    = new JButton("Guardar Cambios");
        btnLimpiar   = new JButton("Limpiar");

        //colores
        btnRegistrar.setBackground(azulMarino);
        btnRegistrar.setForeground(Color.WHITE);
        btnEditar.setBackground(azulMarino);
        btnEditar.setForeground(Color.WHITE);
        btnLimpiar.setBackground(lavandaPastel);
        btnLimpiar.setForeground(Color.BLACK);
        //

        btnEditar.setEnabled(false);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnLimpiar);

        panelSuperior.add(panelBotones);

        add(panelSuperior, BorderLayout.NORTH);

        // --- Tabla de estudiantes (centro) ---
        String[] columnas = {"Cédula", "Nombres", "Apellidos", "Correo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(22);

        //colores
        Color rojoOscuro = new Color(139, 24, 36);
        tabla.setSelectionBackground(lavandaPastel);
        tabla.setSelectionForeground(textoOscuro);
        tabla.getTableHeader().setBackground(rojoOscuro);
        tabla.getTableHeader().setForeground(Color.WHITE);

        // Ajustar ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(100);  // Cédula
        tabla.getColumnModel().getColumn(1).setPreferredWidth(160);  // Nombres
        tabla.getColumnModel().getColumn(2).setPreferredWidth(160);  // Apellidos
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200);  // Correo

        JScrollPane scrollTabla = new JScrollPane(tabla);
        //colores
        scrollTabla.setBackground(fondoCrema);
        scrollTabla.getViewport().setBackground(Color.WHITE);

        scrollTabla.setBorder(
                BorderFactory.createTitledBorder("Lista de Estudiantes (ordenada por apellido)"));

        add(scrollTabla, BorderLayout.CENTER);
    }

    // =========================================================================
    //  EVENTOS
    // =========================================================================

    private void configurarEventos() {
        btnRegistrar.addActionListener(e -> registrar());
        btnEditar.addActionListener(e -> editar());
        btnLimpiar.addActionListener(e -> limpiar());

        // Al seleccionar una fila → cargar datos en el formulario (modo edición)
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    cargarEstudianteEnFormulario(fila);
                }
            }
        });
    }

    // =========================================================================
    //  ACCIONES
    // =========================================================================

    /**
     * Registra un nuevo estudiante usando el servicio.
     */
    private void registrar() {
        String error = servicio.registrar(
                txtCedula.getText(),
                txtNombres.getText(),
                txtApellidos.getText(),
                txtCorreo.getText()
        );

        if (error != null) {
            JOptionPane.showMessageDialog(this, error,
                    "Error al registrar", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Estudiante registrado exitosamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
            actualizarTabla();
        }
    }

    /**
     * Edita el estudiante seleccionado (sin modificar la cédula).
     */
    private void editar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un estudiante de la tabla para editar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cedula = (String) modeloTabla.getValueAt(fila, 0);
        String error = servicio.editar(
                cedula,
                txtNombres.getText(),
                txtApellidos.getText(),
                txtCorreo.getText()
        );

        if (error != null) {
            JOptionPane.showMessageDialog(this, error,
                    "Error al editar", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Estudiante actualizado exitosamente.",
                    "Edición exitosa", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
            actualizarTabla();
        }
    }

    /**
     * Limpia el formulario y vuelve al modo de registro.
     */
    private void limpiar() {
        txtCedula.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCorreo.setText("");

        // Restaurar modo registro
        txtCedula.setEditable(true);
        txtCedula.setBackground(Color.WHITE);
        btnRegistrar.setEnabled(true);
        btnEditar.setEnabled(false);
        modoEdicion = false;

        tabla.clearSelection();
        txtCedula.requestFocus();
    }

    /**
     * Carga los datos de la fila seleccionada en el formulario
     * y activa el modo edición (cédula no editable).
     */
    private void cargarEstudianteEnFormulario(int fila) {
        txtCedula.setText((String) modeloTabla.getValueAt(fila, 0));
        txtNombres.setText((String) modeloTabla.getValueAt(fila, 1));
        txtApellidos.setText((String) modeloTabla.getValueAt(fila, 2));
        txtCorreo.setText((String) modeloTabla.getValueAt(fila, 3));

        // Bloquear cédula y cambiar a modo edición
        txtCedula.setEditable(false);
        txtCedula.setBackground(new Color(230, 230, 230));
        btnRegistrar.setEnabled(false);
        btnEditar.setEnabled(true);
        modoEdicion = true;
    }

    /**
     * Recarga los datos de la tabla desde el servicio.
     */
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Estudiante> lista = servicio.listar();
        for (int i = 0; i < lista.size(); i++) {
            Estudiante e = lista.get(i);
            modeloTabla.addRow(new Object[]{
                    e.getCedula(),
                    e.getNombres(),
                    e.getApellidos(),
                    e.getCorreo()
            });
        }
    }

    // =========================================================================
    //  MAIN PARA PRUEBAS INDEPENDIENTES
    // =========================================================================

    /**
     * Permite probar este panel de forma independiente.
     * Al integrar con el menú principal, este main ya no se usará.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("TutorAlerta - Registro de Estudiantes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new PanelEstudiantes());
            frame.setSize(750, 520);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
