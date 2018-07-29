package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.table.DefaultTableModel;

import accesoDatos.I_Acceso_Datos;
import entidades.Alumno;
import entidades.Titulacion;
import interfazUsuario.VistaPrincipal;

public class Controlador {

	Scanner teclado;
	SeleccionAccesoDatos selector;
	I_Acceso_Datos accesoDatos;
	I_Acceso_Datos accesoExportar;
	VistaPrincipal vistaP;

	HashMap<String, Alumno> recogerAlumnos;
	HashMap<String, Titulacion> recogerTitulaciones;
	ArrayList<String> cbTitulacion;

	String mensaje;

	public Controlador(Scanner miScanner) {
		teclado = miScanner;
		selector = new SeleccionAccesoDatos(miScanner);
		// Seleccionamos el acceso a datos
		accesoDatos = (I_Acceso_Datos) selector.elegirClase("accesoDatos");

		if (accesoDatos != null) {
			recogerAlumnos = accesoDatos.obtenerAlumno();
			recogerTitulaciones = accesoDatos.obtenerTitulacion();
			
			System.out.println(recogerAlumnos.size() + "  " + recogerTitulaciones.size());
			if ((recogerAlumnos != null) && (recogerTitulaciones != null)) {

				this.setVisibleInterfazGrafica();
				cargarNombreTitu();
				cargarNombreExpo();
			}

		}

	}

	private void setVisibleInterfazGrafica() {
		try {
			vistaP = new VistaPrincipal();
			this.crearTabla();
			vistaP.setControlador(this);
			vistaP.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void crearTabla() {
		recogerAlumnos = accesoDatos.obtenerAlumno();
		recogerTitulaciones = accesoDatos.obtenerTitulacion();
		if (!recogerAlumnos.isEmpty() || !recogerTitulaciones.isEmpty()) {
			vistaP.crearTabla(recogerAlumnos);
		} else {
			System.out.println("vacio");
		}
	}

	private void actualizarTabla() {
		recogerAlumnos = accesoDatos.obtenerAlumno();
		vistaP.actualizarTabla(recogerAlumnos);
	}

	public void nuevoAlumno() {
		Alumno alumno;
		alumno = new Alumno(vistaP.getTxtDni(), vistaP.getTxtNombre(), vistaP.getTxtApellido(),
				Integer.parseInt(vistaP.getTxtTelefono()), vistaP.getTxtNacionalidad(),
				recogerTitulaciones.get(vistaP.getTxtTitulacion()));

		accesoDatos.insertarAlumno(alumno);
		actualizarTabla();
	}

	public void actualizarAlumno() {
		Alumno alumno;
		String dato = String.valueOf(vistaP.getTabla().getValueAt(vistaP.getTabla().getSelectedRow(), 1));
		alumno = new Alumno(dato, vistaP.getTxtNombreMod(), vistaP.getTxtApellidoMod(),
				Integer.parseInt(vistaP.getTxtTelefonoMod()), vistaP.getTxtNacionalidadMod(),
				recogerTitulaciones.get(vistaP.getTxtTitulacionMod()));
		accesoDatos.actualizarAlumnos(alumno);
		actualizarTabla();
	}

	public void eliminarUno() {
		String dni = (String) vistaP.getTabla().getValueAt(vistaP.getTabla().getSelectedRow(), 1);
		accesoDatos.borrarAlumno(dni);
		actualizarTabla();
	}

	public void eliminarTodos() {
		accesoDatos.borrarTodoAlumnos();
		actualizarTabla();
	}

	public void nuevoCurso() {
		Titulacion titulacion;
		titulacion = new Titulacion(vistaP.getTxtNombreCurso(), vistaP.getTxtDescripcionCurso());

		accesoDatos.insertarTitulacion(titulacion);
		cargarNombreTitu();

	}

	public void cargarNombreTitu() {
		int aux = recogerTitulaciones.size();
		cbTitulacion = new ArrayList<String>();
		for (String key : recogerTitulaciones.keySet()) {
			if (aux > 0) {
				cbTitulacion.add(recogerTitulaciones.get(key).getNombre());
				aux++;
			}

		}
		vistaP.setComboBoxTitu(cbTitulacion);
	}

	public void cargarNombreExpo() {
		String[] arrClass = new String[selector.arrClases.size()];
		for (int i = 0; i < arrClass.length; i++) {
			arrClass[i] = selector.arrClases.get(i);
		}
		vistaP.setCbExportar(arrClass);
	}
	
	public void claseExportar(){
		accesoExportar = (I_Acceso_Datos) selector.crearInstanciaClase(vistaP.getCbExportar(), "accesoDatos");
		if (accesoExportar!=null) {
			System.out.println("claseExportar: " + vistaP.getCbExportar());
			//accesoDatos.insertarTodosTitulaciones(recogerTitulaciones);
			System.out.println(accesoExportar.insertarTodosTitulaciones(recogerTitulaciones));
			//accesoDatos.insertarTodosAlumnos(recogerAlumnos);
			System.out.println(accesoExportar.insertarTodosAlumnos(recogerAlumnos));
			
		}
	}
}