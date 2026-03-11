package BancoBBVA;

public class Validador {
	/**
	 * Valida el formato Regex y el algoritmo de la letra del DNI.
	 */
	public static boolean esDniValido(String dni) {
		if (!dni.matches("^[0-9]{8}[A-Z]$"))
			return false;

		int numero = Integer.parseInt(dni.substring(0, 8));
		char letraDada = dni.charAt(8);
		String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
		return letraDada == letras.charAt(numero % 23);
	}

	/**
	 * Determina el porcentaje de Seguros Sociales por edad.
	 */
	public static double obtenerCuotaSS(int edad, double sueldo) {
		if (edad >= 18 && edad <= 25)
			return sueldo * 0.12;
		if (edad >= 26 && edad <= 45)
			return sueldo * 0.18;
		return sueldo * 0.23; // Senior 46+
	}
}
