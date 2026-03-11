package archivos.e05;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clase auxiliar para controlar las operaciones con ficheros para el ejercicio
 * 5
 */
public class FileHandling {
	/**
	 * Archivo sobre el que trabajar
	 */
	public static final String FILE_NAME = "empleados.txt";	

	/**
	 * Archivo temporal para editar datos
	 */
	public static final String TEMP_FILE = "empleados.temp.txt";

	/**
	 * Comprobar si un archivo existe
	 * 
	 * @param fileName Nombre del archivo
	 * @return True si el archivo existe
	 */
	public static boolean fileExist(String fileName) {
		return new File(fileName).exists();
	}

	/**
	 * Crear archivo vacío
	 * 
	 * @param fileName Nombre del fichero
	 */
	public static void createFile(String fileName) {
		if (!fileExist(fileName)) {
			try {
				new File(fileName).createNewFile();
			} catch (IOException e) {
				System.err.println("No se pudo crear el archivo.");
				return;
			}
		}
	}

	/**
	 * Comprobar si hay salto de línea al final del archivo
	 * 
	 * @param fileName Archivo a analizar
	 * @return True si hay salto de línea o es de tamaño 0
	 * @throws IOException
	 */
	public static boolean newLineAtEOF(String fileName) throws IOException {
		File file = new File(fileName);

		// Si el tamaño es 0 no hay que recorrer el archivo
		if (file.length() == 0) {
			return true;
		}

		// Buscar con RandomAccessFile el último caracter para saber si es salto
		try (var raf = new java.io.RandomAccessFile(file, "r")) {
			raf.seek(file.length() - 1);
			int lastChar = raf.read();
			return lastChar == '\n';
		}
	}

	/**
	 * Mostrar todos los empleados por pantalla
	 */
	public void listEmployees() {
		// Si el archivo no existe avisar
		if (!fileExist(FILE_NAME)) {
			System.out.printf("El archivo \"%s\" no existe, no se pueden mostrar empleados.%n", FILE_NAME);
			return;
		}
		// Leer línea a línea los empleados y mostrarlos por pantalla
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
			String linea = br.readLine();
			while (linea != null) {
				// Descifrar csv
				var newEmployee = Personajes.fromCsv(linea);
				// Mostrar los datos del empleado si existe
				if (newEmployee != null)
					System.out.println(newEmployee);
				linea = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error al leer");
		}
	}

	/**
	 * Mostrar todos los empleados con salario comprendido en un rango por pantalla
	 * 
	 * @param min Salario mínimo a listar
	 * @param max Salario máximo a listar
	 */
	public void listEmployees(double min, double max) {
		// Si el archivo no existe avisar
		if (!fileExist(FILE_NAME)) {
			System.out.printf("El archivo \"%s\" no existe, no se pueden mostrar empleados.%n", FILE_NAME);
			return;
		}
		// Leer línea a línea los empleados y mostrarlos por pantalla
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
			String linea = br.readLine();
			while (linea != null) {
				// Descifrar csv
				var newEmployee = Personajes.fromCsv(linea);

				// Si existe el empleado y su salario esta en el rango lo mostramos
				if (newEmployee != null) {
					var salary = newEmployee.getSalary();
					if (salary >= min && salary <= max)
						System.out.println(newEmployee);
				}
				linea = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error al leer");
		}
	}

	/**
	 * Lista todos los empleados con el nombre y apellidos pasados como parámetros
	 * 
	 * @param fName Nombre a buscar
	 * @param lName Apellidos a buscar
	 */
	public void listByName(String fName, String lName) {
		// Si el archivo no existe avisar
		if (!fileExist(FILE_NAME)) {
			System.out.printf("El archivo \"%s\" no existe, no se pueden mostrar empleados.%n", FILE_NAME);
			return;
		}
		// Leer línea a línea los empleados y mostrarlos por pantalla
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
			String linea = br.readLine();
			while (linea != null) {
				// Descifrar csv
				var newEmployee = Personajes.fromCsv(linea);
				// Mostrar los datos del empleado si existe y coincide con el nombre
				if (newEmployee != null && newEmployee.getFirstName().trim().equalsIgnoreCase(fName)
						&& newEmployee.getLastName().trim().equalsIgnoreCase(lName))
					System.out.println(newEmployee);
				linea = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error al leer");
		}
	}

	/**
	 * Buscar empleado por DNI
	 * 
	 * @param dni DNI a buscar
	 * @return Empleado encontrado o null
	 */
	public Personajes findEmployee(String dni) {
		// Si no existe el fichero avisar
		if (!fileExist(FILE_NAME)) {
			System.out.printf("El archivo \"%s\" no existe, no se pueden buscar empleados.%n", FILE_NAME);
			return null;
		}

		dni = Personajes.normalizeDni(dni);

		// Leer el archivo línea a línea
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
			String linea = br.readLine();
			while (linea != null) {
				// Descifrar csv
				var newEmployee = Personajes.fromCsv(linea);
				// Si el empleado es el que buscamos lo devolvemos
				if (newEmployee != null && newEmployee.getDni().equalsIgnoreCase(dni.trim()))
					return newEmployee;
				linea = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error al leer el archivo");
		}
		// Si no hemos encontrado el empleado devolvemos null
		return null;
	}

	/**
	 * Mostrar la información del empleado con el DNI pasado
	 * 
	 * @param dni DNI a buscar
	 */
	public void showEmployee(String dni) {
		// Buscamos el empleado con el DNI pasado
		var employee = findEmployee(dni);
		// Si lo encontramos lo mostramos por pantalla, si no avisamos
		if (employee == null)
			System.out.printf("No se ha podido encontrar el empleado %s%n", dni);
		else
			System.out.println(employee);
	}

	/**
	 * @return Media de sueldos
	 */
	public double getAverage() {
		// Si no existe archivo a buscar avisamos
		if (!fileExist(FILE_NAME)) {
			System.out.printf("El archivo \"%s\" no existe, no se puede calcular la media.%n", FILE_NAME);
			return 0;
		}

		double sum = 0;
		int amount = 0;
		// Leemos cada línea
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
			String linea = br.readLine();
			while (linea != null) {
				// Desciframos csv y si existe empleado añadimos a suma y al contador
				var newEmployee = Personajes.fromCsv(linea);
				if (newEmployee != null) {
					sum += newEmployee.getSalary();
					amount++;
				}
				linea = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error al leer el archivo");
		}
		// Si el contador es 0 devolvemos 0, si hay algún empleado devolvemos la media
		if (amount == 0)
			return 0;
		return sum / amount;
	}

	/**
	 * Añadir empleado al archivo FILE_NAME
	 * 
	 * @param newEmployee
	 */
	public void addEmployee(Personajes newEmployee) {
		// Si el empleado no existe dar error
		if (newEmployee == null) {
			System.err.println("Empleado no válido, no se puede añadir.");
			return;
		}

		// Crear archivo si no existe
		if (!fileExist(FILE_NAME)) {
			createFile(FILE_NAME);
		}

		// Abrimos PrintWriter para escribir al archivo
		try (FileWriter fw = new FileWriter(FILE_NAME, true); // true para modo anexar
				PrintWriter pw = new PrintWriter(fw)) {
			// Si no hay salto de linea al final del archivo lo añadimos
			if (!newLineAtEOF(FILE_NAME))
				pw.println();
			// Añadimos el empleado
			pw.println(newEmployee.toCsv());
		} catch (IOException e) {
			System.err.println("Error al escribir en el fichero: " + e.getMessage());
		}
	}

	/**
	 * Modificar un empleado
	 * 
	 * @param id          DNI del empleado a modificar
	 * @param newEmployee Nuevos valores del empleado
	 * @return True si se modifican los datos
	 */
	public boolean modifyEmployee(String id, Personajes newEmployee) {
		// Si el empleado no existe dar error
		if (newEmployee == null || id == null) {
			System.out.println("Empleado no válido, no se puede modificar.");
			return false;
		}
		// Si no existe el fichero avisar
		if (!fileExist(FILE_NAME)) {
			System.out.printf("El archivo \"%s\" no existe, no se pueden modificar empleados.%n", FILE_NAME);
			return false;
		}
		// Si no existe el empleado avisamos
		String dni = Personajes.normalizeDni(id);
		if (findEmployee(dni) == null) {
			System.out.println("Error, no se ha podido encontrar el empleado.");
			return false;
		}
		// Creamos archivo temporal
		var tempFile = new File(TEMP_FILE);
		// Variable de control
		var updated = false;

		// Leer el archivo línea a línea
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
				PrintWriter pw = new PrintWriter(new FileWriter(TEMP_FILE));) {
			String line = br.readLine();
			// Recorrer archivo mientras queden líneas
			while (line != null) {
				// Descifrar csv
				var tempEmployee = Personajes.fromCsv(line);
				// Si el empleado es el que buscamos lo añadimos modificado
				if (newEmployee != null && tempEmployee.getDni().equalsIgnoreCase(dni)) {
					pw.println(newEmployee.toCsv());
					updated = true;
					// Si el empleado no es el que buscamos lo añadimos como estaba
				} else
					pw.println(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			System.err.println("Error al leer el archivo");
		}
		// Si no se ha modificado borramos archivo temporal
		if (!updated)
			tempFile.delete();
		// Si se ha modificado borramos el archivo original y cambiamos nombre al
		// temporal
		else {
			File original = new File(FILE_NAME);
			original.delete();
			tempFile.renameTo(original);
		}
		return updated;
	}

	/**
	 * Elimitar un empleado
	 * 
	 * @param id DNI del empleado a eliminar
	 * @return True si se ha eliminado el empleado
	 */
	public boolean deleteEmployee(String id) {
		// Si el empleado no existe dar error
		if (id == null || !Personajes.isValidId(id)) {
			System.out.println("Empleado no válido, no se puede eliminar.");
			return false;
		}
		// Si no existe el fichero avisar
		if (!fileExist(FILE_NAME)) {
			System.out.printf("El archivo \"%s\" no existe, no se pueden eliminar empleados.%n", FILE_NAME);
			return false;
		}
		// Si no existe el empleado avisamos
		String dni = Personajes.normalizeDni(id);
		if (findEmployee(dni) == null) {
			System.out.println("Error, no se ha podido encontrar el empleado.");
			return false;
		}

		// Creamos archivo temporal
		var tempFile = new File(TEMP_FILE);
		// Variable de control
		var deleted = false;

		// Leer el archivo línea a línea
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
				PrintWriter pw = new PrintWriter(new FileWriter(TEMP_FILE));) {
			String line = br.readLine();
			// Recorrer archivo mientras queden líneas
			while (line != null) {
				// Descifrar csv
				var tempEmployee = Personajes.fromCsv(line);
				// Si el empleado es el que buscamos marcamos borrado a verdadero
				if (tempEmployee.getDni().equalsIgnoreCase(dni))
					deleted = true;
				else
					// Si no es el empleado copiamos la línea
					pw.println(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			System.err.println("Error al leer el archivo");
		}
		// Si no se ha modificado borramos archivo temporal
		if (!deleted)
			tempFile.delete();
		// Si se ha modificado borramos el archivo original y cambiamos nombre al
		// temporal
		else {
			File original = new File(FILE_NAME);
			original.delete();
			tempFile.renameTo(original);
		}
		return deleted;
	}
}

