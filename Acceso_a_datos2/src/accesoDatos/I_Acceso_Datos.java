package accesoDatos;

import java.util.HashMap;

import entidades.Alumno;
import entidades.Titulacion;


public interface I_Acceso_Datos {
	
	public HashMap<String, Alumno> obtenerAlumno();
	public boolean insertarAlumno(Alumno alumno);
	public boolean borrarAlumno(String dni);
	public boolean borrarTodoAlumnos();
	public boolean actualizarAlumnos(Alumno alumno);
	public boolean insertarTodosAlumnos(HashMap<String, Alumno> mapAlumno);
	
	public HashMap<String, Titulacion> obtenerTitulacion();
	public boolean insertarTitulacion(Titulacion titulacion);
	public boolean insertarTodosTitulaciones( HashMap<String, Titulacion> mapTitulacion); 
}
