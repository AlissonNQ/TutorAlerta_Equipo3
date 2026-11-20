# TutorAlerta - Equipo 3 (Escenario B)

Aplicación de escritorio en Java para la alerta temprana de riesgo académico, diseñada para que el tutor registre la asistencia y las notas de su curso y detecte a tiempo qué estudiantes requieren tutoría.

## Integrantes y Roles (Sprint 1)
* **Doménica** – Product Owner & Desarrolladora (`HU-B1`: Registrar estudiantes)
* **Andrés** – Scrum Master & Desarrollador (`HU-B2`: Tomar asistencia)
* **David** – Desarrollador (`HU-B3`: Registrar notas)
* **Alisson** – Arquitectura, Persistencia (CSV) & QA Lead

## Tablero de Seguimiento (Scrum)
* **Trello:** `<PEGAR_LINK_DE_TRELLO_AQUI>`

## Estructura del Proyecto
* `src/tutoralerta/principal/` – Punto de entrada de la aplicación.
* `src/tutoralerta/modelo/` – Entidades del dominio (`Estudiante`, `Sesion`, `Evaluacion`, `Nota`).
* `src/tutoralerta/persistencia/` – Persistencia y lectura/escritura en archivos CSV.
* `src/tutoralerta/servicio/` – Lógica de negocio y validaciones.
* `src/tutoralerta/vista/` – Interfaz gráfica de usuario (Java Swing/JavaFX).
* `dist/` – Archivo ejecutable `TutorAlerta.jar` y carpeta `datos/` con archivos CSV.

## Cómo ejecutar la aplicación

### Desde el ejecutable (.jar)
1. Asegúrate de estar en la carpeta raíz del proyecto o dentro de `dist/`.
2. Ejecuta en la terminal:
   ```bash
   java -jar dist/TutorAlerta.jar