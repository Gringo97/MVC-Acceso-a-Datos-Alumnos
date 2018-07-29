<?php

/*  Formato JSON esperado */

$arrEsperado = array();
$arrAlumnoEsperado = array();

$arrEsperado["peticion"] = "add";

$arrAlumnoEsperado["dni"] = "45615427B (String)";
$arrAlumnoEsperado["nombre"] = "Paco (String)";
$arrAlumnoEsperado["apellido"] = "Fernandez (String)";
$arrAlumnoEsperado["telefono"] = "628192748 (Un int)";
$arrAlumnoEsperado["nacionalidad"] = "España (String)";
$arrAlumnoEsperado["titulacion"] = "1 (Un int)";

/*
            $arrAlumno["cod"] = $row["cod"];
			$arrAlumno["dni"] = $row["dni"];
			$arrAlumno["nombre"] = $row["nombre"];
            $arrAlumno["apellido"] = $row["apellido"];
            $arrAlumno["telefono"] = $row["telefono"];
            $arrAlumno["nacionalidad"] = $row["nacionalidad"];
            $arrAlumno["titulacion"] = $row["tituNOmbre"];
*/

$arrEsperado["alumnoUpd"] = $arrAlumnoEsperado;


/* Funcion para comprobar si el recibido es igual al esperado */

function JSONCorrectoAnnadir($recibido){

	$auxCorrecto = false;

	if(isset($recibido["peticion"]) && $recibido["peticion"] ="add" && isset($recibido["alumnoUpd"])){

		$auxAlumno = $recibido["alumnoUpd"];
		if(isset($auxAlumno["dni"]) && isset($auxAlumno["nombre"]) && 
		isset($auxAlumno["apellido"]) && isset($auxAlumno["telefono"]) && 
		isset($auxAlumno["nacionalidad"]) && isset($auxAlumno["titulacion"])){
			$auxCorrecto = true;
		}

	}


	return $auxCorrecto;

}
