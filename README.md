# TutorAlerta - Equipo 3 (Escenario B)

Aplicación de escritorio en Java Swing con persistencia en archivos CSV para la alerta temprana de riesgo académico, diseñada para que el tutor registre estudiantes, tome asistencia y cargue calificaciones de su curso.

## Integrantes y Roles (Sprint 1)
- **Doménica** – Product Owner y Desarrolladora
- **Andrés** – Scrum Master y Desarrollador
- **David** – Desarrollador
- **Alisson** – Desarrolladora 

## Tablero de Seguimiento (Scrum)
- **Trello:** https://trello.com/invite/b/6affbb680328e514d0a9099e/ATTI3e8dde98c423dac40c7a50901cbd9c32E65DD0E5/tutoralerta-equipo-3

## Estructura del Proyecto
- `src/tutoralerta/main/` – Punto de entrada de la aplicación (`Main.java`).
- `src/tutoralerta/modelo/` – Entidades del dominio (`Estudiante`, `Sesion`, `Evaluacion`, `Nota`).
- `src/tutoralerta/persistencia/` – Lectura y escritura de datos en archivos CSV.
- `src/tutoralerta/servicio/` – Reglas de negocio y validaciones.
- `src/tutoralerta/vista/` – Interfaz gráfica de usuario en Java Swing (`VentanaPrincipal`, paneles por historia).
- `dist/` – Ejecutable `TutorAlerta.jar` y carpeta `datos/` con archivos CSV de prueba.
  
## Cómo ejecutar la aplicación

### Desde la raíz del proyecto (`TutorAlerta_Equipo3`)
```bash
java -jar dist/TutorAlerta.jar
