package controlador;

import java.util.Scanner;

public class Principal {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		System.out.println("Inicio Ejecucion - Elija el tipo de datos que quiere cargar");
		Scanner miScanner = new Scanner(System.in); // Para leer las opciones de teclado
		
		Controlador miControlador = new Controlador(miScanner);		
		
	}



}