package tutoralerta.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GestorCSV {
    public static File obtenerArchivo(String nombreArchivo){
        File carpetaLocal = new File("datos");
        if(carpetaLocal.exists() && carpetaLocal.isDirectory()){
            return new File(carpetaLocal, nombreArchivo);
        } else {
            File carpetaDist = new File("dist" + File.separator + "datos");
            if (!carpetaDist.exists()){
                carpetaDist.mkdirs();
            }
            return new File(carpetaDist, nombreArchivo);
        }
    }

    public static List<String> leerLineas(String nombreArchivo){
        List<String> lineas = new ArrayList<>();
        File archivo = obtenerArchivo(nombreArchivo);
        if(!archivo.exists()){
            return lineas;
        }
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))){
            String linea;
            boolean esEncabezado = true;
            while((linea = br.readLine()) != null){
                if (esEncabezado){
                    esEncabezado = false;
                } else {
                    if(!linea.trim().isEmpty()){
                        lineas.add(linea);
                    }
                }
            }
        } catch (IOException e){
            System.err.println("Error al leer " + nombreArchivo + ": " + e.getMessage());
        }
        return lineas;
    }

    public static void escribirLineas(String nombreArchivo, String encabezado, List<String> filas){
        File archivo = obtenerArchivo(nombreArchivo);
        try (BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8)
        )){
            bw.write(encabezado);
            bw.newLine();
            for (int i=0; i<filas.size(); i++){
                bw.write(filas.get(i));
                bw.newLine();
            }
        }catch (IOException e){
            System.err.println("Error al escribir " + nombreArchivo + ": " + e.getMessage());
        }
    }

}
