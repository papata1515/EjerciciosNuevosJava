package Bancos;

import java.io.*;

public class GestionEmpleados {
	static final String NOMBRE_FICHERO = "empleados.txt";

	public static void aniadirEmpleado() {
		String dni = Utilidades.leerDni();
		String nombre = Utilidades.leerTextoSeguro("Nombre");
		String apellidos = Utilidades.leerTextoSeguro("Apellidos");
		int edad = Utilidades.leerEdad();
		double sueldo = Utilidades.leerSueldo();

		try (PrintWriter pw = new PrintWriter(new FileWriter(NOMBRE_FICHERO, true))) {
			pw.println(dni + ";" + nombre + ";" + apellidos + ";" + edad + ";" + sueldo);
			System.out.println("Empleado guardado correctamente.");
		} catch (IOException e) {
			System.out.println("Error al escribir: " + e.getMessage());
		}
	}

	public static void listarEmpleados() {
		File f = new File(NOMBRE_FICHERO);
		if (!f.exists()) {
			System.out.println("El fichero no existe.");
			return;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String linea;
			System.out.println("\n--- LISTA DE EMPLEADOS ---");
			while ((linea = br.readLine()) != null)
				System.out.println(linea);
		} catch (IOException e) {
			System.out.println("Error al leer.");
		}
	}

	public static void buscarPorDni() {
		System.out.print("Introduce el DNI a buscar: ");
		String dniBusqueda = Utilidades.sc.nextLine().trim();
		boolean encontrado = false;
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] datos = linea.split(";");
				if (datos.length > 0 && datos[0].equalsIgnoreCase(dniBusqueda)) {
					System.out.println("Empleado encontrado: " + linea.replace(";", " | "));
					encontrado = true;
					break;
				}
			}
			if (!encontrado)
				System.out.println("DNI no encontrado.");
		} catch (IOException e) {
			System.out.println("Error al leer.");
		}
	}

	public static void calcularSueldoMedio() {
		double sumaSueldos = 0;
		int contador = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] datos = linea.split(";");
				if (datos.length < 5)
					continue;
				try {
					sumaSueldos += Double.parseDouble(datos[4]);
					contador++;
				} catch (Exception e) {
				}
			}
			if (contador > 0)
				System.out.printf("Sueldo medio: %.2f\n", (sumaSueldos / contador));
			else
				System.out.println("No hay datos suficientes.");
		} catch (IOException e) {
			System.out.println("Error de acceso.");
		}
	}

	public static void ListaPorSueldo() {
		System.out.println("\n--- RANGO DE SUELDO ---");
		double s1 = Utilidades.leerSueldo();
		double s2 = Utilidades.leerSueldo();
		if (s1 == s2) {
			System.out.println("No pueden ser iguales.");
			return;
		}
		double min = Math.min(s1, s2), max = Math.max(s1, s2);
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] datos = linea.split(";");
				if (datos.length >= 5) {
					double sueldo = Double.parseDouble(datos[4]);
					if (sueldo >= min && sueldo <= max)
						System.out.println(linea.replace(";", " | "));
				}
			}
		} catch (IOException e) {
			System.out.println("Error al leer.");
		}
	}

	public static void BusquedaPorNombreyApelldio() {
		String nomBusqueda = Utilidades.leerTextoSeguro("Nombre o apellido").toLowerCase();
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] datos = linea.split(";");
				if (datos.length >= 3) {
					String completo = (datos[1] + " " + datos[2]).toLowerCase();
					if (completo.contains(nomBusqueda))
						System.out.println("Encontrado: " + linea.replace(";", " | "));
				}
			}
		} catch (IOException e) {
			System.out.println("Error.");
		}
	}
}