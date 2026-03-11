package BBVA.EX;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class ProcesadorContratos {

	// Nombres de los archivos de entrada y salida
	static final String ORIGEN = "datos_contratosE.C.txt";
	static final String CORRECTOS = "contratos_validosC.txt";
	static final String ERRORES = "errores_procesamientoC.txt";

	public static void main(String[] args) {
		// Punto de entrada: arranca la maquinaria
		procesarFichero();
	}

	public static void procesarFichero() {
		// Comprobamos si el archivo existe antes de empezar
		File f = new File(ORIGEN);
		if (!f.exists())
			return;

		// Abrimos las tuberías de lectura (BufferedReader) y escritura (PrintWriter)
		try (BufferedReader br = new BufferedReader(new FileReader(f));
				PrintWriter pwCorrectos = new PrintWriter(new FileWriter(CORRECTOS));
				PrintWriter pwErrores = new PrintWriter(new FileWriter(ERRORES))) {

			String linea;
			int numLinea = 0;

			// Leemos la primera línea (los títulos: Empresa, IBAN, etc.) y la copiamos tal
			// cual
			String cabecera = br.readLine();
			pwCorrectos.println(cabecera);

			// Empezamos a leer los datos reales línea por línea
			while ((linea = br.readLine()) != null) {
				numLinea++;
				// Si la línea está vacía, saltamos a la siguiente
				if (linea.trim().isEmpty())
					continue;

				// Cortamos la línea por las comas
				// El -1 ignore las comas vacías al final de la línea
				String[] campos = linea.split(",", -1);

				// Si la línea no tiene al menos 6 columnas, es basura y la mandamos a errores
				if (campos.length < 6) {
					pwErrores.println("Línea " + numLinea + ": Columnas insuficientes.");
					continue;
				}

				try {
					// Aquí tratamos cada columna por separado
					// Normalizamos texto simple (Empresa, IBAN, Producto)
					String empresa = normalizarTexto(campos[0], "Desconocida");
					String iban = normalizarTexto(campos[1], "ES00000000000000000000");
					String producto = normalizarTexto(campos[2], "otros");

					// Convertimos el importe a número decimal (ej: "diez mil" -> 10000.0)
					double importe = normalizarImporte(campos[3]);

					// Arreglamos la fecha (ej: "ayer" -> fecha de hoy menos 1)
					String fecha = normalizarFecha(campos[4]);

					// Convertimos "si/no" o "1/0" a un número entero 1 o 0
					int activa = normalizarActiva(campos[5]);

					// 3. ESCRITURA: Guardamos los datos ya limpios y ordenados
					// %s = texto, %.2f = número con 2 decimales, %d = número entero
					pwCorrectos.printf("%s,%s,%s,%.2f,%s,%d%n", empresa, iban, producto, importe, fecha, activa);

				} catch (Exception e) {
					// Si algo falla dentro del "try" (ej: un dato muy corrupto), va a errores
					pwErrores.println("Línea " + numLinea + ": " + e.getMessage() + " -> [" + linea + "]");
				}
			}
			System.out.println(">> Proceso terminado. Revisa los archivos generados.");

		} catch (IOException e) {
			// Error técnico de lectura o escritura de archivos
			System.err.println("Error: " + e.getMessage());
		}
	}

	// --- MÉTODOS DE AYUDA (LOS "LIMPIADORES") ---

	/**
	 * Si el texto está vacío, le pone un valor por defecto.
	 */
	private static String normalizarTexto(String texto, String valorDefecto) {
		if (texto == null || texto.trim().isEmpty())
			return valorDefecto;
		return texto.trim();
	}

	/**
	 * Limpia el importe: quita paréntesis, cambia letras por números y lo convierte
	 * a Double.
	 */
	private static double normalizarImporte(String importeRaw) {
		if (importeRaw == null || importeRaw.isEmpty())
			return 0.0;
		String limpio = importeRaw.toLowerCase().replace("diez mil", "10000") // Cambio de palabra a número
				.replace("(", "").replace(")", "") // Quita los paréntesis
				.trim();
		try {
			return Double.parseDouble(limpio);
		} catch (NumberFormatException e) {
			return 0.0; // Si no puede convertirlo, pone 0.0 por seguridad
		}
	}

	/**
	 * Maneja fechas: entiende "ayer", arregla las "/" y prueba varios formatos.
	 */
	private static String normalizarFecha(String fechaRaw) {
		// Si dice "ayer", calcula la fecha de hoy y le resta un día
		if (fechaRaw == null || fechaRaw.isEmpty() || fechaRaw.equalsIgnoreCase("ayer")) {
			return LocalDate.now().minusDays(1).toString();
		}

		String fecha = fechaRaw.replace("/", "-").trim();

		// Listado de formatos que el programa intentará reconocer
		String[] formatos = { "yyyy-MM-dd", "dd-MM-yyyy", "yyyy-M-d", "d-M-yyyy" };

		for (String formato : formatos) {
			try {
				// Intenta leer la fecha con el formato actual del bucle
				return LocalDate.parse(fecha, DateTimeFormatter.ofPattern(formato)).toString();
			} catch (DateTimeParseException ignored) {
			} // Si falla, prueba con el siguiente formato
		}

		// Si ningún formato funciona, pone la fecha de hoy
		return LocalDate.now().toString();
	}

	/**
	 * Convierte estados lógicos a binario (1 para activado, 0 para desactivado).
	 */
	private static int normalizarActiva(String activaRaw) {
		String s = activaRaw.toLowerCase().trim();
		// Si el texto es "1", "1.0" o contiene "si", devolvemos un 1
		if (s.equals("1") || s.equals("1.0") || s.contains("si"))
			return 1;
		return 0; // Para todo lo demás (no, 0, vacío), devolvemos 0
	}
}
