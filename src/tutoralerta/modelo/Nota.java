package tutoralerta.modelo;

public class Nota {
    private final String id_evaluacion;
    private final String cedula;
    private double calificacion;

    public Nota (String id_evaluacion, String cedula, double calificacion){
        this.id_evaluacion = id_evaluacion;
        this.cedula = cedula;
        this.calificacion = calificacion;
    }

    public String getIdEvaluacion(){
        return id_evaluacion;
    }
    public String getCedula(){
        return cedula;
    }
    public double getCalificacion(){
        return calificacion;
    }
    
    public void setCalificacion(double calificacion){
        this.calificacion = calificacion;
    }
    
    @Override
    public String toString() {
        return "Evaluación: " + id_evaluacion + " | Estudiante: " + cedula + " | Nota: " + calificacion;
    }
}
