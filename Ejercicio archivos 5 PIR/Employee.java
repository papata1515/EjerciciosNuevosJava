package archivos.e05;

import java.util.Locale;

/**
 * Clase auxiliar para almacenar la información de los empleados
 */
public class Employee {

	/**
	 * Edad mínima de los empleados
	 */
	public static final int MIN_AGE = 18;

	/**
	 * Edad máxima de los empleados
	 */
	public static final int MAX_AGE = 99;

	/**
	 * Salario mínimo de los empleados
	 */
	public static final double MIN_SALARY = 1100;

	/**
	 * Salario máximo de los empleados
	 */
	public static final double MAX_SALARY = 8000;
	
	/**
	 * Rango Inicial
	 */
	public static final int JUNIOR = 18;
	
	/**
	 * Rango Senior
	 */
	public static final int SENIOR = 45;
	
	/**
	 * Rango medio
	 */
	public static final int MEDIUM = 25;
	
	/**
	 * Seguro social inicial
	 */
	public static final int JUNIOR_WELFARE = 12;
	
	/**
	 * Seguro social medio
	 */
	public static final int MEDIUM_WELFARE = 18;
	
	/**
	 * Seguro social senior
	 */
	public static final int SENIOR_WELFARE = 23;

	/**
	 * DNI del empleado
	 */
	private String dni;
	/**
	 * Nombre del empleado
	 */
	private String firstName;
	/**
	 * Apellidos del empleado
	 */
	private String lastName;
	/**
	 * Edad del empleado
	 */
	private int age;
	/**
	 * Sueldo del empleado
	 */
	private double salary;

	/**
	 * @param dni       DNI del empleado
	 * @param firstName Nombre del Empleado
	 * @param lastName  Apellidos del Empleado
	 * @param age       Edad
	 * @param salary    Sueldo
	 */
	public Employee(String dni, String firstName, String lastName, int age, double salary) {
		super();
		this.dni = dni;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.salary = salary;
	}

	/**
	 * @return DNI del empleado
	 */
	public String getDni() {
		return dni;
	}

	/**
	 * @return Nombre del empleado
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return Apellidos del empleado
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return Edad del empleado
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @return Sueldo del empleado
	 */
	public double getSalary() {
		return salary;
	}

	/**
	 * @return Datos en el formato CSV
	 */
	public String toCsv() {
		// Locale.US para cambiar coma a punto en números con decimales para que pueda
		// funcionar el parse
		return String.format(Locale.US, "%s;%s;%s;%d;%.2f", dni, firstName, lastName, age, salary);
	}

	/**
	 * Genera objeto empleado basado en la línea CSV
	 * 
	 * @param line Línea CSV original
	 * @return Objeto Employee relleno
	 */
	public static Employee fromCsv(String line) {
		String[] parts = line.trim().split(";");
		// Si no contiene el número de parámetros correctos no es válido y devolvemos null
		if (parts.length != 5) {
			System.out.println("Error de formato CSV, deben existir 5 parámetros.");
			return null;
		}
		// Creamos el empleado en un try catch por si no tienen números en parámetros 4 y/o 5
		Employee newEmployee;
		try {
			newEmployee = new Employee(parts[0].trim(), parts[1].trim(), parts[2].trim(),
					Integer.parseInt(parts[3].trim()), Double.parseDouble(parts[4].trim()));
		} catch (NumberFormatException e) {
			System.out.println("Error de formato CSV, carácteres no númericos en parámetros 4 y/o 5");
			return null;
		}
		// Devolvemos el empleado creado
		return newEmployee;
	}

	/**
	 * Sobrecarga del metodo toString para mostrar la información del empleado
	 */
	public String toString() {
		return String.format("DNI: %s Apellidos y Nombre: %s, %s Edad: %d Sueldo: %.2f €", dni, lastName, firstName,
				age, salary);
	}

	/**
	 * Calcula la letra del DNI
	 * 
	 * @param num Número base
	 * @return Letra
	 */
	public static char calcularLetra(int num) {
		return "TRWAGMYFPDXBNJZSQVHLCKE".charAt(num % 23);
	}

	/**
	 * Normalizar: quitar espacios y guiones
	 * 
	 * @param dni DNI Original
	 * @return DNI normalizado
	 */
	public static String normalizeDni(String dni) {
		if (dni == null)
			return null;
		return dni.replace(" ", "").replace("-", "").replace("_", "").toUpperCase().trim();
	}

	/**
	 * Comprobar si un DNI puede ser válido
	 * 
	 * @param dni DNI a comprobar
	 * @return True si el DNI tiene la estructura y letra correctos
	 */
	public static boolean isValidId(String dni) {
		// Si es nulo no es válido
		if (dni == null)
			return false;
		
		// Normalizar: quitar espacios y guiones
		dni = normalizeDni(dni);

		// Si no tiene 9 caracteres no es válido
		if (dni.length() != 9)
			return false;

		// Comprobar si la parte númerica son números y guardar el número para calcular
		// la letra
		int num = 0;
		try {
			num = Integer.parseInt(dni.substring(0, 8));
		} catch (NumberFormatException e) {
			// Si no es un número no es válido
			return false;
		}

		// Sacamos la letra
		var letter = dni.toUpperCase().charAt(8);

		// Devolvemos la comprobación de la letra del DNI y la letra calculada
		return calcularLetra(num) == letter;
	}
}
