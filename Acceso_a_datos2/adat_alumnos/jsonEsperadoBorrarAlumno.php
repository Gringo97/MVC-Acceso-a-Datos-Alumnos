<?php

/*  Formato JSON esperado */

$arrEsperado = array();
$arrAlumnoEsperado = array();

$arrEsperado["peticion"] = "del";

$arrAlumnoEsperado["dni"] = "45615427B (String)";


$arrEsperado["alumnoDel"] = $arrAlumnoEsperado;


/* Funcion para comprobar si el recibido es igual al esperado */

function JSONCorrectoAnnadir($recibido){

	$auxCorrecto = false;

	if(isset($recibido["peticion"]) && $recibido["peticion"] ="del" && isset($recibido["alumnoDel"])){

		$auxAlumno = $recibido["alumnoDel"];
		if(isset($auxAlumno["dni"])){
			$auxCorrecto = true;
		}

	}


	return $auxCorrecto;

}
