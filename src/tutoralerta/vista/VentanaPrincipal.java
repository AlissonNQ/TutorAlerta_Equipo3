package tutoralerta.vista;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class VentanaPrincipal extends JFrame {

    private JTabbedPane pestanas;
    private PanelEstudiantes panelEstudiantes;

    public VentanaPrincipal() {
        setTitle("TutorAlerta - Sistema de Alerta Temprana de Riesgo Académico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        pestanas = new JTabbedPane();
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Módulo 1: Estudiantes (HU-B1 - Listo)
        panelEstudiantes = new PanelEstudiantes();
        pestanas.addTab("1. Estudiantes", panelEstudiantes);

        // Módulo 2: Asistencia (HU-B2 - Pendiente de integración)
        pestanas.addTab("2. Asistencia", crearPanelPendiente("Módulo de Toma de Asistencia (HU-B2) - Pendiente de integración"));

        // Módulo 3: Notas (HU-B3 - Pendiente de integración)
        pestanas.addTab("3. Notas", crearPanelPendiente("Módulo de Registro de Notas (HU-B3) - Pendiente de integración"));

        add(pestanas, BorderLayout.CENTER);
    }

    private JPanel crearPanelPendiente(String mensaje) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel etiqueta = new JLabel(mensaje, SwingConstants.CENTER);
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(etiqueta, BorderLayout.CENTER);
        return panel;
    }
}
