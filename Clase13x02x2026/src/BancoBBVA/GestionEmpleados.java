package BancoBBVA;

import java.io.*;
import java.util.Scanner;

public class GestionEmpleados {
	private static final String FICHERO = "empleados.txt";

	/**
	 * Verifica la existencia del fichero de datos. Si no existe, lo crea vacío para
	 * evitar errores de lectura.
	 */
	public static void inicializarArchivo() {
		File f = new File(FICHERO);
		try {
			if (!f.exists()) {
				if (f.createNewFile()) {
					System.out.println(">> Sistema: Archivo 'empleados.txt' creado correctamente.");
				}
			}
		} catch (IOException e) {
			System.err.println("Error crítico: No se tiene permisos para crear el archivo.");
		}
	}

	// --- MÉTODOS DE LECTURA Y BÚSQUEDA ---

	public static void listarTodo() {
		File f = new File(FICHERO);
		if (!f.exists()) {
			System.out.println("Archivo vacío.");
			return;
		}

		System.out.println("\n--- LISTADO COMPLETO ---");
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String l;
			while ((l = br.readLine()) != null) {
				System.out.println(l.replace(";", " | "));
			}
		} catch (IOException e) {
			System.err.println("Error de lectura.");
		}
	}

	public static void buscarPorDni(Scanner sc) {
		String dni = CustomInput.pedirTexto(sc, "DNI a buscar: ").toUpperCase();
		try (BufferedReader br = new BufferedReader(new FileReader(FICHERO))) {
			String l;
			while ((l = br.readLine()) != null) {
				if (l.startsWith(dni)) {
					System.out.println("Encontrado: " + l.replace(";", " | "));
					return;
				}
			}
			System.out.println("No existe ese DNI.");
		} catch (IOException e) {
			System.err.println("Error.");
		}
	}

	public static void buscarPorNombre(Scanner sc) {
		String busqueda = CustomInput.pedirTexto(sc, "Nombre o Apellido: ").toLowerCase();
		try (BufferedReader br = new BufferedReader(new FileReader(FICHERO))) {
			String l;
			while ((l = br.readLine()) != null) {
				String[] d = l.split(";");
				if (d.length >= 3 && (d[1] + d[2]).toLowerCase().contains(busqueda)) {
					System.out.println("Coincidencia: " + l.replace(";", " | "));
				}
			}
		} catch (IOException e) {
		}
	}

	public static void filtrarPorSueldo(Scanner sc) {
		double min = CustomInput.pedirDecimal(sc, "Sueldo mínimo: ");
		double max = CustomInput.pedirDecimal(sc, "Sueldo máximo: ");
		try (BufferedReader br = new BufferedReader(new FileReader(FICHERO))) {
			String l;
			while ((l = br.readLine()) != null) {
				String[] d = l.split(";");
				double s = Double.parseDouble(d[4]);
				if (s >= min && s <= max)
					System.out.println(l.replace(";", " | "));
			}
		} catch (Exception e) {
		}
	}

	// --- OPERACIONES DE MODIFICACIÓN (LA IDEA FELIZ) ---

	public static void aniadir(Scanner sc) {
		String dni;
		while (true) {
			dni = CustomInput.pedirTexto(sc, "DNI: ").toUpperCase();
			if (Validador.esDniValido(dni))
				break;
			System.err.println("DNI inválido.");
		}
		if (existeDni(dni)) {
			System.err.println("Ya existe.");
			return;
		}

		String n = CustomInput.pedirTexto(sc, "Nombre: ");
		String a = CustomInput.pedirTexto(sc, "Apellidos: ");
		int e = CustomInput.pedirEntero(sc, "Edad: ");
		double s = CustomInput.pedirDecimal(sc, "Sueldo: ");

		try (PrintWriter pw = new PrintWriter(new FileWriter(FICHERO, true))) {
			pw.println(dni + ";" + n + ";" + a + ";" + e + ";" + s);
			System.out.println("Guardado.");
		} catch (IOException ex) {
		}
	}

	private static void procesarFichero(String dni, double sueldo, boolean borrar) {
		File fOri = new File(FICHERO);
		File fTmp = new File("temp_" + FICHERO);
		try (BufferedReader br = new BufferedReader(new FileReader(fOri));
				PrintWriter pw = new PrintWriter(new FileWriter(fTmp))) {
			String l;
			while ((l = br.readLine()) != null) {
				String[] d = l.split(";");
				if (d[0].equalsIgnoreCase(dni)) {
					if (!borrar)
						pw.println(d[0] + ";" + d[1] + ";" + d[2] + ";" + d[3] + ";" + sueldo);
				} else {
					pw.println(l);
				}
			}
		} catch (IOException e) {
		}
		fOri.delete();
		fTmp.renameTo(fOri);
	}

	public static void modificarSueldo(Scanner sc) {
		String dni = CustomInput.pedirTexto(sc, "DNI: ").toUpperCase();
		if (existeDni(dni)) {
			double s = CustomInput.pedirDecimal(sc, "Nuevo Sueldo: ");
			procesarFichero(dni, s, false);
			System.out.println("Modificado.");
		} else {
			System.err.println("No existe.");
		}
	}

	public static void eliminar(Scanner sc) {
		String dni = CustomInput.pedirTexto(sc, "DNI a eliminar: ").toUpperCase();
		if (existeDni(dni)) {
			procesarFichero(dni, 0, true);
			System.out.println("Eliminado.");
		} else {
			System.err.println("No existe.");
		}
	}

	// --- INFORMES Y MEDIAS ---

	public static void calcularMediaTotal() {
		double suma = 0;
		int cont = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(FICHERO))) {
			String l;
			while ((l = br.readLine()) != null) {
				suma += Double.parseDouble(l.split(";")[4]);
				cont++;
			}
			System.out.printf("Sueldo medio global: %.2f€\n", (cont > 0 ? suma / cont : 0));
		} catch (Exception e) {
		}
	}

	public static void generarInforme() {
		String inf = "análisis_Salarial.txt";
		double sI = 0, sM = 0, sS = 0;
		int cI = 0, cM = 0, cS = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(FICHERO));
				PrintWriter pw = new PrintWriter(new FileWriter(inf))) {
			pw.println("NOMBRE | APELLIDO | SUELDO | SEGUROS SOCIALES");
			String l;
			while ((l = br.readLine()) != null) {
				String[] d = l.split(";");
				int edad = Integer.parseInt(d[3]);
				double sueldo = Double.parseDouble(d[4]);
				double ss = Validador.obtenerCuotaSS(edad, sueldo);
				pw.printf("%s | %s | %.2f€ | %.2f€\n", d[1], d[2], sueldo, ss);
				if (edad <= 25) {
					sI += sueldo;
					cI++;
				} else if (edad <= 45) {
					sM += sueldo;
					cM++;
				} else {
					sS += sueldo;
					cS++;
				}
			}
			pw.println("\n--- MEDIAS POR RANGO ---");
			pw.printf("Inicial: %.2f | Medio: %.2f | Senior: %.2f\n", (cI > 0 ? sI / cI : 0), (cM > 0 ? sM / cM : 0),
					(cS > 0 ? sS / cS : 0));
			System.out.println("Informe creado.");
		} catch (Exception e) {
		}
	}

	public static boolean existeDni(String dni) {
		File f = new File(FICHERO);
		if (!f.exists())
			return false;
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String l;
			while ((l = br.readLine()) != null) {
				if (l.split(";")[0].equalsIgnoreCase(dni))
					return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
}
