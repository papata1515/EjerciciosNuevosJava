package Practicando;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calcular {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("numeros.txt")); // se queda
			String linea = ""; // se queda
			double suma = 0; //
			int contador = 0; //

			while ((linea = br.readLine()) != null) {
				System.out.println(linea + " pingúinos ");
				try {
					// Convertimos la línea a número decimal (por si hay decimales en el txt)
					double numero = Double.parseDouble(linea.trim());

					suma += numero; // Acumulamos el valor
					contador++; // Contamos el número
				} catch (NumberFormatException e) {
					System.out.println("Línea omitida (no es un número): " + linea);
				}

			}

			if (contador > 0) {
				double media = suma / contador;
				System.out.println("---------------------------------");
				System.out.println("Total de números: " + contador);
				System.out.println("Suma total: " + suma);
				System.out.printf("La media es: %.2f%n", media);
				// %.2f formatea a 2 decimales
			} else {
				System.out.println("No se encontraron números válidos para calcular la media.");
			}

			br.close(); // ¡IMPORTANTE CERRAR EL FICHERO!
		} catch (IOException ioe) {
			System.out.println("Error al leer");
		}
	}

}
