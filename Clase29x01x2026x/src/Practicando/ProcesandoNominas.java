package Practicando;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class Empleado {

	/**
	 * ID por cada trabajador
	 */
	int id;

	/**
	 * Nombres de cada trabajador
	 */
	String nombre;

	/**
	 * Sueldo base de cada trabajador
	 */
	double sueldoBase;

	/**
	 * Horas extras de cada trabajador
	 */
	int horasExtra;

	/**
	 * Sueldo total por cada trabajador
	 */
	double sueldoTotal;

	/**
	 * Categoria a la que pertenece el trabajador
	 */
	char categoria;

	/**
	 * Metodo donde creamos los atributos del objeto * @param id
	 * 
	 * @param nombre
	 * @param sueldoBase
	 * @param horasExtra
	 */
	public Empleado(int id, String nombre, double sueldoBase, int horasExtra) {
		this.id = id;
		this.nombre = nombre;
		this.sueldoBase = sueldoBase;
		this.horasExtra = horasExtra;
		calcularSueldo();
	}

	/**
	 * Calcula el sueldo total y asigna la categoria correspondiente
	 */
	public void calcularSueldo() {
		// Sueldo base + (horas extra * 25€)
		this.sueldoTotal = this.sueldoBase + (this.horasExtra * 25);

		// Asignacion de categoria segun sueldo total
		if (this.sueldoTotal > 3000) {
			this.categoria = 'A';
		} else if (this.sueldoTotal >= 2000) {
			this.categoria = 'B';
		} else {
			this.categoria = 'C';
		}
	}
}

public class ProcesandoNominas {
	static final int MAX_EMPLEADOS = 150;
	static Empleado[] listaEmpleados = new Empleado[MAX_EMPLEADOS];
	static int numEmpleados = 0;
	static int lineasTotales = 0;

	/**
	 * Lee el archivo CSV y procesa cada linea validando los datos
	 * 
	 * @param ruta
	 */
	public static void leerArchivoCSV(String ruta) {
		try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				lineasTotales++;

				if (numEmpleados >= MAX_EMPLEADOS)
					break;

				String[] trozos = linea.split(",");

				// Validacion de numero de campos
				if (trozos.length != 4) {
					System.out.println("línea " + lineasTotales + " ignorada (" + trozos.length + " campos): " + linea);
					continue;
				}

				try {
					int id = Integer.parseInt(trozos[0].trim());
					String nombre = trozos[1].trim();
					double sueldoB = Double.parseDouble(trozos[2].trim());
					int horasE = Integer.parseInt(trozos[3].trim());

					// Validacion de sueldo y horas positivas
					if (sueldoB <= 0 || horasE < 0) {
						String motivo = sueldoB <= 0 ? "sueldo invalido" : "horas extra negativas";
						System.out.println("línea " + lineasTotales + " ignorada (" + motivo + "): " + linea);
						continue;
					}

					// Busqueda de duplicados por ID
					if (buscarEmpleadoPorId(id) != -1) {
						System.out.println("línea " + lineasTotales + " ignorada (ID duplicado): " + linea);
						continue;
					}

					// Guardado del empleado procesado
					Empleado nuevo = new Empleado(id, nombre, sueldoB, horasE);
					listaEmpleados[numEmpleados++] = nuevo;

					System.out.println("línea " + lineasTotales + ": " + nombre + " (ID:" + id + ", "
							+ String.format("%.2f", nuevo.sueldoTotal) + "€)");

				} catch (NumberFormatException e) {
					System.out.println("línea " + lineasTotales + " error numerico: " + linea);
				}
			} 
		} catch (IOException e) {
			System.out.println("Error al leer el archivo.");
		}
	}

	/**
	 * Busca si un ID ya existe en la lista actual
	 * 
	 * @param id
	 * @return posicion o -1 si no existe
	 */
	public static int buscarEmpleadoPorId(int id) {
		for (int i = 0; i < numEmpleados; i++) {
			if (listaEmpleados[i].id == id)
				return i;
		}
		return -1;
	}

	/**
	 * Ordena el array de empleados por ID mediante Bubble Sort
	 */
	public static void ordenarPorId() {
		for (int i = 0; i < numEmpleados - 1; i++) {
			for (int j = 0; j < numEmpleados - i - 1; j++) {
				if (listaEmpleados[j].id > listaEmpleados[j + 1].id) {
					Empleado temp = listaEmpleados[j];
					listaEmpleados[j] = listaEmpleados[j + 1];
					listaEmpleados[j + 1] = temp;
				}
			}
		}
	}

	/**
	 * Genera el archivo de texto con el resumen y detalle de empleados
	 * 
	 * @param ruta
	 */
	public static void generarInformeTxt(String ruta) {
		try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
			pw.println("INFORME DE NÓMINAS");
			pw.println("==================");

			double totalA = 0, totalB = 0, totalC = 0;
			int horasA = 0, horasB = 0, horasC = 0;
			Empleado mejorA = null, mejorB = null, mejorC = null;

			for (int i = 0; i < numEmpleados; i++) {
				Empleado e = listaEmpleados[i];
				if (e.categoria == 'A') {
					totalA += e.sueldoTotal;
					horasA += e.horasExtra;
					if (mejorA == null || e.sueldoTotal > mejorA.sueldoTotal)
						mejorA = e;
				} else if (e.categoria == 'B') {
					totalB += e.sueldoTotal;
					horasB += e.horasExtra;
					if (mejorB == null || e.sueldoTotal > mejorB.sueldoTotal)
						mejorB = e;
				} else {
					totalC += e.sueldoTotal;
					horasC += e.horasExtra;
					if (mejorC == null || e.sueldoTotal > mejorC.sueldoTotal)
						mejorC = e;
				}
			}

			pw.printf("CATEGORIA A (>3000€): Total %.2f€ - Mejor: %s - Total Horas Extra en Categoría: %d%n", totalA,
					(mejorA != null ? mejorA.nombre : "N/A"), horasA);
			pw.printf("CATEGORIA B (2000-3000€): Total %.2f€ - Mejor: %s - Total Horas Extra en Categoría: %d%n",
					totalB, (mejorB != null ? mejorB.nombre : "N/A"), horasB);
			pw.printf("CATEGORIA C (<2000€): Total %.2f€ - Mejor: %s - Total Horas Extra en Categoría: %d%n", totalC,
					(mejorC != null ? mejorC.nombre : "N/A"), horasC);

			pw.println("\nDETALLE EMPLEADOS:");
			pw.println("ID  | Nombre          | Sueldo Base | Horas Extra | Sueldo Total | Categoría");
			pw.println("----+-----------------+-------------+-------------+--------------+----------");

			for (int i = 0; i < numEmpleados; i++) {
				Empleado e = listaEmpleados[i];
				pw.printf("%-3d | %-15s | %11.2f | %11d | %12.2f |    %c%n", e.id, e.nombre, e.sueldoBase, e.horasExtra,
						e.sueldoTotal, e.categoria);
			}

			pw.printf("\nGRAN TOTAL NOMINAS: %.2f€%n", (totalA + totalB + totalC));

		} catch (IOException e) {
			System.out.println("Error al generar el archivo de salida.");
		}
	}

	public static void main(String[] args) {
		System.out.println("PROCESANDO NÓMINAS\n");

		leerArchivoCSV("empleados.csv");
		ordenarPorId();
		generarInformeTxt("nominas.txt");

		System.out.println("\nRESUMEN PROCESAMIENTO:");
		System.out.println("líneas totales: " + lineasTotales);
		System.out.println("Empleados únicos: " + numEmpleados + "/" + MAX_EMPLEADOS);
		System.out.println("\nInforme generado: nominas.txt");
	}
}