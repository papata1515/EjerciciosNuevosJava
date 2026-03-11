package archivos.e05;

import java.util.Scanner;

/**
 * <h1>Gestión datos empleados</h1>
 * <p>
 * Diseña una aplicación en Java que gestione un fichero de texto llamado
 * empleados.txt usando acceso secuencial. El fichero almacenará información de
 * empleados, una persona por línea, con el siguiente formato:<br>
 * dni;nombre;apellidos;edad;sueldo<br>
 * Por ejemplo:<br>
 * 12345678A;Ana;López Pérez;32;1850.50<br>
 * 87654321B;Juan;Martín Ruiz;45;2200.00<br>
 * <h2>La aplicación debe mostrar un menú con las siguientes opciones:</h2>
 * <h3>1. Añadir empleado</h3>
 * <p>
 * Pedirá por teclado todos los datos del empleado. <br>
 * Al pedir los datos del empleado debe comprobar lo siguiente:
 * <ul>
 * <li>DNI válido</li>
 * <li>Edad número positivo entre 18-99</li>
 * <li>Salario número positivo 1100-8000</li>
 * </ul>
 * Abrirá el fichero en modo añadir (append) y escribirá la nueva línea al final
 * del fichero.
 * <h3>2. Listar empleados</h3>
 * <p>
 * Leerá el fichero línea a línea y mostrará por pantalla los datos de todos los
 * empleados.
 * <h3>3. Buscar empleado por DNI</h3>
 * <p>
 * Pedirá un DNI por teclado (no será sensible a mayúsculas). <br>
 * Recorrerá el fichero secuencialmente y mostrará los datos del empleado si lo
 * encuentra, o un mensaje indicándolo si no existe. <br>
 * <h3>4. Calcular sueldo medio</h3>
 * <p>
 * Recorrerá el fichero, acumulará los sueldos y mostrará en pantalla el sueldo
 * medio de todos los empleados.
 * <h3>5. Salir</h3>
 * <p>
 * Terminará la aplicación cerrando correctamente cualquier flujo abierto.
 * <h3>6. Listar Empleados por Salarios</h3>
 * <p>
 * Listar los empleados que tengan los salarios comprendidos entre dos salarios
 * que se pidan por teclado (esos salarios deben ser válidos y chequearlos).
 * <h3>7. Búsqueda por nombre y apellidos</h3>
 * <p>
 * Por teclado meterán el nombre y apellidos y lo buscará. No es sensible a
 * mayúsculas/minúsculas. Pueden haber más de una persona con los mismos nombre
 * y apellidos.
 * <h2>Requisitos técnicos</h2>
 * <p>
 * Utiliza clases de flujos de caracteres (FileReader, BufferedReader,
 * FileWriter, PrintWriter) para trabajar con el fichero de texto. <br>
 * Controla las posibles excepciones de E/S con bloques try-catch adecuados.
 * <br>
 * Antes de leer el fichero, comprueba si existe; si no existe, créalo vacío la
 * primera vez que se necesite. <br>
 * Se valorará el diseño modular: al menos un método para cada opción del menú y
 * otro para mostrar el menú y leer la opción seleccionada.
 * <h2>Autor: Pablo Illescas</h2>
 */
public class Executable {
	public static final int maxOptions = 9;

	/**
	 * Crear empleado
	 * 
	 * @param sc  Scanner
	 * @param dni DNI del empleado
	 * @return Empleado creado
	 */
	private static Personajes createEmployee(Scanner sc, String id) {

		var tempFName = CustomInput.requestInput(sc, "Nombre del empleado: ");
		var tempLName = CustomInput.requestInput(sc, "Apellidos del empleado: ");

		int tempAge;
		do {
			tempAge = CustomInput.requestInteger(sc, "Edad: ");
		} while (tempAge < Personajes.MIN_AGE || tempAge > Personajes.MAX_AGE);

		double tempSalary;
		do {
			tempSalary = CustomInput.requestDouble(sc, "Sueldo: ");
		} while (tempSalary < Personajes.MIN_SALARY || tempSalary > Personajes.MAX_SALARY);

		return new Personajes(id, tempFName, tempLName, tempAge, tempSalary);
	}

	/**
	 * Crear empleado
	 * 
	 * @param sc          Scanner
	 * @param fileHandler FileHandling
	 * @return Empleado creado
	 */
	private static Personajes requestEmployee(Scanner sc, FileHandling fileHandler) {
		System.out.println("Introduzca:");

		// Variables creadas fuera del do while
		String tempID;
		var isValid = false;
		// Mientras el DNI no sea válido
		do {
			// Pedimos DNI y comprobamos si es válido
			tempID = CustomInput.requestInput(sc, "DNI del empleado: ");
			tempID = Personajes.normalizeDni(tempID);
			isValid = Personajes.isValidId(tempID);
			// Si no es válido avisamos antes del siguiente bucle
			if (!isValid) {
				System.err.println("DNI no válido");
				continue;
			}
			// Si el empleado ya existe avisamos y cambiamos el valor de no válido a falso
			if (fileHandler.findEmployee(tempID) != null) {
				System.err.println("Ya existe un empleado con ese DNI");
				isValid = false;
			}
		} while (!isValid);
		return createEmployee(sc, tempID);
	}

	/**
	 * Añadir empleado con FileHandling
	 * 
	 * @param sc          Scanner
	 * @param fileHandler FileHandling
	 */
	private static void addEmployee(Scanner sc, FileHandling fileHandler) {
		// Añadir el empleado
		fileHandler.addEmployee(requestEmployee(sc, fileHandler));
	}

	/**
	 * Mandar a modificar un empleado
	 * 
	 * @param sc          Scanner
	 * @param fileHandler FileHandling
	 */
	private static void modifyEmployee(Scanner sc, FileHandling fileHandler) {
		// Pedir DNI
		var id = Personajes.normalizeDni(CustomInput.requestInput(sc, "Introduzca el DNI a modificar: "));
		// Si no es válido avisar
		if (!Personajes.isValidId(id)) {
			System.err.println("DNI no válido");
			return;
		}
		// Si No existe el empleado avisar
		if (fileHandler.findEmployee(id) == null) {
			System.err.println("No existe ningún empleado con ese DNI");
			return;
		}
		// Crear empleado
		var temp = createEmployee(sc, id);
		// Modificar empleado
		if (fileHandler.modifyEmployee(id, temp))
			System.out.println("Empleado modificado correctamente");
		else
			System.out.println("No se pudo modificar el empleado");
	}

	/**
	 * Mandar a eliminar un empleado
	 * 
	 * @param sc          Scanner
	 * @param fileHandler FileHandling
	 */
	private static void deleteEmployee(Scanner sc, FileHandling fileHandler) {
		// Pedir DNI
		var id = Personajes.normalizeDni(CustomInput.requestInput(sc, "Introduzca el DNI a eliminar: "));
		// Si no es válido avisar
		if (!Personajes.isValidId(id)) {
			System.err.println("DNI no válido");
			return;
		}
		// Si No existe el empleado avisar
		if (fileHandler.findEmployee(id) == null) {
			System.err.println("No existe ningún empleado con ese DNI");
			return;
		}
		// Eliminar empleado
		if (fileHandler.deleteEmployee(id))
			System.out.println("Empleado eliminado correctamente");
		else
			System.out.println("No se pudo eliminar el empleado");
	}

	/**
	 * Listar los empleados en un rango de sueldo
	 * 
	 * @param sc          Scanner
	 * @param fileHandler FileHandling
	 */
	private static void listInRange(Scanner sc, FileHandling fileHandler) {
		// Pedir mínimo con validación de rango
		double min = 0;
		do {
			min = CustomInput.requestDouble(sc, "Sueldo mínimo: ");
			if (min < Personajes.MIN_SALARY || min > Personajes.MAX_SALARY)
				System.out.printf("Error, el salario debe estar comprendido entre %.2f y %.2f €%n", Personajes.MIN_SALARY,
						Personajes.MAX_SALARY);
		} while (min < Personajes.MIN_SALARY || min > Personajes.MAX_SALARY);
		// Pedir máximo con validación de rango
		double max = 0;
		do {
			max = CustomInput.requestDouble(sc, "Sueldo máximo: ");
			if (max < min || max > Personajes.MAX_SALARY)
				System.out.printf("Error, el salario debe estar comprendido entre %.2f y %.2f €%n", min,
						Personajes.MAX_SALARY);
		} while (max < min || max > Personajes.MAX_SALARY);
		// Listar en el rango
		fileHandler.listEmployees(min, max);
	}

	/**
	 * Listar empleados con nombre concreto
	 * 
	 * @param sc          Scanner
	 * @param fileHandler FileHandling
	 */
	private static void listByName(Scanner sc, FileHandling fileHandler) {
		var fName = CustomInput.requestInput(sc, "Nombre del empleado: ");
		var lName = CustomInput.requestInput(sc, "Apellidos del empleado: ");
		fileHandler.listByName(fName, lName);
	}

	/**
	 * Bucle principal de la aplicación
	 */
	private static void appLoop() {
		// Abrir Scanner en try para no tener que cerrarlo
		try (var sc = new Scanner(System.in);) {
			// Crear variable de control de opciones y objeto lector de ficheros
			int option = 0;
			var fileHandler = new FileHandling();

			// Mientras no usemos la opción de salida
			while (option != 5) {

				// Cadena con las opciones disponibles
				var label = String.format("Introduzca:%n"
						+ "1.- Para añadir un empleado%n"
						+ "2.- Para mostrar un listado de todos los empleados%n"
						+ "3.- Para buscar un empleado por su DNI%n"
						+ "4.- Para calcular el sueldo medio de todos los empleados%n"
						+ "5.- Para salir del programa%n"
						+ "6.- Listar Empleados por Salarios%n"
						+ "7.- Búsqueda por nombre y apellidos%n"
						+ "8.- Modificar empleado%n"
						+ "9.- Eliminar empleado%n");

				// Pedimos la opción a usar
				option = CustomInput.requestInteger(sc, label);

				// Si la opción no es válida avisamos
				if (option < 1 || option > maxOptions) {
					System.err.println("Error, opción no válida");
					CustomInput.requestInput(sc, "Pulse intro para continuar");
					// Y volvemos a repetir el bucle
					continue;
				}

				// Dependiendo del valor de la opción selecionada
				switch (option) {
				// Añadimos empleado a fichero
				case 1:
					addEmployee(sc, fileHandler);
					break;
				// Mostramos listado de empleados
				case 2:
					fileHandler.listEmployees();
					break;
				// Buscamos empleado
				case 3:
					fileHandler.showEmployee(CustomInput.requestInput(sc, "Introduzca el DNI del empleado a buscar: "));
					break;
				// Calculamos la media
				case 4:
					System.out.printf("Salario medio: %.2f €%n", fileHandler.getAverage());
					break;
				// Avisar de cierre de programa, sale sin más comandos por ser un while
				case 5:
					System.out.println("Cerrando programa.");
					break;
				// Listar empleados por Salario
				case 6:
					listInRange(sc, fileHandler);
					break;
				// Lista empleados por nombre
				case 7:
					listByName(sc, fileHandler);
					break;
				// Modificar empleado
				case 8:
					modifyEmployee(sc, fileHandler);
					break;
				// Eliminar empleado
				case 9:
					deleteEmployee(sc, fileHandler);
					break;
				}
				CustomInput.requestInput(sc, "Pulse intro para continuar");
			}
		}
	}

	public static void main(String args[]) {
		appLoop();
	}
}

