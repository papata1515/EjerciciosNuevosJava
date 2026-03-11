package uuuu;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Clase principal para la gestión de empleados mediante ficheros de texto.
 * Utiliza un formato CSV sencillo separado por punto y coma (;).
 */
public class Ayudasss {

	// Definimos el Scanner como estático para usarlo en toda la clase [cite: 304]
	static Scanner sc = new Scanner(System.in);

	// Nombre del archivo centralizado para no cometer errores al escribirlo [cite:
	// 265]
	static final String NOMBRE_FICHERO = "empleados.txt";

	/**
	 * PASO INICIAL: Preparar el terreno Verifica si el archivo existe. Si no, lo
	 * crea para evitar errores de lectura[cite: 245, 569].
	 */
	public static void inicializarArchivo() {
		File f = new File(NOMBRE_FICHERO);
		try {
			if (!f.exists()) {
				if (f.createNewFile()) {
					System.out.println(">> Sistema: Archivo creado correctamente.");
				}
			}
		} catch (IOException e) {
			System.err.println("Error crítico: Sin permisos para crear el archivo.");
		}
	}

	/**
	 * NUEVA FUNCIÓN: CONTAR LÍNEAS Útil para saber cuántos empleados hay en total.
	 */
	public static int contarLineasFichero() {
		int contador = 0;
		// try-with-resources cierra el archivo solo al terminar [cite: 628]
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			while (br.readLine() != null) {
				contador++; // Suma 1 por cada línea que no sea nula
			}
		} catch (IOException e) {
			System.err.println("Error al contar registros.");
		}
		return contador;
	}

	/**
	 * NUEVA FUNCIÓN: COMPARAR FICHEROS Compara línea a línea dos archivos
	 * distintos[cite: 373].
	 */
	public static void compararFicheros(String arch1, String arch2) {
		try (BufferedReader br1 = new BufferedReader(new FileReader(arch1));
				BufferedReader br2 = new BufferedReader(new FileReader(arch2))) {
			String l1, l2;
			boolean iguales = true;
			// Lee ambos al mismo tiempo mientras tengan contenido
			while ((l1 = br1.readLine()) != null && (l2 = br2.readLine()) != null) {
				if (!l1.equals(l2)) { // Compara contenido de Strings [cite: 58]
					iguales = false;
					break;
				}
			}
			System.out.println(iguales ? "Son iguales." : "Son diferentes.");
		} catch (IOException e) {
			System.err.println("No se pudo realizar la comparación.");
		}
	}

	// --- MÉTODOS DE VALIDACIÓN (Tus "Escudos" de datos) ---

	public static String leerDni() {
		String dni;
		while (true) {
			System.out.print("DNI (8 números y 1 letra): ");
			dni = sc.nextLine().trim().toUpperCase();
			// Validamos con expresión regular (Regex) [cite: 150]
			if (!dni.matches("^[0-9]{8}[A-Z]$")) {
				System.err.println("Formato incorrecto.");
			} else if (!validarLetraDNI(dni)) {
				System.err.println("La letra no coincide con el número.");
			} else if (existeDni(dni)) {
				System.err.println("Este DNI ya está registrado.");
			} else {
				return dni;
			}
		}
	}

	public static boolean existeDni(String dni) {
		File f = new File(NOMBRE_FICHERO);
		if (!f.exists())
			return false;
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				// split con -1 para no perder campos vacíos [cite: 151, 153]
				String[] d = linea.split(";", -1);
				if (d[0].equalsIgnoreCase(dni))
					return true;
			}
		} catch (IOException e) {
			return false;
		}
		return false;
	}

	public static double leerSueldo() {
		while (true) {
			try {
				System.out.print("Sueldo: ");
				// Reemplazamos coma por punto para el parseDouble [cite: 109, 113]
				double s = Double.parseDouble(sc.nextLine().replace(',', '.'));
				if (s >= 0)
					return s;
				System.out.println("No puede ser negativo.");
			} catch (NumberFormatException e) {
				System.out.println("Formato inválido.");
			}
		}
	}

	// --- LAS 10 OPCIONES DEL MENÚ ---

	// CASO 1: Añadir (Append) [cite: 562, 571]
	public static void aniadirEmpleado() {
		String dni = leerDni();
		String nom = leerTextoSeguro("Nombre");
		String ape = leerTextoSeguro("Apellidos");
		int ed = leerEdad();
		double sue = leerSueldo();
		// El 'true' activa el modo de anexar (no borra lo anterior) [cite: 562]
		try (PrintWriter pw = new PrintWriter(new FileWriter(NOMBRE_FICHERO, true))) {
			pw.println(dni + ";" + nom + ";" + ape + ";" + ed + ";" + sue);
			System.out.println("Empleado guardado.");
		} catch (IOException e) {
			System.err.println("Error al escribir.");
		}
	}

	// CASO 2: Listar todo el archivo [cite: 267, 274]
	public static void listarEmpleados() {
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String linea;
			System.out.println("\n--- LISTADO ---");
			while ((linea = br.readLine()) != null) {
				System.out.println(linea.replace(";", " | "));
			}
		} catch (IOException e) {
			System.err.println("Error de lectura.");
		}
	}

	// CASO 3: Buscar por DNI [cite: 737, 739]
	public static void buscarPorDni() {
		System.out.print("DNI a buscar: ");
		String busq = sc.nextLine().trim();
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String l;
			boolean hallado = false;
			while ((l = br.readLine()) != null) {
				String[] d = l.split(";");
				if (d[0].equalsIgnoreCase(busq)) {
					System.out.println("Encontrado: " + l.replace(";", " | "));
					hallado = true;
					break;
				}
			}
			if (!hallado)
				System.out.println("No existe.");
		} catch (IOException e) {
		}
	}

	// CASO 4: Sueldo Medio [cite: 292, 741]
	public static void calcularSueldoMedio() {
		double suma = 0;
		int cont = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String l;
			while ((l = br.readLine()) != null) {
				String[] d = l.split(";");
				if (d.length >= 5) {
					suma += Double.parseDouble(d[4]);
					cont++;
				}
			}
			if (cont > 0)
				System.out.printf("Media: %.2f€\n", (suma / cont));
		} catch (Exception e) {
		}
	}

	// CASO 5: Filtrar por Rango de Sueldos [cite: 744, 745]
	public static void listaPorSueldo() {
		System.out.println("Sueldo mínimo:");
		double min = leerSueldo();
		System.out.println("Sueldo máximo:");
		double max = leerSueldo();
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String l;
			while ((l = br.readLine()) != null) {
				String[] d = l.split(";");
				double s = Double.parseDouble(d[4]);
				if (s >= Math.min(min, max) && s <= Math.max(min, max)) {
					System.out.println(l);
				}
			}
		} catch (Exception e) {
		}
	}

	// CASO 6: Buscar por Nombre/Apellido [cite: 746, 747]
	public static void busquedaPorNombre() {
		String busq = leerTextoSeguro("Nombre a buscar").toLowerCase();
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String l;
			while ((l = br.readLine()) != null) {
				if (l.toLowerCase().contains(busq))
					System.out.println(l);
			}
		} catch (Exception e) {
		}
	}

	// CASO 7: Modificar (Usa archivo temporal) [cite: 674, 688, 712]
	public static void modificarEmpleado() {
		System.out.print("DNI para modificar sueldo: ");
		String dni = sc.nextLine().trim().toUpperCase();
		if (!existeDni(dni))
			return;
		double nuevoS = leerSueldo();
		File orig = new File(NOMBRE_FICHERO);
		File temp = new File("temp.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(orig));
				PrintWriter pw = new PrintWriter(new FileWriter(temp))) {
			String l;
			while ((l = br.readLine()) != null) {
				String[] d = l.split(";");
				if (d[0].equalsIgnoreCase(dni)) {
					pw.println(d[0] + ";" + d[1] + ";" + d[2] + ";" + d[3] + ";" + nuevoS);
				} else {
					pw.println(l);
				}
			}
		} catch (IOException e) {
		}
		orig.delete();
		temp.renameTo(orig); // Reemplazamos el original [cite: 712]
	}

	// CASO 8: Eliminar [cite: 751, 752]
	public static void eliminarEmpleado() {
		System.out.print("DNI a eliminar: ");
		String dni = sc.nextLine().trim().toUpperCase();
		File orig = new File(NOMBRE_FICHERO);
		File temp = new File("temp_del.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(orig));
				PrintWriter pw = new PrintWriter(new FileWriter(temp))) {
			String l;
			while ((l = br.readLine()) != null) {
				String[] d = l.split(";");
				// Copiamos todos MENOS el que queremos borrar
				if (!d[0].equalsIgnoreCase(dni))
					pw.println(l);
			}
		} catch (IOException e) {
		}
		orig.delete();
		temp.renameTo(orig);
	}

	// CASO 9: Informe Económico con cálculos de SS [cite: 753, 756]
	public static void generarInforme() {
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO));
				PrintWriter pw = new PrintWriter(new FileWriter("informe.txt"))) {
			String l;
			pw.println("--- INFORME DE SEGUROS SOCIALES ---");
			while ((l = br.readLine()) != null) {
				String[] d = l.split(";");
				int edad = Integer.parseInt(d[3]);
				double sueldo = Double.parseDouble(d[4]);
				double ss = (edad <= 25) ? sueldo * 0.12 : (edad <= 45) ? sueldo * 0.18 : sueldo * 0.23;
				pw.printf("%s %s: SS = %.2f€\n", d[1], d[2], ss); // Formato con 2 decimales [cite: 120]
			}
			System.out.println("Informe generado en 'informe.txt'");
		} catch (Exception e) {
		}
	}

	// --- MÉTODOS AUXILIARES ---

	public static String leerTextoSeguro(String campo) {
		System.out.print(campo + ": ");
		return sc.nextLine().replace(";", "").trim();
	}

	public static int leerEdad() {
		System.out.print("Edad: ");
		return Integer.parseInt(sc.nextLine());
	}

	public static boolean validarLetraDNI(String dni) {
		int num = Integer.parseInt(dni.substring(0, 8));
		String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
		return dni.charAt(8) == letras.charAt(num % 23);
	}

	// EL CORAZÓN DEL PROGRAMA: MAIN [cite: 272, 303]
	public static void main(String[] args) {
		inicializarArchivo();
		int op;
		do {
			System.out
					.println("\n1.Añadir 2.Listar 3.Buscar 4.Media 5.Rango 6.Nombre 7.Modif 8.Elim 9.Informe 10.Salir");
			System.out.print("Opción: ");
			try {
				op = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				op = 0;
			}

			switch (op) {
			case 1:
				aniadirEmpleado();
				break;
			case 2:
				listarEmpleados();
				break;
			case 3:
				buscarPorDni();
				break;
			case 4:
				calcularSueldoMedio();
				break;
			case 5:
				listaPorSueldo();
				break;
			case 6:
				busquedaPorNombre();
				break;
			case 7:
				modificarEmpleado();
				break;
			case 8:
				eliminarEmpleado();
				break;
			case 9:
				generarInforme();
				break;
			}
		} while (op != 10);
	}
}