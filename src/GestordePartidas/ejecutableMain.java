package GestordePartidas;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;



public class ejecutableMain {
	
	/**
	 * Creamos el scanner 
	 */
	static Scanner sc = new Scanner(System.in);
	
	static final String NOMBRE_FICHERO = "partidas.txt";


	private static Personajes createEmployee(Scanner sc, String id) {

		var temAgente = CustomInput.requestInput(sc, "agente : ");
		int temMuertes = CustomInput.requestInteger(sc, "deaths: ");

		int tempAsesinatos;
		do {
			tempAsesinatos = CustomInput.requestInteger(sc, "kills: ");
		} while (tempAsesinatos <= 0);

		double tempMedia;
		do {
			tempMedia = CustomInput.requestInteger(sc, "kda: ");
		} while (tempMedia <= 0);

		return new Personajes(id, temAgente, temMuertes, tempAsesinatos, tempMedia);
	}

	
	/**
	 * Lo que queremos que se muestre por pantalla
	 */
	public static void SalidaPorPantalla() {
		
		System.out.println("\n-------- GESTIÓN DE EMPLEADOS --------");
		System.out.println("1. Añadir empleado");
		System.out.println("2. Listar empleados");
		System.out.println("3. Buscar empleado por DNI");
		System.out.println("4. Calcular sueldo medio");
		System.out.println("5. Listar Empleados por Salarios");
		System.out.println("6. Búsqueda por nombre y apellidos");
		System.out.println("7. Modificar Empleado");
		System.out.println("8. Eliminar Empleado"); // Añadido
	    System.out.println("9. Generar Informe Económico"); // Añadido
	    System.out.println("10. Salir"); // Corregido
	}
	
	/**
	 * Validamos si el el numero que se pide x pantalla es correcto
	 * @return Lo que tendremos que hacer 
	 */
	public static int Verificar() {
		int realizar = 0;// Numero que se solicitara x pantalla
		boolean entradaValida = false; // controlamos con un boolean si es verdadero o no
		do { //do while para que almenos entre una vez 
			System.out.print("Selecciona una opción: ");
			String entrada = sc.nextLine(); //leeemos la linea completa
			try {
				realizar = Integer.parseInt(entrada); // parseamos para veficar que sea un numero y no una letra
				if (realizar >= 1 && realizar <= 10) {
					entradaValida = true; // si es un numero entre el 1 y 7 y no se una letra que salga
				} else {
					System.out.println("Error: Introduce un valor entre 1 y 10.");//error
				}
			} catch (NumberFormatException e) {
				System.err.println("Error: ¡Debes introducir un número!");//error
			}
		} while (!entradaValida);
		return realizar;
	}
	
	/**
	 * Verifica la existencia del fichero de datos. Si no existe, lo crea vacío para
	 * evitar errores de lectura.
	 */
	public static void inicializarArchivo() {
		File f = new File(NOMBRE_FICHERO);
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
	
	public static void Añadirpartida() {
		
	}
	
	public static void ListarTodasLasPartidas() {
		
	}
	
	public static void CargarPartidasDeFichero() {
		
	}
	
	public static void GenerarInformeDetallado() {
		
	}

		public static void main(String[] args) {

			inicializarArchivo();
			int opcion;
			do {
				SalidaPorPantalla();
				opcion = Verificar(); // Obtenemos la opción (1-5)

				switch (opcion) {
				case 1:
					Añadirpartida();
					break;
				case 2:
					ListarTodasLasPartidas();
					break;
				case 3:
					CargarPartidasDeFichero();
					break;
				case 4:
					GenerarInformeDetallado();
					break;
				case 5:
					System.out.println("Cerrando la aplicación :C ");
					break;
				}
			} while (opcion != 5);
		}

}
