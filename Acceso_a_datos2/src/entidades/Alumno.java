package entidades;

public class Alumno {
	private int cod;
	private String dni;
	private String nombre;
	private String apellido;
	private int telefono;
	private String nacionalidad;
	private Titulacion titulacionAlumno;
	
	
	public Alumno(){}

	public Alumno(int cod, String dni, String nombre, String apellido, int telefono, String nacionalidad,Titulacion titulacionAlumno) {

		this.cod = cod;
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.nacionalidad = nacionalidad;
		this.titulacionAlumno = titulacionAlumno;
	}

	public Alumno(String dni, String nombre, String apellido, int telefono, String nacionalidad, Titulacion titulacionAlumno) {

		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.nacionalidad = nacionalidad;
		this.titulacionAlumno = titulacionAlumno;
	}


	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	
	public Titulacion getTitulacionAlumno() {
		return titulacionAlumno;
	}

	public void setTitulacionAlumno(Titulacion titulacion) {
		this.titulacionAlumno = titulacion;
	}

}