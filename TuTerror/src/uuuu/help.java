package uuuu;

//IMPORTACIONES: Traemos las herramientas necesarias del API de Java [cite: 233, 259, 901, 902]
import java.io.*; // Importa todo lo de Entrada/Salida (File, Readers, Writers, etc.) [cite: 233, 902]
import java.util.Scanner; // Para capturar lo que escribas en el teclado [cite: 301, 304]

public class help {

	// Definimos el Scanner como 'static' para que todos los métodos puedan usarlo
	// sin crearlo otra vez
	static Scanner sc = new Scanner(System.in);

	// Constante 'final' para el nombre del archivo. Si cambia el nombre, solo lo
	// tocas aquí una vez [cite: 9]
	static final String NOMBRE_FICHERO = "empleados.txt";

	/**
	 * MÉTODO: INICIALIZAR ARCHIVO Propósito: Asegurar que el archivo existe antes
	 * de intentar leerlo [cite: 245]
	 */
	public static void inicializarArchivo() {
		File f = new File(NOMBRE_FICHERO); // Creamos un objeto File que apunta al archivo físico
		try {
			if (!f.exists()) { // Si el archivo NO existe en la carpeta del proyecto
				if (f.createNewFile()) { // Intentamos crearlo físicamente en el disco
					System.out.println(">> Sistema: Archivo 'empleados.txt' creado.");
				}
			}
		} catch (IOException e) { // Capturamos errores de disco (por ejemplo, si no hay espacio) [cite: 252, 280]
			System.err.println("Error crítico: No se tiene permisos para crear el archivo.");
		}
	}

	/**
	 * MÉTODO: VERIFICAR OPCIÓN (EL FILTRO DEL MENÚ) Propósito: Evitar que el
	 * programa "explote" si metes una letra en vez de un número
	 */
	public static int Verificar() {
		int realizar = 0; // Variable para guardar el número de la opción elegida
		boolean entradaValida = false; // Controla que el bucle siga hasta que el número sea correcto
		do {
			System.out.print("Selecciona una opción: ");
			String entrada = sc.nextLine(); // Leemos la línea completa como texto para que no queden restos en el
											// buffer
			try {
				realizar = Integer.parseInt(entrada); // Intentamos convertir el texto a un número entero [cite: 99,
														// 111]
				if (realizar >= 1 && realizar <= 10) { // Si el número está en el rango del menú...
					entradaValida = true; // ...la entrada es perfecta y salimos del bucle
				} else {
					System.out.println("Error: Introduce un valor entre 1 y 10.");
				}
			} catch (NumberFormatException e) { // Si el usuario escribió una letra, 'parseInt' lanza este error [cite:
												// 111]
				System.err.println("Error: ¡Debes introducir un número!");
			}
		} while (!entradaValida); // Se repite mientras 'entradaValida' sea false
		return realizar; // Devolvemos el número validado al menú principal
	}

	/**
	 * MÉTODO: AÑADIR EMPLEADO (MODO APPEND) Propósito: Guardar datos nuevos al
	 * final del archivo sin borrar lo anterior [cite: 544, 562]
	 */
	public static void aniadirEmpleado() {
		// Pedimos cada dato usando métodos que ya los validan (leerDni, leerSueldo,
		// etc.)
		String dni = leerDni();
		String nombre = leerTextoSeguro("Nombre");
		String apellidos = leerTextoSeguro("Apellidos");
		int edad = leerEdad();
		double sueldo = leerSueldo();

		// El 'true' en FileWriter es VITAL: activa el modo ANEXAR (añadir al final)
		// [cite: 562, 571]
		// PrintWriter es más cómodo porque permite usar println() igual que en la
		// consola [cite: 356, 358]
		try (PrintWriter pw = new PrintWriter(new FileWriter(NOMBRE_FICHERO, true))) {
			// Escribimos los datos en una sola línea separados por punto y coma (Formato
			// CSV)
			pw.println(dni + ";" + nombre + ";" + apellidos + ";" + edad + ";" + sueldo);
			System.out.println("Empleado guardado correctamente.");
		} catch (IOException e) {
			System.out.println("Error al escribir en el fichero: " + e.getMessage());
		}
	}

	/**
	 * MÉTODO: EXISTE DNI Propósito: Lectura secuencial para buscar si un DNI ya
	 * está registrado [cite: 250, 334]
	 */
	public static boolean existeDni(String dni) {
		File f = new File(NOMBRE_FICHERO);
		if (!f.exists())
			return false; // Si ni siquiera hay archivo, es imposible que el DNI exista

		// BufferedReader es la forma eficiente de leer líneas completas de un archivo
		// [cite: 264]
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String linea;
			// Bucle que lee el archivo hasta que no queden líneas (null) [cite: 275, 309]
			while ((linea = br.readLine()) != null) {
				if (linea.trim().isEmpty())
					continue; // Si la línea está vacía, la saltamos

				String[] datos = linea.split(";"); // Troceamos la línea usando el ';' como cuchillo [cite: 148, 159]
				// Comparamos el primer trozo (DNI) con el que buscamos (ignora
				// mayúsculas/minúsculas) [cite: 58, 451]
				if (datos[0].equalsIgnoreCase(dni)) {
					return true; // Si lo encuentra, devuelve true y para de buscar
				}
			}
		} catch (IOException e) {
			return false;
		}
		return false; // Si llega aquí, es que recorrió todo el archivo y no lo encontró
	}

	/**
	 * MÉTODO: MODIFICAR EMPLEADO (EL PROCESO TEMPORAL) Propósito: Actualizar un
	 * dato específico [cite: 673, 674]
	 */
	public static void ModificarEmpleados() {
		System.out.println("\n--- MODIFICAR SALARIO ---");
		System.out.print("Introduce el DNI del empleado: ");
		String dniBusqueda = sc.nextLine().trim().toUpperCase();

		if (!existeDni(dniBusqueda)) { // Primero comprobamos que el empleado realmente exista
			System.err.println("Error: El DNI no existe.");
			return;
		}

		double nuevoSueldo = leerSueldo(); // Pedimos el nuevo salario validado
		File fOriginal = new File(NOMBRE_FICHERO);
		File fTemporal = new File("temp_empleados.txt"); // Creamos un archivo "espejo" temporal [cite: 681]

		// Abrimos el original para leer y el temporal para escribir [cite: 661]
		try (BufferedReader br = new BufferedReader(new FileReader(fOriginal));
				PrintWriter pw = new PrintWriter(new FileWriter(fTemporal))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				if (linea.trim().isEmpty())
					continue;
				String[] datos = linea.split(";");

				// Si esta línea coincide con el DNI, escribimos el nuevo sueldo en el archivo
				// temporal
				if (datos[0].equalsIgnoreCase(dniBusqueda)) {
					pw.println(datos[0] + ";" + datos[1] + ";" + datos[2] + ";" + datos[3] + ";" + nuevoSueldo);
				} else {
					// Si no es el empleado, copiamos la línea original tal cual al temporal [cite:
					// 674]
					pw.println(linea);
				}
			}
		} catch (IOException e) {
			System.err.println("Error durante la operación.");
			return;
		}

		// PASO FINAL: Intercambio de archivos [cite: 712]
		if (fOriginal.delete()) { // Borramos el archivo viejo desactualizado [cite: 583]
			fTemporal.renameTo(fOriginal); // Renombramos el temporal con el nombre del original [cite: 581]
			System.out.println("¡Empleado modificado con éxito!");
		}
	}

	/**
	 * MÉTODO: GENERAR INFORME ECONÓMICO (OPCIÓN 9) Propósito: Crear un nuevo
	 * archivo con cálculos matemáticos y formato [cite: 417, 753]
	 */
	public static void generarInformeEconomico() {
		String ficheroInforme = "análisis_Salarial.txt";
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO));
				PrintWriter pw = new PrintWriter(new FileWriter(ficheroInforme))) {

			pw.println("========== INFORME ECONÓMICO =========="); // Escribimos cabecera en el nuevo archivo [cite:
																	// 424]
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] d = linea.split(";");
				if (d.length < 5)
					continue; // Protección por si alguna línea está incompleta [cite: 461]

				int edad = Integer.parseInt(d[3]); // Convertimos edad a número [cite: 197]
				double sueldo = Double.parseDouble(d[4]); // Convertimos sueldo a número [cite: 104]
				double ss = (edad <= 25) ? sueldo * 0.12 : (edad <= 45) ? sueldo * 0.18 : sueldo * 0.23; // Lógica de SS
																											// [cite:
																											// 756]

				// printf permite usar %s para texto y %.2f para números con 2 decimales [cite:
				// 120, 126]
				pw.printf("%s - %s - %.2f€ - %.2f€\n", d[1], d[2], sueldo, ss);
			}
			System.out.println("Informe generado en: " + ficheroInforme);
		} catch (IOException e) {
			System.err.println("Error al generar el informe.");
		}
	}

	/**
	 * MÉTODO: LISTAR ORDENADO (BONUS) Propósito: Mostrar los datos ordenados
	 * alfabéticamente por DNI [cite: 460]
	 */
	public static void listarOrdenado() {
		java.util.ArrayList<String> lista = new java.util.ArrayList<>(); // Lista temporal en memoria
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String l;
			while ((l = br.readLine()) != null)
				lista.add(l); // Metemos todas las líneas en la lista

			// Ordenamos la lista usando el primer campo (DNI) como referencia [cite: 460]
			lista.sort((a, b) -> a.split(";")[0].compareTo(b.split(";")[0]));

			for (String l : lista)
				System.out.println(l.replace(";", " | "));
		} catch (IOException e) {
		}
	}
	
	
	
	
	//_------------------------------------------------------------------------------------------------
	
	/**
	 * FUNCIÓN: Leer texto sin espacios (Ideal para nombres de usuario o códigos)
	 * No permite que el usuario pulse espacio ni deje el campo vacío.
	 */
	public static String leerSinEspacios(String etiqueta) {
	    String texto;
	    while (true) {
	        System.out.print(etiqueta + ": ");
	        texto = sc.nextLine().trim(); // Quitamos espacios accidentales al inicio/final [cite: 25, 51]
	        
	        if (texto.isEmpty()) { // Verifica si tiene longitud 0 [cite: 25]
	            System.err.println("Error: El campo no puede estar vacío.");
	        } else if (texto.contains(" ")) { // Verifica si hay un espacio intermedio [cite: 25]
	            System.err.println("Error: No se permiten espacios en este campo.");
	        } else {
	            return texto; // Si todo está bien, sale del bucle y devuelve el texto
	        }
	    }
	}

	/**
	 * FUNCIÓN: Leer solo letras (Bloquea números y signos de puntuación)
	 * Usa una expresión regular (Regex) para validar.
	 */
	public static String leerSoloLetras(String etiqueta) {
	    String texto;
	    while (true) {
	        System.out.print(etiqueta + ": ");
	        texto = sc.nextLine().trim();
	        
	        // El Regex "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$" permite letras, tildes y la ñ
	        if (texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
	            return texto;
	        } else {
	            System.err.println("Error: Solo se permiten letras (sin números ni signos).");
	        }
	    }
	}

	/**
	 * FUNCIÓN: Leer texto seguro para CSV (Bloquea el punto y coma ';')
	 * Evita que el archivo se corrompa al usar split(";") más adelante[cite: 148, 195].
	 */
	public static String leerTextoSeguro(String etiqueta) {
	    String texto;
	    while (true) {
	        System.out.print(etiqueta + ": ");
	        texto = sc.nextLine().trim();
	        
	        if (texto.contains(";")) { // Buscamos el carácter prohibido [cite: 25]
	            System.err.println("Error: No puedes usar el carácter ';' porque rompe el archivo.");
	        } else if (texto.isEmpty()) {
	            System.err.println("Error: El campo es obligatorio.");
	        } else {
	            return texto;
	        }
	    }
	}
//---------------------------------------------------------------------------------------------------
	public static void aniadirEmpleado() {
	    // LLAMADAS A LAS FUNCIONES DE VALIDACIÓN
	  //  String dni = leerDni(); // Tu función de DNI
	    String nombre = leerSoloLetras("Nombre"); // Bloquea números
	    String apellidos = leerSoloLetras("Apellidos");
	    String departamento = leerSinEspacios("Código Departamento"); // Bloquea espacios
	  // double sueldo = leerSueldo(); // Tu función de sueldo

	    // ESCRITURA EN FICHERO [cite: 336, 562]
	    try (PrintWriter pw = new PrintWriter(new FileWriter(NOMBRE_FICHERO, true))) {
	        // Guardamos todo separado por ";"
	  //      pw.println(dni + ";" + nombre + ";" + apellidos + ";" + departamento + ";" + sueldo);
	        System.out.println(">> Empleado registrado correctamente.");
	    } catch (IOException e) {
	        System.err.println("Error al acceder al disco: " + e.getMessage());
	    }
	}
}



/**
 * Puntos clave para que mañana no te pillen:
¿Por qué split(";", -1)?
Si una línea termina en un punto y coma y no hay nada después (ej: 87654321B;Ana;López;32;), el split normal a veces ignora el último campo. El -1 obliga a Java a contar todos los trozos, aunque estén vacíos.
+1

¿Qué es el try-with-resources?
Es el try (recurso) { ... }. Su gran ventaja es que cierra el archivo automáticamente al terminar, incluso si ocurre un error. ¡Esto te ahorra poner el .close() a mano y evita que el archivo se quede bloqueado!.
+3

Diferencia entre equals y equalsIgnoreCase:

equals: El DNI "1234a" y "1234A" serían diferentes.


equalsIgnoreCase: El DNI "1234a" y "1234A" serían iguales. ¡Usa siempre este último para DNIs y nombres!
+1

Uso de printf:
Para informes, es tu mejor amigo. %s es para texto, %d para enteros y %.2f para que los sueldos no salgan con mil decimales horribles.
 */


/**
 * 1. Para Analizar (Saber qué hay dentro)

length(): Te dice cuántas letras tiene en total.


isEmpty(): Te dice si la cadena está vacía (longitud 0).


charAt(índice): Te da la letra que está en esa posición específica. Si pides la 0, te da la primera.
+1


indexOf("texto"): Busca dónde empieza esa palabra y te da el número. OJO: Si no la encuentra, te devuelve -1.
+3


contains("texto"): Solo te dice true o false si esa palabra está ahí dentro.

2. Para Modificar (Crear una versión nueva)

substring(inicio, fin): Corta un trozo. Trampa de examen: El número de fin no se incluye, corta justo antes.
+2


trim(): Borra los espacios sobrantes al principio y al final (como limpiar los bordes).
+1


concat("otro"): Une dos textos (aunque es más fácil usar el signo +).
+1


toUpperCase() / toLowerCase(): Lo pasa todo a MAYÚSCULAS o minúsculas.
+2

3. Para Comparar

equals(otroString): Compara si el contenido es idéntico.


¡Cuidado!: Distingue entre mayúsculas y minúsculas ("Hola" no es igual a "hola").

💡 El "Truco Final" para el examen:
Si mañana te piden sacar el nombre de un correo como kenji@gmail.com:

Buscas la posición del @ con indexOf("@").

Cortas desde el principio hasta esa posición con substring(0, posicion).
 */





 
