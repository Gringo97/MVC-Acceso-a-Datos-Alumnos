package accesoDatos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;

import entidades.Alumno;
import entidades.Titulacion;


public class AccesoMongo implements I_Acceso_Datos {

	MongoClient mongoClient;
	MongoCollection<Document> collection;
	MongoDatabase db;
	HashMap<String, Alumno> recogerAlumnos;
	HashMap<String, Titulacion> recogerTitulaciones;
	int mayorCodAlumno;
	int mayorCodTitu;

	public AccesoMongo() {
		try {
			// PASO 1: Conexión al Server de MongoDB Pasandole el host y el
			// puerto
			mongoClient = new MongoClient("localhost", 27017);

			// PASO 2: Conexión a la base de datos
			db = mongoClient.getDatabase("adat_alumnos");
			System.out.println("Conectado a BD MONGO");
			recogerAlumnos = new HashMap<String, Alumno>();
			recogerTitulaciones = new HashMap<String, Titulacion>();

		} catch (Exception e) {
			System.out.println("Error leyendo la BD MONGO: " + e.getMessage());
			System.out.println("Fin de la ejecucion del programa");
			System.exit(1);
		}

	}

	@Override
	public HashMap<String, Alumno> obtenerAlumno() {
		obtenerTitulacion();

		Alumno nuevoAlumno;
		int cod;
		String dni;
		String nombre;
		String apellido;
		int telefono;
		String nacionalidad;
		Titulacion titulacionAlumno;

		try {

			// // PASO 3: Obtenemos una coleccion para trabajar con ella
			// collection = db.getCollection("titulaciones");
			//
			// // PASO 4.2.1: "READ" -> Leemos todos los documentos de la base
			// de
			// // datos
			// int numDocumentos = (int) collection.count();
			// System.out.println("Número de documentos (registros) en la
			// colección alumnos: " + numDocumentos + "\n");
			//
			// BsonDocument documentBson;
			// Document alumnoDocument = null;

			for (Entry<String, Titulacion> entry : recogerTitulaciones.entrySet()) {
				String key = entry.getKey();
				Titulacion titulacion = entry.getValue();
				ArrayList<Document> arrAlumno = titulacion.getArrayAlumnos();
				for (int i = 0; i < arrAlumno.size(); i++) {
					cod = arrAlumno.get(i).getInteger("cod");
					if (cod >= mayorCodAlumno) {
						mayorCodAlumno = cod;
					}
					nombre = arrAlumno.get(i).getString("nombre");
					dni = arrAlumno.get(i).getString("dni");
					apellido = arrAlumno.get(i).getString("apellido");
					telefono = arrAlumno.get(i).getInteger("telefono");
					nacionalidad = arrAlumno.get(i).getString("nacionalidad");
					titulacionAlumno = recogerTitulaciones.get(arrAlumno.get(i).getString("titulacionAlumno"));
					nuevoAlumno = new Alumno(cod, dni, nombre, apellido, telefono, nacionalidad, titulacionAlumno);
					recogerAlumnos.put(dni, nuevoAlumno);

				}

				// do what you have to do here
				// In your case, another loop.
			}

		} catch (Exception ex) {
			System.out.println("Error leyendo la coleccion: no se ha podido acceder a los datos");
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			System.out.println("Fin de la ejecucion del programa");
			System.exit(1);
		}

		System.out.println("Leidos datos de la coleccion de Depositos");
		return recogerAlumnos;
	}

	@Override
	public boolean insertarAlumno(Alumno alumno) {
		boolean todoOK = true;

		try {

			collection = db.getCollection("titulaciones");

			// Para que salga ordenado el hashmap de monedas (de stackoverflow)
			Document updatedDocument = collection.findOneAndUpdate(
					Filters.eq("titulacion", alumno.getTitulacionAlumno().getNombre()),
					new Document("$push", alumnoToDocument(alumno, "insert")),
					new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));

			recogerAlumnos.put(alumno.getDni(), alumno);

		} catch (Exception e) {
			todoOK = false;
			System.out.println("Opcion guardar datos de Depositos no disponible por el momento");
			e.printStackTrace();
		}

		return todoOK;

	}

	@Override
	public boolean borrarAlumno(String dni) {
		
		boolean todoOK = true;

		try {

			collection = db.getCollection("titulaciones");
			String titulacion = recogerAlumnos.get(dni).getTitulacionAlumno().getNombre();

			
			Document filtro = new Document();
			filtro.put("titulacion", titulacion);
			filtro.put("alumnos.dni", dni);
			//System.out.println(filtro);
			
			Document deleteAl = new Document();
			deleteAl.put("dni", dni);
			Document delete = new Document();
			delete.put("alumnos", deleteAl);
			
			
			
			Document update = new Document();
			update.put("$pull", delete);
			
			
			//System.out.println(update);
			collection.updateOne(filtro,update);
			recogerAlumnos.remove(dni);

		} catch (Exception e) {
			todoOK = false;
			System.out.println("Opcion guardar datos de Depositos no disponible por el momento");
			e.printStackTrace();
		}

		return todoOK;
		
	}

	@Override
	public boolean borrarTodoAlumnos() {
		boolean todoOK = true;
		collection = db.getCollection("titulaciones");
		collection.drop();
		try {
			for (String key : recogerTitulaciones.keySet()) {
				collection.insertOne(titulacionToDocument(recogerTitulaciones.get(key)));
			}
			
			recogerAlumnos.clear();;

		} catch (Exception e) {
			todoOK = false;
			System.out.println("Opcion guardar datos de Depositos no disponible por el momento");
			e.printStackTrace();
		}
		
		return todoOK;
	}

	@Override
	public boolean actualizarAlumnos(Alumno alumno) {
		boolean todoOK = true;

		try {

			collection = db.getCollection("titulaciones");

			// Para que salga ordenado el hashmap de monedas (de stackoverflow)
			Document docAlumno = new Document();
			docAlumno.put("alumnos.$.dni", alumno.getDni());
			docAlumno.put("alumnos.$.nombre", alumno.getNombre());
			docAlumno.put("alumnos.$.apellido", alumno.getApellido());
			docAlumno.put("alumnos.$.telefono", alumno.getTelefono());
			docAlumno.put("alumnos.$.nacionalidad", alumno.getNacionalidad());
			docAlumno.put("alumnos.$.titulacionAlumno", alumno.getTitulacionAlumno().getNombre());
			Document filtro = new Document();
			filtro.put("titulacion", alumno.getTitulacionAlumno().getNombre());
			filtro.put("alumnos.dni", alumno.getDni());

			Document update = new Document();
			update.put("$set", docAlumno);
			System.out.println(update);
			collection.updateOne(filtro, update);

			recogerAlumnos.put(alumno.getDni(), alumno);

		} catch (Exception e) {
			todoOK = false;
			System.out.println("Opcion guardar datos de Depositos no disponible por el momento");
			e.printStackTrace();
		}

		return todoOK;
	}

	@Override
	public boolean insertarTodosAlumnos(HashMap<String, Alumno> mapAlumno) {
		Boolean todoOk = true;
		for (String dni : mapAlumno.keySet()) {
			todoOk = insertarAlumno(mapAlumno.get(dni));
			recogerAlumnos.put(dni, mapAlumno.get(dni));
		}
		
		return todoOk;
	}

	@Override
	public HashMap<String, Titulacion> obtenerTitulacion() {
		Titulacion nuevaTitulacion;

		String cod;
		String nombre;
		String descripcion;

		try {

			// PASO 3: Obtenemos una coleccion para trabajar con ella
			collection = db.getCollection("titulaciones");

			// PASO 4.2.1: "READ" -> Leemos todos los documentos de la base de
			// datos
			int numDocumentos = (int) collection.count();
			System.out
					.println("Número de documentos (registros) en la colección titulaciones: " + numDocumentos + "\n");

			// Busco todos los documentos de la colección, creo el objeto
			// deposito y lo almaceno en el hashmap
			MongoCursor<Document> cursor = collection.find().iterator();

			while (cursor.hasNext()) {

				Document rs = cursor.next();
				ArrayList<Document> arrAlumnos = (ArrayList<Document>) rs.get("alumnos");
				nombre = rs.getString("titulacion");

				descripcion = rs.getString("descripcion");

				nuevaTitulacion = new Titulacion(nombre, descripcion, arrAlumnos);
				// Una vez creado el deposito con valor de la moneda y cantidad
				// lo metemos en el hashmap
				recogerTitulaciones.put(nombre, nuevaTitulacion);

			}

		} catch (Exception ex) {
			System.out.println("Error leyendo la coleccion: no se ha podido acceder a los datos");
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			System.out.println("Fin de la ejecucion del programa");
			System.exit(1);
		}

		System.out.println("Leidos datos de la coleccion de Depositos");
		return recogerTitulaciones;
	}

	@Override
	public boolean insertarTitulacion(Titulacion titulacion) {
		boolean todoOK = true;

		try {

			collection = db.getCollection("titulaciones");

			// Para que salga ordenado el hashmap de monedas (de stackoverflow)
			collection.insertOne(titulacionToDocument(titulacion));
			recogerTitulaciones.put(titulacion.getNombre(), titulacion);

		} catch (Exception e) {
			todoOK = false;
			System.out.println("Opcion guardar datos de Depositos no disponible por el momento");
			e.printStackTrace();
		}

		return todoOK;
	}

	@Override
	public boolean insertarTodosTitulaciones(HashMap<String, Titulacion> mapTitulacion) {
		Boolean todoOk = true;
		collection = db.getCollection("titulaciones");
		collection.drop();
		for (String nombre : mapTitulacion.keySet()) {
			todoOk = insertarTitulacion(mapTitulacion.get(nombre));
			recogerTitulaciones.put(nombre, mapTitulacion.get(nombre));
		}
		
		return todoOk;
	}

	/*
	 * @alumnoToDocument CAMBIAR METODO pasar por parametro si es insertar o
	 * actualizar
	 */
	private Document alumnoToDocument(Alumno auxAlum, String tipo) {
		// Creamos una instancia Documento
		Document dbObjectAlumno = new Document();
		String titulacion = auxAlum.getTitulacionAlumno().getNombre();

		if (tipo.equals("insert")) {
			dbObjectAlumno.append("cod", ++mayorCodAlumno);
		} else {
			dbObjectAlumno.append("cod", auxAlum.getCod());
		}

		// documento titulacion para buscar donde insertar

		dbObjectAlumno.append("dni", auxAlum.getDni());
		dbObjectAlumno.append("nombre", auxAlum.getNombre());
		dbObjectAlumno.append("apellido", auxAlum.getApellido());
		dbObjectAlumno.append("telefono", auxAlum.getTelefono());
		dbObjectAlumno.append("nacionalidad", auxAlum.getNacionalidad());
		dbObjectAlumno.append("titulacionAlumno", titulacion);
		Document dbObjectTituInsertarAlumno = new Document();
		dbObjectTituInsertarAlumno.append("alumnos", dbObjectAlumno);
		return dbObjectTituInsertarAlumno;

		/*
		 * Document updatedDocument = collection.findOneAndUpdate(
		 * Filters.eq("idCurso", String.valueOf(alumno.getId_Curso())), new
		 * Document("$push", new Document("alumnos", new
		 * Document(dbObjectDeposito))), new
		 * FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
		 */
	}

	private Document titulacionToDocument(Titulacion auxTitu) {
		// Creamos una instancia Documento
		Document dbObjectTitulacion = new Document();
		ArrayList<Document> alumnosTitu = new ArrayList<>();
		dbObjectTitulacion.append("titulacion", auxTitu.getNombre());
		dbObjectTitulacion.append("descripcion", auxTitu.getDescripcion());
		dbObjectTitulacion.append("alumnos", alumnosTitu);

		return dbObjectTitulacion;
	}

}
