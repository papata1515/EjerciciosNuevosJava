package Bancos;

import java.io.*;
import java.util.Scanner;

public class Utilidades {
	public static Scanner sc = new Scanner(System.in);

	public static String leerDni() {
		String dni;
		while (true) {
			System.out.print("DNI (8 números y 1 letra): ");
			dni = sc.nextLine().trim().toUpperCase();
			if (!dni.matches("^[0-9]{8}[A-Z]$")) {
				System.out.println("Error: Formato inválido.");
			} else if (existeDni(dni)) {
				System.out.println("Error: Ya registrado.");
			} else
				return dni;
		}
	}

	public static boolean existeDni(String dni) {
		File f = new File(GestionEmpleados.NOMBRE_FICHERO);
		if (!f.exists())
			return false;
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				if (linea.split(";")[0].equalsIgnoreCase(dni))
					return true;
			}
		} catch (IOException e) {
			return false;
		}
		return false;
	}

	public static int leerEdad() {
		while (true) {
			try {
				System.out.print("Edad: ");
				int edad = Integer.parseInt(sc.nextLine());
				if (edad >= 18 && edad <= 99)
					return edad;
				System.out.println("Error: Entre 18 y 99.");
			} catch (Exception e) {
				System.out.println("Error: Número entero.");
			}
		}
	}

	public static double leerSueldo() {
		while (true) {
			try {
				System.out.print("Sueldo: ");
				double sueldo = Double.parseDouble(sc.nextLine().replace(',', '.'));
				if (sueldo >= 0)
					return sueldo;
			} catch (Exception e) {
				System.out.println("Error: Sueldo inválido.");
			}
		}
	}

	public static String leerTextoSeguro(String etiqueta) {
		String texto;
		while (true) {
			System.out.print(etiqueta + ": ");
			texto = sc.nextLine().trim();
			if (texto.contains(";") || texto.isEmpty())
				System.out.println("Error: Texto inválido.");
			else
				return texto;
		}
	}
}