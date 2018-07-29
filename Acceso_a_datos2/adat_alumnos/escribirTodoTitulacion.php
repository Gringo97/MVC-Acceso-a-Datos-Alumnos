<?php

require 'bbdd.php'; // Incluimos fichero en la que está la coenxión con la BBDD
require 'jsonEsperadoTodoTitu.php';

/*
 * Se mostrará siempre la información en formato json para que se pueda leer desde un html (via js)
 * o una aplicación móvil o de escritorio realizada en java o en otro lenguajes
 */

$arrMensaje = array();  // Este array es el codificaremos como JSON tanto si hay resultado como si hay error

/*
 * Lo primero es comprobar que nos han enviado la información via JSON
 */

$parameters = file_get_contents("php://input");

if(isset($parameters)){

	// Parseamos el string json y lo convertimos a objeto JSON
	$mensajeRecibido = json_decode($parameters, true);
	// Comprobamos que están todos los datos en el json que hemos recibido
	// Funcion declarada en jsonEsperado.php
	if(JSONCorrectoAnnadir($mensajeRecibido)){

		$titulacion = $mensajeRecibido["titulacionAdd"]; 
        
		$cod = $titulacion["cod"];
		$nombre = $titulacion["nombre"];
		$descripcion = $titulacion["descripcion"];
		
        $query  = "INSERT INTO `titulaciones`(`cod`,`nombre`, `descripcion`)";
        $query .= "VALUES ('$cod','$nombre','$descripcion')";
		
		$result = $conn->query ( $query );
		
		if (isset ( $result ) && $result) { // Si pasa por este if, la query está está bien y se ha insertado correctamente
			
			$arrMensaje["estado"] = "ok";
			$arrMensaje["mensaje"] = "Titulacion insertado correctamente";
			
		}else{ // Se ha producido algún error al ejecutar la query
			
			$arrMensaje["estado"] = "error";
			$arrMensaje["mensaje"] = "SE HA PRODUCIDO UN ERROR AL ACCEDER A LA BASE DE DATOS";
			$arrMensaje["error"] = $conn->error;
			$arrMensaje["query"] = $query;
			
		}

		
	}else{ // Nos ha llegado un json no tiene los campos necesarios
		
		$arrMensaje["estado"] = "error";
		$arrMensaje["mensaje"] = "EL JSON NO CONTIENE LOS CAMPOS ESPERADOS";
		$arrMensaje["recibido"] = $mensajeRecibido;
		$arrMensaje["esperado"] = $arrEsperado;
	}

}else{	// No nos han enviado el json correctamente
	
	$arrMensaje["estado"] = "error";
	$arrMensaje["mensaje"] = "EL JSON NO SE HA ENVIADO CORRECTAMENTE";
	
}

$mensajeJSON = json_encode($arrMensaje,JSON_PRETTY_PRINT);

//echo "<pre>";  // Descomentar si se quiere ver resultado "bonito" en navegador. Solo para pruebas
echo $mensajeJSON;
//echo "</pre>"; // Descomentar si se quiere ver resultado "bonito" en navegador

$conn->close ();

die();

?>