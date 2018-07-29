package entidades;

import java.util.ArrayList;

import org.bson.Document;

public class Titulacion {
	private int cod;
	private String nombre;
	private String descripcion;
	private ArrayList<Document> arrayAlumnos;

	public Titulacion(){}
	
	public Titulacion(int cod, String nombre,String descripcion) {
		super();
		this.cod = cod;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	public Titulacion(String nombre,String descripcion,ArrayList<Document> arrayAlumnos) {
		super();
		
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.arrayAlumnos = arrayAlumnos;
	}

	public Titulacion( String nombre,String descripcion) {
		super();
		
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ArrayList<Document> getArrayAlumnos() {
		return arrayAlumnos;
	}

	

}

