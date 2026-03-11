package uuuu;

import java.io.*;
import java.util.Scanner;

public class Banco {
	
	/**
	 * Creamos el scanner 
	 */
	static Scanner sc = new Scanner(System.in);
	
	/**
	 * Creamos un final para el fichero para que si en caso queramos modificarlo solo lo hagamos aca
	 */
	static final String NOMBRE_FICHERO = "empleados.txt";
	
	/**
	 * Verifica la existencia del fichero de datos. Si no existe, lo crea vacío para
	 * evitar errores de lectura.
	 */
	public static void inicializarArchivo() {
		File Fichero1 = new File(NOMBRE_FICHERO);
		try {
			if (!Fichero1.exists()) {
				if (Fichero1.createNewFile()) {
					System.out.println(">> Sistema: Archivo 'empleados.txt' creado correctamente.");
				}
			}
		} catch (IOException e) {
			System.err.println("Error crítico: No se tiene permisos para crear el archivo.");
		}
	}
	
	/**
	 * Lo que queremos que se muestre por pantalla
	 */
	public static void SalidaPorPantalla() {
		
		System.out.println("\n-------- GESTIÓN DE EMPLEADOS --------");
		System.out.println("1. Añadir empleado");
		System.out.println("2. Listar empleados");
		System.out.println("3. Buscar empleado por DNI");
		System.out.println("4. Calcular sueldo medio");
		System.out.println("5. Listar Empleados por Salarios");
		System.out.println("6. Búsqueda por nombre y apellidos");
		System.out.println("7. Modificar Empleado");
		System.out.println("8. Eliminar Empleado"); // Añadido
	    System.out.println("9. Generar Informe Económico"); // Añadido
	    System.out.println("10. Salir"); // Corregido
	}
	
	
	

}
