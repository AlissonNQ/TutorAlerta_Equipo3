package tutoralerta.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class VentanaPrincipal extends JFrame {

    private JTabbedPane pestanas;
    private PanelEstudiantes panelEstudiantes;
    private PanelAsistencia panelAsistencia;
    private PanelNotas panelNotas;

    public VentanaPrincipal() {

        Color fondoBannerPastel = new Color(218, 232, 248);
        Color bordeSuave = new Color(188, 202, 232);
        Color textoPrincipal = new Color(45, 52, 64);
        Color textoSecundario = new Color(85, 96, 112);

        setTitle("TutorAlerta - Sistema de Alerta Temprana de Riesgo Académico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Encabezado
        JPanel encabezado = new JPanel(new BorderLayout(12, 0));
        encabezado.setBackground(fondoBannerPastel);
        encabezado.setPreferredSize(new Dimension(0, 64));
        encabezado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, bordeSuave),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        ImageIcon iconoLogo = cargarLogoEscalonado("dist/logo_epn.png", 38, 38);
        if (iconoLogo != null) {
            JLabel lblLogo = new JLabel(iconoLogo);
            encabezado.add(lblLogo, BorderLayout.WEST);
        }

        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setBackground(fondoBannerPastel);

        JLabel lblUniversidad = new JLabel("Escuela Politécnica Nacional");
        lblUniversidad.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblUniversidad.setForeground(textoPrincipal);

        JLabel lblSubtitulo = new JLabel("TutorAlerta · Sistema de Alerta Temprana de Riesgo Académico · Equipo 3");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitulo.setForeground(textoSecundario);

        panelTextos.add(lblUniversidad);
        panelTextos.add(Box.createVerticalStrut(2));
        panelTextos.add(lblSubtitulo);

        encabezado.add(panelTextos, BorderLayout.CENTER);
        add(encabezado, BorderLayout.NORTH);

        pestanas = new JTabbedPane();
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Módulo 1: Estudiantes
        panelEstudiantes = new PanelEstudiantes();
        pestanas.addTab("1. Estudiantes", panelEstudiantes);

        // Módulo 2: Asistencia
        panelAsistencia = new PanelAsistencia();
        pestanas.addTab("2. Asistencia", panelAsistencia);

        // Módulo 3: Notas
        panelNotas = new PanelNotas();
        pestanas.addTab("3. Notas", panelNotas);

        // Sincronizar estudiantes y evaluaciones al entrar a la pestaña de Notas
        pestanas.addChangeListener(e -> {
            if (pestanas.getSelectedComponent() == panelNotas) {
                panelNotas.recargarDatos();
            }
        });

        add(pestanas, BorderLayout.CENTER);
    }

    private ImageIcon cargarLogoEscalonado(String rutaArchivo, int ancho, int alto) {
        try {
            File archivo = new File(rutaArchivo);
            if (!archivo.exists()) {
                return null;
            }
            BufferedImage imagenOriginal = ImageIO.read(archivo);
            if (imagenOriginal == null) {
                return null;
            }
            Image imagenEscalada = imagenOriginal.getScaledInstance(
                    ancho, alto, Image.SCALE_SMOOTH
            );
            return new ImageIcon(imagenEscalada);
        } catch (Exception e) {
            return null;
        }
    }
}