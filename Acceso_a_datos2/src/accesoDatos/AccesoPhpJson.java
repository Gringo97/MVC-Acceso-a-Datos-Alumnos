package accesoDatos;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import auxiliares.ApiRequests;
import entidades.Alumno;
import entidades.Titulacion;

public class AccesoPhpJson implements I_Acceso_Datos{
	
	ApiRequests encargadoPeticiones;
	private String SERVER_PATH, GET, SET_insert_ALUMNO,SET_insert_TITULACION,SET_delete_ALUMNO,SET_deleteAll_ALUMNO;
	HashMap<String, Alumno> recogerAlumnos;
	HashMap<String, Titulacion> recogerTitulaciones;
	
	public AccesoPhpJson(){
		recogerAlumnos = new HashMap<String, Alumno>();
		recogerTitulaciones = new HashMap<String, Titulacion>();
		
		encargadoPeticiones = new ApiRequests();
		

		SERVER_PATH = "http://localhost/adat_alumnos/";
	
		
	}

	@Override
	public HashMap<String, Alumno> obtenerAlumno() {
		
		obtenerTitulacion();
		//recogerAlumnos = new HashMap<String, Alumno>();
		GET = "leerAlumnos.php";
		
		try{
			
		    System.out.println("---------- Leemos datos de JSON --------------------");	
		    
			System.out.println("Lanzamos peticion JSON para alumnos");
			
			String url = SERVER_PATH + GET; // Sacadas de configuracion
			
			System.out.println("La url a la que lanzamos la peticion es " + url);
			
			String response = encargadoPeticiones.getRequest(url);
			
			System.out.println(response); // Traza para pruebas
			JSONObject respuesta = (JSONObject) JSONValue.parse(response);
			JSONArray alumnos = (JSONArray) respuesta.get("Alumnos");
			System.out.println("--------"+alumnos);
			for (Object object : alumnos) {
				JSONObject aux = (JSONObject) object;
				
				
				String clave = aux.get("dni").toString();
				int cod = Integer.parseInt(aux.get("cod").toString());
				String nombre = aux.get("nombre").toString();
				String apellido = aux.get("apellido").toString();
				int telefono = Integer.parseInt(aux.get("telefono").toString());
				String nacionalidad = aux.get("nacionalidad").toString();
				System.err.println("antes de titu");
				Titulacion titulacion = recogerTitulaciones.get(aux.get("titulacion").toString());
				System.err.println("despues de titu");
				Alumno alumno = new Alumno(cod,clave,nombre,apellido,telefono,nacionalidad,titulacion);
				System.err.println("despues de alumno");
				recogerAlumnos.put(clave, alumno);
				System.err.println("despues de recoger alumnos");
			}
			System.out.println("-----------------"+recogerAlumnos.size());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return recogerAlumnos;
	}

	@Override
	public boolean insertarAlumno(Alumno alumno) {
		SET_insert_ALUMNO = "escribirAlumno.php";
		JSONObject objAlumno = new JSONObject();
		JSONObject objPeticion = new JSONObject();
		try {
			recogerAlumnos.put(alumno.getDni(), alumno);
			objAlumno.put("dni", alumno.getDni());
			objAlumno.put("nombre", alumno.getNombre());
			objAlumno.put("apellido", alumno.getApellido());
			objAlumno.put("telefono", alumno.getTelefono());
			objAlumno.put("nacionalidad", alumno.getNacionalidad());
			objAlumno.put("titulacion", alumno.getTitulacionAlumno().getCod());
			
			
			objPeticion.put("peticion", "add");
			objPeticion.put("alumnoAdd", objAlumno);
			String json = objPeticion.toJSONString();
			
			System.out.println("Lanzamos peticion JSON para almacenar un jugador");

			String url = SERVER_PATH + SET_insert_ALUMNO;

			System.out.println("La url a la que lanzamos la peticiÛn es " + url);
			System.out.println("El json que enviamos es: ");
			System.out.println(json);
			// System.exit(-1);

			String response = encargadoPeticiones.postRequest(url, json);

			System.out.println("El json que recibimos es: ");

			System.out.println(response); // Traza para pruebas
			//System.exit(-1);

			// Parseamos la respuesta y la convertimos en un JSONObject

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay alg˙n error de parseo (json
										// incorrecto porque hay alg˙n caracter
										// raro, etc.) la respuesta ser· null
				System.out.println("El json recibido no es correcto. Finaliza la ejecuciÛn");
				//System.exit(-1);
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay alg˙n problema
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {

					System.out.println("Almacenado jugador enviado por JSON Remoto");

				} else { // Hemos recibido el json pero en el estado se nos
							// indica que ha habido alg˙n error

					System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

					//System.exit(-1);

				}
			}
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean borrarAlumno(String dni) {
		SET_delete_ALUMNO = "borrarAlumno.php";
		JSONObject objAlumno = new JSONObject();
		JSONObject objPeticion = new JSONObject();
		try {
			objAlumno.put("dni", dni);
			
			recogerAlumnos.remove(dni);
			
			objPeticion.put("peticion", "del");
			objPeticion.put("alumnoDel", objAlumno);
			String json = objPeticion.toJSONString();
			
			System.out.println("Lanzamos peticion JSON para almacenar un jugador");

			String url = SERVER_PATH + SET_delete_ALUMNO;

			System.out.println("La url a la que lanzamos la peticiÛn es " + url);
			System.out.println("El json que enviamos es: ");
			System.out.println(json);
			// System.exit(-1);

			String response = encargadoPeticiones.postRequest(url, json);

			System.out.println("El json que recibimos es: ");

			System.out.println(response); // Traza para pruebas
			//System.exit(-1);

			// Parseamos la respuesta y la convertimos en un JSONObject

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay alg˙n error de parseo (json
										// incorrecto porque hay alg˙n caracter
										// raro, etc.) la respuesta ser· null
				System.out.println("El json recibido no es correcto. Finaliza la ejecuciÛn");
				//System.exit(-1);
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay alg˙n problema
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {

					System.out.println("Almacenado jugador enviado por JSON Remoto");

				} else { // Hemos recibido el json pero en el estado se nos
							// indica que ha habido alg˙n error

					System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

					//System.exit(-1);

				}
			}
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean borrarTodoAlumnos() {
		SET_deleteAll_ALUMNO = "borrarTodoAlumno.php";
		URL url = null;
        try {
            url = new URL(SERVER_PATH + SET_deleteAll_ALUMNO);
            URLConnection con = url.openConnection();
             con.getInputStream(); // retornamos la entrada de la conexion abierta con el PHP
        } catch (IOException e) {
            e.printStackTrace();
        }
		return false;
	}

	@Override
	public boolean actualizarAlumnos(Alumno alumno) {
		SET_insert_ALUMNO = "actualizarAlumno.php";
		JSONObject objAlumno = new JSONObject();
		JSONObject objPeticion = new JSONObject();
		try {
			recogerAlumnos.put(alumno.getDni(), alumno);
			objAlumno.put("dni", alumno.getDni());
			objAlumno.put("nombre", alumno.getNombre());
			objAlumno.put("apellido", alumno.getApellido());
			objAlumno.put("telefono", alumno.getTelefono());
			objAlumno.put("nacionalidad", alumno.getNacionalidad());
			objAlumno.put("titulacion", alumno.getTitulacionAlumno().getCod());
			
			
			objPeticion.put("peticion", "add");
			objPeticion.put("alumnoUpd", objAlumno);
			String json = objPeticion.toJSONString();
			
			System.out.println("Lanzamos peticion JSON para almacenar un jugador");

			String url = SERVER_PATH + SET_insert_ALUMNO;

			System.out.println("La url a la que lanzamos la peticiÛn es " + url);
			System.out.println("El json que enviamos es: ");
			System.out.println(json);
			// System.exit(-1);

			String response = encargadoPeticiones.postRequest(url, json);

			System.out.println("El json que recibimos es: ");

			System.out.println(response); // Traza para pruebas
			//System.exit(-1);

			// Parseamos la respuesta y la convertimos en un JSONObject

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay alg˙n error de parseo (json
										// incorrecto porque hay alg˙n caracter
										// raro, etc.) la respuesta ser· null
				System.out.println("El json recibido no es correcto. Finaliza la ejecuciÛn");
				System.exit(-1);
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay alg˙n problema
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {

					System.out.println("Almacenado jugador enviado por JSON Remoto");

				} else { // Hemos recibido el json pero en el estado se nos
							// indica que ha habido alg˙n error

					System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

					System.exit(-1);

				}
			}
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean insertarTodosAlumnos(HashMap<String, Alumno> mapAlumno) {
		boolean todoOK = true;
		borrarTodoAlumnos();
		for (String key : mapAlumno.keySet()) {
			todoOK = this.insertarAlumno(mapAlumno.get(key));
		}
		
		return todoOK;
	}

	@Override
	public HashMap<String, Titulacion> obtenerTitulacion() {
		GET = "leerTitulaciones.php";
		//recogerTitulaciones = new HashMap<String, Titulacion>();
		
		try{
			
		    System.out.println("---------- Leemos datos de JSON --------------------");	
		    
			System.out.println("Lanzamos peticion JSON para titulaciones");
			
			String url = SERVER_PATH + GET; // Sacadas de configuracion
			
			System.out.println("La url a la que lanzamos la peticion es " + url);
			
			String response = encargadoPeticiones.getRequest(url);
			
			//System.out.println(response); // Traza para pruebas
			JSONObject respuesta = (JSONObject) JSONValue.parse(response);
			JSONArray titulaciones = (JSONArray) respuesta.get("Titulaciones");
			for (Object object : titulaciones) {
				JSONObject aux = (JSONObject) object;
				String clave = aux.get("nombre").toString();
				Titulacion titulacion = new Titulacion(Integer.parseInt(aux.get("cod").toString()),
						clave, aux.get("descripcion").toString());
				recogerTitulaciones.put(clave, titulacion);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return recogerTitulaciones;
	}

	@Override
	public boolean insertarTitulacion(Titulacion titulacion) {
		SET_insert_TITULACION = "escribirTitulacion.php";
		JSONObject objTitulacion = new JSONObject();
		JSONObject objPeticion = new JSONObject();
		try {
			recogerTitulaciones.put(titulacion.getNombre(), titulacion);
			

			objTitulacion.put("nombre", titulacion.getNombre());
			objTitulacion.put("descripcion", titulacion.getDescripcion());
			
			
			
			objPeticion.put("peticion", "add");
			objPeticion.put("titulacionAdd", objTitulacion);
			String json = objPeticion.toJSONString();
			
			System.out.println("Lanzamos peticion JSON para almacenar un jugador");

			String url = SERVER_PATH + SET_insert_TITULACION;

			System.out.println("La url a la que lanzamos la peticiÛn es " + url);
			System.out.println("El json que enviamos es: ");
			System.out.println(json);
			// System.exit(-1);

			String response = encargadoPeticiones.postRequest(url, json);

			System.out.println("El json que recibimos es: ");

			System.out.println(response); // Traza para pruebas
			//System.exit(-1);

			// Parseamos la respuesta y la convertimos en un JSONObject

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay alg˙n error de parseo (json
										// incorrecto porque hay alg˙n caracter
										// raro, etc.) la respuesta ser· null
				System.out.println("El json recibido no es correcto. Finaliza la ejecuciÛn");
				//System.exit(-1);
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay alg˙n problema
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {

					System.out.println("Almacenado jugador enviado por JSON Remoto");

				} else { // Hemos recibido el json pero en el estado se nos
							// indica que ha habido alg˙n error

					System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

					//System.exit(-1);

				}
			}
		}catch (Exception e) {
				// TODO: handle exception
			}
		return false;
	}

	@Override
	public boolean insertarTodosTitulaciones(HashMap<String, Titulacion> mapTitulacion) {
		// TODO Auto-generated method stub
		boolean todoOK = true;
		for(String key:mapTitulacion.keySet()){
			//todoOK = this.insertarTitulacion(mapTitulacion.get(key));
			SET_insert_TITULACION = "escribirTodoTitulacion.php";
			JSONObject objTitulacion = new JSONObject();
			JSONObject objPeticion = new JSONObject();
			try {
				recogerTitulaciones.put(key, mapTitulacion.get(key));
				
				objTitulacion.put("cod", mapTitulacion.get(key).getCod());
				System.out.println("-----------"+mapTitulacion.get(key).getCod());
				objTitulacion.put("nombre", mapTitulacion.get(key).getNombre());
				objTitulacion.put("descripcion", mapTitulacion.get(key).getDescripcion());
				
				
				
				objPeticion.put("peticion", "add");
				objPeticion.put("titulacionAdd", objTitulacion);
				String json = objPeticion.toJSONString();
				
				System.out.println("Lanzamos peticion JSON para almacenar un jugador");

				String url = SERVER_PATH + SET_insert_TITULACION;

				System.out.println("La url a la que lanzamos la peticiÛn es " + url);
				System.out.println("El json que enviamos es: ");
				System.out.println(json);
				// System.exit(-1);

				String response = encargadoPeticiones.postRequest(url, json);

				System.out.println("El json que recibimos es: ");

				System.out.println(response); // Traza para pruebas
				//System.exit(-1);

				// Parseamos la respuesta y la convertimos en un JSONObject

				JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

				if (respuesta == null) { // Si hay alg˙n error de parseo (json
											// incorrecto porque hay alg˙n caracter
											// raro, etc.) la respuesta ser· null
					System.out.println("El json recibido no es correcto. Finaliza la ejecuciÛn");
					//System.exit(-1);
				} else { // El JSON recibido es correcto

					// Sera "ok" si todo ha ido bien o "error" si hay alg˙n problema
					String estado = (String) respuesta.get("estado");
					if (estado.equals("ok")) {

						System.out.println("Almacenado jugador enviado por JSON Remoto");

					} else { // Hemos recibido el json pero en el estado se nos
								// indica que ha habido alg˙n error

						System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
						System.out.println("Error: " + (String) respuesta.get("error"));
						System.out.println("Consulta: " + (String) respuesta.get("query"));

						//System.exit(-1);

					}
				}
			}catch (Exception e) {
					// TODO: handle exception
				}
		}
		return todoOK;
	}
	
}
