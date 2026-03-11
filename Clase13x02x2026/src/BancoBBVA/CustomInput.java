package BancoBBVA;

import java.util.Scanner;

public class CustomInput {
	/**
	 * Pide texto y elimina espacios sobrantes.
	 */
	public static String pedirTexto(Scanner sc, String mensaje) {
		System.out.print(mensaje);
		return sc.nextLine().trim();
	}

	/**
	 * Pide un entero. Si hay error de formato, devuelve -1.
	 */
	public static int pedirEntero(Scanner sc, String mensaje) {
		try {
			return Integer.parseInt(pedirTexto(sc, mensaje));
		} catch (NumberFormatException e) {
			System.err.println("Error: Se esperaba un número entero.");
			return -1;
		}
	}

	/**
	 * Pide un decimal. Maneja puntos y comas.
	 */
	public static double pedirDecimal(Scanner sc, String mensaje) {
		try {
			String entrada = pedirTexto(sc, mensaje).replace(',', '.');
			return Double.parseDouble(entrada);
		} catch (NumberFormatException e) {
			System.err.println("Error: Se esperaba un número decimal.");
			return -1.0;
		}
	}
}
