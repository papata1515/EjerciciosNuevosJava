package archivos.e05;

import java.util.Scanner;

/**
 * Clase auxiliar para pedir valores al usuario
 */
public class CustomInput {
	/**
	 * Pedir texto al usuario
	 * 
	 * @param sc    Scanner
	 * @param label Mensaje al usuario
	 * @return Cadena de respuesta
	 */
	public static String requestInput(Scanner sc, String label) {
		System.out.print(label);
		return sc.nextLine().trim();
	}

	/**
	 * Pedir un entero al usuario
	 * 
	 * @param sc    Scanner
	 * @param label Mensaje al usuario
	 * @return Entero introducido por el usuario o -1 al encontrar errores
	 */
	public static int requestInteger(Scanner sc, String label) {
		try {
			return Integer.parseInt(requestInput(sc, label));
		} catch (NumberFormatException e) {
			System.err.println("Error, se esperaba valor númerico");
			return -1;
		}
	}

	/**
	 * Pedir un valor decimal al usuario
	 * 
	 * @param sc    Scanner
	 * @param label Mensaje al usuario
	 * @return Entero introducido por el usuario o -1 al encontrar errores
	 */
	public static double requestDouble(Scanner sc, String label) {
		try {
			return Double.parseDouble(requestInput(sc, label));
		} catch (NumberFormatException e) {
			System.err.println("Error, se esperaba valor númerico");
			return -1;
		}
	}
}

