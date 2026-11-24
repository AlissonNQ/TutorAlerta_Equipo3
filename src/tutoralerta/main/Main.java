package tutoralerta.main;

import javax.swing.SwingUtilities;
import tutoralerta.vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
            }
        });
    }
}