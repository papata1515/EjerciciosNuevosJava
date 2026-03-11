package GestordePartidas;

import java.util.Locale;
import java.util.Scanner;

public class Personajes {
	
	static Scanner sc = new Scanner(System.in);
	
	/**
	 * Creamos un final para el fichero para que si en caso queramos modificarlo
	 * solo lo hagamos aca
	 */
	static final String NOMBRE_FICHERO = "partidas.txt";
	
	/**
	 * id del jugador
	 */
	private String id;
	/**
	 * personaje del jugador
	 */
	private String agente;
	/**
	 * muertes por jugador
	 */
	private int deaths;
	/**
	 * asesinatos por jugador 
	 */
	private int kills;
	/**
	 * media del jugadro
	 */
	private double kda ;

	
	/**
	 * @param dni       id del jugador
	 * @param firstName Npersonaje del jugador
	 * @param lastName  muertes por jugador
	 * @param age       asesinatos por jugador
	 * @param salary    media del jugadro
	 */
	public Personajes(String id, String agente, int deaths, int kills, double kda) {
		super();
		this.id = id;
		this.agente = agente;
		this.deaths = deaths;
		this.kills = kills;
		this.kda = kda;

	}

	/**
	 * @return id del jugador
	 */
	public String getid() {
		return id;
	}

	/**
	 * @return Nombre del jugador
	 */
	public String getagente() {
		return agente;
	}

	/**
	 * @return muertes del empleado
	 */
	public int getdeaths() {
		return deaths;
	}

	/**
	 * @return Edad del empleado
	 */
	public int getkills() {
		return kills;
	}

	/**
	 * @return Sueldo del empleado
	 */
	public double getkda() {
		return kda;
	}
	
	/**
	 * @return Datos en el formato CSV
	 */
	public String toCsv() {
		// Locale.US para cambiar coma a punto en números con decimales para que pueda
		// funcionar el parse
		return String.format(Locale.US, "%s;%s;%d;%d;%.2f", id, agente, deaths, kills, kda);
	}

	/**
	 * Genera objeto empleado basado en la línea CSV
	 * 
	 * @param line Línea CSV original
	 * @return Objeto Employee relleno
	 */
	public static Personajes fromCsv(String line) {
		String[] parts = line.trim().split(";");
		// Si no contiene el número de parámetros correctos no es válido y devolvemos null
		if (parts.length != 6) {
			System.out.println("Error de formato CSV, deben existir 5 parámetros.");
			return null;
		}
		// Creamos el empleado en un try catch por si no tienen números en parámetros 4 y/o 5
		Personajes newEmployee;
		try {
			newEmployee = new Personajes(parts[0].trim(), parts[1].trim(),Integer.parseInt(parts[2].trim()),
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
		return String.format("id: %s Agente: %s deaths: %d kills: %d  kda: %.2f", id, agente, deaths,
				kills, kda);
	}

	/**
	 * Calcula la letra del id
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
	public static String normalizeDni(String id) {
		if (id == null)
			return null;
		return id.replace(" ", "").replace("-", "").replace("_", "").toUpperCase().trim();
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
		if (dni.length() != 4)
			return false;

		// Comprobar si la parte númerica son números y guardar el número para calcular
		// la letra
		int num = 0;
		try {
			num = Integer.parseInt(dni.substring(1,3));
		} catch (NumberFormatException e) {
			// Si no es un número no es válido
			return false;
		}

		// Sacamos la letra
		var letter = dni.toUpperCase().charAt(0);

		// Devolvemos la comprobación de la letra del DNI y la letra calculada
		return calcularLetra(num) == letter;
	}
}
	
}
