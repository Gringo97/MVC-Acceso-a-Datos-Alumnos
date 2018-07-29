package accesoDatos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import entidades.Alumno;
import entidades.Titulacion;

public class FicheroTexto implements I_Acceso_Datos {

	HashMap<String, Alumno> recogerAlumnos;
	File fAlumnos;
	int nAlumnos;
	HashMap<String, Titulacion> recogerTitulaciones;
	File fTitulaciones;
	int nTitulaciones;

	@Override
	public HashMap<String, Alumno> obtenerAlumno() {
		recogerAlumnos = new HashMap<String, Alumno>();
		fAlumnos = new File("Ficheros/datos/alumnos.txt");
		obtenerTitulacion();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(fAlumnos));
			String text = null;
			String clave;
			Alumno alumno = null;

			while ((text = reader.readLine()) != null) {

				String[] datosaux = text.split(",");
				clave = datosaux[1];
				alumno = new Alumno(Integer.parseInt(datosaux[0]), clave, datosaux[2], datosaux[3],
						Integer.parseInt(datosaux[4]), datosaux[5], comprobarTitulacion(Integer.parseInt(datosaux[6])));
				recogerAlumnos.put(clave, alumno);
				nAlumnos = Integer.parseInt(datosaux[0]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recogerAlumnos;
	}

	@Override
	public boolean insertarAlumno(Alumno alumno) {
		boolean todoOK = true;

		FileWriter fichero = null;
		PrintWriter pw = null;

		try {
			if (recogerAlumnos.get(alumno.getDni()) == null) {
				recogerAlumnos.put(alumno.getDni(), alumno);
				fichero = new FileWriter("Ficheros/datos/alumnos.txt", true);
				pw = new PrintWriter(fichero);
				nAlumnos++;
				pw.println((nAlumnos) + "," + alumno.getDni().toLowerCase() + "," + alumno.getNombre().toLowerCase()
						+ "," + alumno.getApellido().toLowerCase() + "," + alumno.getTelefono() + ","
						+ alumno.getNacionalidad().toLowerCase() + "," + alumno.getTitulacionAlumno().getCod());

				System.out.println("nAlumnos" + nAlumnos);
			}else{
				todoOK = false;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			todoOK = false;
		}

		try {
			if (fichero != null) {
				pw.close();
				fichero.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			todoOK = false;
		}
		return todoOK;
	}

	@Override
	public boolean insertarTodosAlumnos(HashMap<String, Alumno> mapAlumno) {
		boolean todoOK = true;
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("Ficheros/datos/alumnos.txt");
			pw = new PrintWriter(fichero);
			pw.print("");
			for (String key : mapAlumno.keySet()) {
				pw.println(mapAlumno.get(key).getCod() + "," + mapAlumno.get(key).getDni().toLowerCase() + "," + mapAlumno.get(key).getNombre().toLowerCase()
						+ "," + mapAlumno.get(key).getApellido().toLowerCase() + "," + mapAlumno.get(key).getTelefono() + ","
						+ mapAlumno.get(key).getNacionalidad().toLowerCase() + "," + mapAlumno.get(key).getTitulacionAlumno().getCod());

				
			}
			
			pw.close();
			fichero.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
//		for (String key : mapAlumno.keySet()) {
//			todoOK = this.insertarAlumno(mapAlumno.get(key));
//		}
		return todoOK;
	}

	@Override
	public boolean actualizarAlumnos(Alumno alumno) {
		boolean todoOK = true;
		
		try {
			System.out.println(alumno.getDni());
			recogerAlumnos.put(alumno.getDni(), alumno);
			todoOK = insertarTodosAlumnos(recogerAlumnos);
		} catch (Exception e) {
			
			todoOK = false;
		}
		System.out.println(todoOK);
		return todoOK;
	}

	@Override
	public boolean borrarAlumno(String dni) {
		boolean todoOK = true;
		try {
			if (recogerAlumnos.remove(dni)!=null) {
				insertarTodosAlumnos(recogerAlumnos);
			}else{
				todoOK = false;
			}
			
			
		} catch (Exception e) {
			todoOK = false;
		}

		return todoOK;
	}

	@Override
	public boolean borrarTodoAlumnos() {
		boolean todoOK = true;
		PrintWriter writer;
		try {
			writer = new PrintWriter(fAlumnos);
			writer.print("");
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			todoOK = false;
		}
		return todoOK;
	}

	@Override
	public HashMap<String, Titulacion> obtenerTitulacion() {
		boolean todoOK = true;
		recogerTitulaciones = new HashMap<String, Titulacion>();
		fTitulaciones = new File("Ficheros/datos/titulaciones.txt");
		BufferedReader reader = null;
		Integer clave;

		try {
			reader = new BufferedReader(new FileReader(fTitulaciones));
			String text = null;
			Titulacion titulacion = null;

			while ((text = reader.readLine()) != null) {

				String[] datosaux = text.split(",");
				clave = Integer.parseInt(datosaux[0]);
				titulacion = new Titulacion(clave, datosaux[1], datosaux[2]);
				recogerTitulaciones.put(datosaux[1], titulacion);

			}
			nTitulaciones = recogerTitulaciones.size();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			todoOK = false;
		} catch (IOException e) {
			e.printStackTrace();
			todoOK = false;
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			todoOK = false;
		}

		return recogerTitulaciones;
	}

	@Override
	public boolean insertarTitulacion(Titulacion titulacion) {
		boolean todoOK = true;
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("Ficheros/datos/Titulaciones.txt", true);
			pw = new PrintWriter(fichero);
			nTitulaciones++;
			pw.println(nTitulaciones + "," + titulacion.getNombre().toUpperCase() + ","
					+ titulacion.getDescripcion().toLowerCase());

			recogerTitulaciones.put(titulacion.getNombre().toUpperCase(), titulacion);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			todoOK = false;
		}

		try {
			if (fichero != null) {
				pw.close();
				fichero.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			todoOK = false;
		}
		return todoOK;
	}

	private Titulacion comprobarTitulacion(int titu) {
		Titulacion titulacion = new Titulacion();
		for (String key : recogerTitulaciones.keySet()) {
			Titulacion value = recogerTitulaciones.get(key);
			if (titu == value.getCod()) {
				titulacion = value;
			}
		}
		return titulacion;
	}

	@Override
	public boolean insertarTodosTitulaciones(HashMap<String, Titulacion> mapTitulacion) {
		boolean todoOK = true;
		FileWriter fichero = null;
		PrintWriter pw = null;
		Titulacion titulacion = null;
		try {
			fichero = new FileWriter("Ficheros/datos/Titulaciones.txt");
			pw = new PrintWriter(fichero);
			pw.print("");
			for (String key : mapTitulacion.keySet()) { 
			pw.println(mapTitulacion.get(key).getCod() + "," + mapTitulacion.get(key).getNombre().toUpperCase() + ","
					+ mapTitulacion.get(key).getDescripcion().toLowerCase());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			todoOK = false;
		}

		try {
			if (fichero != null) {
				pw.close();
				fichero.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			todoOK = false;
		}
		return todoOK;
	}

}
