<?php

/*  Formato JSON esperado */

$arrEsperado = array();
$arrTitulacionesEsperado = array();

$arrEsperado["peticion"] = "add";

$arrTitulacionesEsperado["nombre"] = "DAMP (String)";
$arrTitulacionesEsperado["descripcion"] = "Desarrollo de app (String)";



$arrEsperado["titulacionAdd"] = $arrTitulacionesEsperado;


/* Funcion para comprobar si el recibido es igual al esperado */

function JSONCorrectoAnnadir($recibido){

	$auxCorrecto = false;

	if(isset($recibido["peticion"]) && $recibido["peticion"] ="add" && isset($recibido["titulacionAdd"])){

		$auxAlumno = $recibido["titulacionAdd"];
		if(isset($auxAlumno["nombre"]) && isset($auxAlumno["descripcion"])){
			$auxCorrecto = true;
		}

	}


	return $auxCorrecto;

}
