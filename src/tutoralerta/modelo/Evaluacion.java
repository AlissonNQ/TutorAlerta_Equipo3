package tutoralerta.modelo;

import java.time.LocalDate;

public class Evaluacion {
    private final String id_evaluacion;
    private String nombre;
    private LocalDate fecha;
    
    public Evaluacion(String id_evaluacion, String nombre, LocalDate fecha){
        this.id_evaluacion = id_evaluacion;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public String getIdEvaluacion(){
        return id_evaluacion;
    }
    public String getNombre(){
        return nombre;
    }
    public LocalDate getFecha(){
        return fecha;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setFecha(LocalDate fecha){
        this.fecha = fecha;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + fecha + ")";
    }
}
