<?php

require 'bbdd.php'; // Incluimos fichero en la que está la coenxión con la BBDD

/*
 * Se mostrará siempre la información en formato json para que se pueda leer desde un html (via js)
 * o una aplicación móvil o de escritorio realizada en java o en otro lenguajes
 */

$arrMensaje = array();  // Este array es el codificaremos como JSON tanto si hay resultado como si hay error



$query = "SELECT * FROM `titulaciones`";

$result = $conn->query ( $query );

if (isset ( $result ) && $result) { // Si pasa por este if, la query está está bien y se obtiene resultado
	
	if ($result->num_rows > 0) { // Aunque la query esté bien puede no obtenerse resultado (tabla vacía). Comprobamos antes de recorrer
		
		$arrTitulaciones = array();
		
		while ( $row = $result->fetch_assoc () ) {
			
			// Por cada vuelta del bucle creamos un Titulaciones. Como es un objeto hacemos un array asociativo
			$arrTitulacion = array();
			// Por cada columna de la tabla creamos una propiedad para el objeto
			$arrTitulacion["cod"] = $row["cod"];
            $arrTitulacion["nombre"] = $row["nombre"];
            $arrTitulacion["descripcion"] = $row["descripcion"];
            
			// Por último, añadimos el nuevo Titulaciones al array de Titulaciones
			$arrTitulaciones[] = $arrTitulacion;
			
		}
		
		// Añadimos al $arrMensaje el array de Titulaciones y añadimos un campo para indicar que todo ha ido OK
		$arrMensaje["estado"] = "ok";
		$arrMensaje["Titulaciones"] = $arrTitulaciones;
		
		
	} else {
		
		$arrMensaje["estado"] = "ok";
		$arrMensaje["Titulaciones"] = []; // Array vacío si no hay resultados
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