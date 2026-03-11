package EjercicioUno;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;

public class CSVventas {

	public void borrador() {
		String archivoEntrada = "ventas.csv";

		// Arrays para guardar los datos (tamaño 50 para tener espacio)
		String[] listaNombrePro = new String[50];
		String[] listaCantidadPro = new String[50];
		String[] listaPrecioPro = new String[50];

		int i = 0; // Índice para los arrays
		double totalVentas = 0;
		int totalProductos = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(archivoEntrada))) {
			String linea;

			while ((linea = br.readLine()) != null) {
				if (!linea.trim().isEmpty()) {
					String[] partes = linea.split(",");

					// Formato: Laptop(0), 2(1), 1200.50(2)
					if (partes.length == 3) {

						// 1. Parseamos los valores para poder hacer cálculos
						// Usamos trim() por si hay espacios invisibles
						int cantidad = Integer.parseInt(partes[1].trim());
						double precio = Double.parseDouble(partes[2].trim());

						// 2. Realizamos los cálculos matemáticos
						totalVentas += (cantidad * precio); // Sumamos al total acumulado
						totalProductos += cantidad; // Contamos cuántos objetos hay en total

						// 3. Guardamos en los arrays (como Strings, como pediste)
						if (i < 50) {
							listaNombrePro[i] = partes[0].trim();
							listaCantidadPro[i] = partes[1].trim();
							listaPrecioPro[i] = partes[2].trim();
							i++;
						}
					}
				}
			}

			// 4. Mostramos los resultados finales
			System.out.println("--- Resumen de Ventas ---");
			System.out.println("Productos procesados: " + i);
			System.out.println("Total de artículos vendidos: " + totalProductos);
			System.out.printf("Dinero total recaudado: %.2f€\n", totalVentas);

		} catch (IOException | NumberFormatException e) {
			System.out.println("Error al procesar el archivo: " + e.getMessage());
		}
	}

	public void Csv() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("ventas.csv"));
			String linea = br.readLine();
			while (linea != null) {

				float precio;
				int cantidad;
				String nombre;

				String[] Biblioteca = linea.trim().split(",");
				try {
					nombre = Biblioteca[0].trim();
					cantidad = Integer.parseInt(Biblioteca[1]);
					precio = Float.parseFloat(Biblioteca[2]);

					System.out.printf("nombre %s %d %f \n", nombre, cantidad, precio);
				} catch (NumberFormatException ioe) {
					System.out.println("Error numero ");
				}
				linea = br.readLine();
			}
			br.close();
		} catch (IOException ioe) {
			System.out.println("Error al leer");
		}
	}

	public static void main(String[] args) {

		CSVventas hola = new CSVventas();
		hola.Csv();

	}
}