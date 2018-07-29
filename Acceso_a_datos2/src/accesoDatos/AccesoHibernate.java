package accesoDatos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.Session;

import auxiliares.HibernateUtil;
import entidades.Alumno;
import entidades.Titulacion;
import interfazUsuario.VistaPrincipal;

public class AccesoHibernate implements I_Acceso_Datos {

	Session session;
	HashMap<String, Alumno> recogerAlumnos;
	HashMap<String, Titulacion> recogerTitulaciones;

	public AccesoHibernate() {

		recogerAlumnos = new HashMap<String, Alumno>();
		recogerTitulaciones = new HashMap<String, Titulacion>();

		HibernateUtil util = new HibernateUtil();

		session = util.getSessionFactory().openSession();

	}

	@Override
	public HashMap<String, Alumno> obtenerAlumno() {
		//recogerAlumnos = new HashMap<String, Alumno>();
		Query q = session.createQuery("select alum from Alumno alum");
		List results = q.list();

		Iterator alumnosIterator = results.iterator();

		while (alumnosIterator.hasNext()) {
			Alumno alumno = (Alumno) alumnosIterator.next();
			recogerAlumnos.put(alumno.getDni(), alumno);
		}

		return recogerAlumnos;
	}

	@Override
	public boolean insertarAlumno(Alumno alumno) {
		boolean todoOK = true;
		try {
			if (recogerAlumnos.get(alumno.getDni()) == null) {
				session.beginTransaction();
				session.save(alumno);
				session.getTransaction().commit();
				recogerAlumnos.put(alumno.getDni(), alumno);
			} else {
				todoOK = false;
			}
		} catch (Exception e) {
			todoOK = false;
		}

		return todoOK;
	}

	@Override
	public boolean borrarAlumno(String dni) {
		boolean todoOK = true;
		try {
			session.beginTransaction();
			session.delete(recogerAlumnos.get(dni));
			session.getTransaction().commit();
			recogerAlumnos.remove(dni);
		} catch (Exception e) {
			todoOK = false;
		}

		return todoOK;
	}

	@Override
	public boolean borrarTodoAlumnos() {
		boolean todoOK = true;

		try {
			session.beginTransaction();

			for (String key : recogerAlumnos.keySet()) {
				session.delete(recogerAlumnos.get(key));
			}
			recogerAlumnos.clear();

			session.getTransaction().commit();
		} catch (Exception e) {
			todoOK = false;
		}
		return todoOK;
	}

	@Override
	public boolean actualizarAlumnos(Alumno alumno) {
		boolean todoOK = true;
		try {
			session.beginTransaction();
			session.merge(alumno);
			session.getTransaction().commit();
			recogerAlumnos.put(alumno.getDni(), alumno);
		} catch (Exception e) {
			todoOK = false;
		}

		return todoOK;
	}

	@Override
	public boolean insertarTodosAlumnos(HashMap<String, Alumno> mapAlumno) {
		boolean todoOK = true;
		for (String key : mapAlumno.keySet()) {
			todoOK = this.insertarAlumno(mapAlumno.get(key));
		}
		return todoOK;
	}

	@Override
	public HashMap<String, Titulacion> obtenerTitulacion() {
		//recogerTitulaciones = new HashMap<String, Titulacion>();
		Query q = session.createQuery("select dep from Titulacion dep");
		List results = q.list();

		Iterator titulacionesIterator = results.iterator();

		while (titulacionesIterator.hasNext()) {
			Titulacion titulacion = (Titulacion) titulacionesIterator.next();
			recogerTitulaciones.put(titulacion.getNombre().toUpperCase(), titulacion);
		}

		return recogerTitulaciones;
	}

	@Override
	public boolean insertarTitulacion(Titulacion titulacion) {
		boolean todoOK = true;
		try {
			session.beginTransaction();
			session.save(titulacion);
			session.getTransaction().commit();
			recogerTitulaciones.put(titulacion.getNombre().toUpperCase(), titulacion);
		} catch (Exception e) {
			todoOK = false;
		}

		return todoOK;
	}

	@Override
	public boolean insertarTodosTitulaciones(HashMap<String, Titulacion> mapTitulacion) {
		boolean todoOK = true;
		for (String key : mapTitulacion.keySet()) {
			todoOK = this.insertarTitulacion(mapTitulacion.get(key));
		}
		return todoOK;
	}

}
