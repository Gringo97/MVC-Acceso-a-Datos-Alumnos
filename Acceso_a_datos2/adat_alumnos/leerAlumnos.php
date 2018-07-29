<?php

require 'bbdd.php'; // Incluimos fichero en la que está la coenxión con la BBDD

/*
 * Se mostrará siempre la información en formato json para que se pueda leer desde un html (via js)
 * o una aplicación móvil o de escritorio realizada en java o en otro lenguajes
 */

$arrMensaje = array();  // Este array es el codificaremos como JSON tanto si hay resultado como si hay error



$query = "SELECT alumnos.cod, dni,alumnos.nombre, apellido,telefono,nacionalidad,titulaciones.nombre as tituNOmbre 
FROM alumnos
JOIN titulaciones ON titulaciones.cod=alumnos.titulacion";

$result = $conn->query ( $query );

if (isset ( $result ) && $result) { // Si pasa por este if, la query está está bien y se obtiene resultado
	
	if ($result->num_rows > 0) { // Aunque la query esté bien puede no obtenerse resultado (tabla vacía). Comprobamos antes de recorrer
		
		$arrAlumnos = array();
		
		while ( $row = $result->fetch_assoc () ) {
			
			// Por cada vuelta del bucle creamos un Alumnos. Como es un objeto hacemos un array asociativo
			$arrAlumno = array();
			// Por cada columna de la tabla creamos una propiedad para el objeto
			$arrAlumno["cod"] = $row["cod"];
			$arrAlumno["dni"] = $row["dni"];
			$arrAlumno["nombre"] = $row["nombre"];
            $arrAlumno["apellido"] = $row["apellido"];
            $arrAlumno["telefono"] = $row["telefono"];
            $arrAlumno["nacionalidad"] = $row["nacionalidad"];
            $arrAlumno["titulacion"] = $row["tituNOmbre"];
			// Por último, añadimos el nuevo Alumnos al array de Alumnos
			$arrAlumnos[] = $arrAlumno;
			
		}
		
		// Añadimos al $arrMensaje el array de Alumnos y añadimos un campo para indicar que todo ha ido OK
		$arrMensaje["estado"] = "ok";
		$arrMensaje["Alumnos"] = $arrAlumnos;
		
		
	} else {
		
		$arrMensaje["estado"] = "ok";
		$arrMensaje["Alumnos"] = []; // Array vacío si no hay resultados
	}
	
} else {
	
	$arrMensaje["estado"] = "error";
	$arrMensaje["mensaje"] = "SE HA PRODUCIDO UN ERROR AL ACCEDER A LA BASE DE DATOS";
	$arrMensaje["error"] = $conn->error;
	$arrMensaje["query"] = $query;
	
}

$mensajeJSON = json_encode($arrMensaje,JSON_PRETTY_PRINT);

//echo "<pre>";  // Descomentar si se quiere ver resultado "bonito" en navegador. Solo para pruebas 
echo $mensajeJSON;
//echo "</pre>"; // Descomentar si se quiere ver resultado "bonito" en navegador

$conn->close ();

?>