package tutoralerta.modelo;

import java.time.LocalDate;

public class Sesion {
    private final LocalDate fecha;
    private final String cedula;
    private String estado;

    public Sesion(LocalDate fecha, String cedula, String estado){
        this.fecha = fecha;
        this.cedula = cedula;
        this.estado = estado;
    }

    public LocalDate getFecha(){
        return fecha;
    }
    public String getCedula(){
        return cedula;
    }
    public String getEstado(){
        return estado;
    }
    public void setEstado(String estado){
        this.estado = estado;
    }
        
    @Override
    public String toString() {
        return fecha + " - " + cedula + ": " + estado;
    }
}
