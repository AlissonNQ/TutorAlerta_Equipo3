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
import javax.swing.SwingConstants;


public class VentanaPrincipal extends JFrame {

    private JTabbedPane pestanas;
    private PanelEstudiantes panelEstudiantes;

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

        // Encabezado institucional en tonos pasteles con logo a la izquierda
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