package Bancos;
//importamos lo que va a hacer necesario 
import java.io.*;
import java.util.Scanner;

public class Proyecto {
	/**
	 * Creamos el scanner 
	 */
	static Scanner sc = new Scanner(System.in);
	
	/**
	 * Creamos un final para el fichero para que si en caso queramos modificarlo solo lo hagamos aca
	 */
	static final String NOMBRE_FICHERO = "empleados.txt";
	
	 
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
	 *  Opción 1: Pide datos y los guarda al final del archivo
	 */
	public static void aniadirEmpleado() {

		// Hacemos llamado a los metodos para verificar el los campos
		
		String dni = leerDni(); //metodo para verificar que tenga 8 numeros y 1 letra
		String nombre = leerTextoSeguro("Nombre: "); //llamos al metodo para que no meta comas o signos que puedan romperlo
		String apellidos = leerTextoSeguro("Apellidos: ");//llamos al metodo para que no meta comas o signos que puedan romperlo
		
		int edad = leerEdad();//llamamos al metodo para que meta una edad correcta 
		double sueldo = leerSueldo();//Sueldo > 0 para que teng 

		// El true en FileWriter es para activar el modo 'append' (añadir al final)
		// new FileWriter(NOMBRE_FICHERO, true) ejemplo
		try (PrintWriter pw = new PrintWriter(new FileWriter(NOMBRE_FICHERO, true))) {
			pw.println(dni + ";" + nombre + ";" + apellidos + ";" + edad + ";" + sueldo);
			System.out.println("Empleado guardado correctamente.");
		} catch (IOException e) {
			System.out.println("Error al escribir en el fichero: " + e.getMessage());
		}
	}
	
	public static boolean validarLetraDNI(String dni) {
	    // 1. Extraemos los números (primeros 8 caracteres) y la letra (el último)
		int numero = Integer.parseInt(dni.replaceAll("[^0-9]", ""));
	    char letraDada = dni.charAt(8);

	    // 2. Cadena de control de letras (orden oficial del Ministerio del Interior)
	    String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
	    
	    // 3. Calculamos la letra que le corresponde
	    int indice = numero % 23;
	    char letraCorrecta = letras.charAt(indice);

	    // 4. Retornamos si coinciden
	    return letraDada == letraCorrecta;
	}
	
	/**
	 * Comprovamos que el dni que se pide sea correcto 
	 * @return
	 */
	public static String leerDni() {
		/**
		 * 	! Es el operador "NOT". La condición se cumple si el texto NO coincide con el patrón.
			^	Indica el inicio de la cadena. Nada puede haber antes.
			[0-9]	Significa que el carácter debe ser un dígito del 0 al 9.
			{8}	Indica que el elemento anterior se repite exactamente 8 veces.
			[A-Z]	El último carácter debe ser una letra mayúscula (A-Z).
			$	Indica el final de la cadena. Nada puede haber después.
		 */
		
		String dni;
	    while (true) {
	        System.out.print("DNI (8 números y 1 letra): ");
	        dni = sc.nextLine().trim().toUpperCase();

	        // PASO 1: Validar formato básico con tu Regex
	        if (!dni.matches("^[P]{1}[0-9]{3}$")) {
	            System.err.println("Error: Formato inválido (ej: 12345678Z).");
	        } 
	        // PASO 2: Validar la MATEMÁTICA de la letra (Lo nuevo)
	        else if (!validarLetraDNI(dni)) {
	            System.err.println("Error: La letra no corresponde al número de DNI introducido.");
	        }
	        // PASO 3: Validar que no esté repetido en el archivo
	        else if (existeDni(dni)) {
	            System.err.println("Error: Este DNI ya está registrado."); 
	        } 
	        else {
	            return dni; // Si pasa todo, el DNI es perfecto
	        }
	    }
	}
	
	
	/**
	 * Verificamos si el Dni existe o no
	 * 
	 * @param dni le pasamos de la lista del fichero
	 * @return
	 */
	public static boolean existeDni(String dni) {
		/**
		 * ¿Existe el archivo? → No: false / Sí: Continuar.
		 * Leer línea. 
		 * ¿Es el DNI que busco? → Sí: true (Fin) / No: Volver al paso 2.
		 *  ¿Se acabó el archivo? → Sí: false (Fin).
		 */
		File f = new File(NOMBRE_FICHERO);
		if (!f.exists())//compramos si existe 
			return false;

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String linea;//leeamos cada linea
			while ((linea = br.readLine()) != null) {
				if (linea.trim().isEmpty())//  para omitir lineas vacias o espaciso
					continue; // Salta líneas vacías
				String[] datos = linea.split(";");
				// (equalsIgnoreCase) hace que 12345678z y 12345678Z se consideren iguales
				// (ignora si es mayúscula o minúscula).
				if (datos[0].equalsIgnoreCase(dni)) {
					return true; // Encontrado
				}
			}
		} catch (IOException e) {
			return false;
		}
		return false;
	}

	/**
	 * Método para validar la edad que sea cooerente
	 * @return Edad correcta 
	 */
	public static int leerEdad() {
		while (true) { //creamos un bucle para que meta la edad hasta que se correcta 
			try {
				System.out.print("Edad: ");
				int edad = Integer.parseInt(sc.nextLine());//hacemos el parceo para verificar si no mete alguna letra 
				if (edad >= 18 && edad <= 99) //verificamos que la edad sea logica
					return edad; //devolvemos la edad si es valida 
				System.out.println("Error: La edad debe estar entre 18 y 99.");
			} catch (NumberFormatException e) {
				System.out.println("Error: Introduce un número entero.");
			}
		}
	}

	/**
	 *  Método para validar el sueldo si no mete alguna coma de mas 
	 * @return devuelve el suelso valido
	 */
	public static double leerSueldo() {
		while (true) { //creamos un bucle para que meta el sueldo hasta que se correcta
			try {
				System.out.print("Sueldo: ");
				double sueldo = Double.parseDouble(sc.nextLine().replace(',', '.'));//hacemos un parseo para verificar 
				if (sueldo >= 0)//mayor que 0 para que sea logico la comprobacion
					return sueldo;
				System.out.println("Error: El sueldo no puede ser negativo.");
			} catch (NumberFormatException e) {
				System.out.println("Error: Formato de sueldo no válido.");
			}
		}
	}

	/**
	 * Opción 2: Lee el archivo línea por línea y lo muestra
	 */
	public static void listarEmpleados() {
		File f = new File(NOMBRE_FICHERO);//lemos el fichero entero 
		if (!f.exists()) {//verificamos si el fichero existe 
			System.out.println("El fichero no existe todavía.");
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String linea;//leemos linea x linea 
			System.out.println("\n--- LISTA DE EMPLEADOS ---");
			while ((linea = br.readLine()) != null) {
				System.out.println(linea);//mostramos x pantalla todo lo que contiene
			}
		} catch (IOException e) {
			System.out.println("Error al leer el fichero.");
		}
	}

	/**
	 *  Opción 3: Busca una línea que empiece por el DNI indicado
	 */
	public static void buscarPorDni() {
		System.out.print("Introduce el DNI a buscar: ");
		String dniBusqueda = sc.nextLine().trim();
		boolean encontrado = false;

		File f = new File(NOMBRE_FICHERO);
		if (!f.exists()) {
			System.out.println("El fichero no existe.");
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				if (linea.trim().isEmpty())
					continue;

				String[] datos = linea.split(";");

				// PROTECCIÓN: Verificar que al menos existe el campo DNI (índice 0)
				if (datos.length > 0 && datos[0].equalsIgnoreCase(dniBusqueda)) {
					System.out.println("Empleado encontrado: " + linea.replace(";", " | "));
					encontrado = true;
					break;
				}
			}
			if (!encontrado)
				System.out.println("DNI no encontrado.");
		} catch (IOException e) {
			System.out.println("Error al leer el archivo.");
		}
	}

	public static void calcularSueldoMedio() {
		double sumaSueldos = 0;
		int contador = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				linea = linea.trim();
				if (linea.isEmpty())
					continue;

				String[] datos = linea.split(";");

				// PROTECCIÓN:D: Si la línea no tiene 5 partes, la ignora en vez de romperse
				if (datos.length < 5) {
					System.err.println("Advertencia: Línea corrupta ignorada -> " + linea);
					continue;
				}

				try {
					// El sueldo es la posición 4
					sumaSueldos += Double.parseDouble(datos[4]);
					contador++;
				} catch (NumberFormatException e) {
					System.err.println("Error: Formato de sueldo inválido en línea -> " + linea);
				}
			}

			if (contador > 0) {
				double media = sumaSueldos / contador;
				System.out.printf("El sueldo medio de los %d empleados es: %.2f\n", contador, media);
			} else {
				System.out.println("No hay datos válidos para calcular la media.");
			}
		} catch (IOException e) {
			System.out.println("Error: No se pudo acceder al fichero.");
		}
	}

	/**
	 * Método para limpiar el texto de caracteres que rompen el CSV
	 * @param etiqueta
	 * @return comprueba que sea coorecto la palabra
	 */
	public static String leerTextoSeguro(String etiqueta) {
		String texto;
		while (true) {
			System.out.print(etiqueta + ": ");
			texto = sc.nextLine().trim();
			if (texto.contains(";")) {
				System.out.println("Error: No se permite el uso del carácter ';' (punto y coma).");
			} else if (texto.isEmpty()) {
				System.out.println("Error: El campo no puede estar vacío.");
			} else {
				return texto;
			}
		}
	}

	
	public static void ListaPorSueldo() {
		// Quiero ver toda la lista por el sueldo que tiene el fichero (leer)
		// necesito pedir por escaner dos cantidad
		// -validar que no sea un numero negativo
		// -validar que el suedo A y el sueldo B no sean iguales
		// -validar que el sueldo A y B no tenga fallas por ; o por ,
		// -validar que sueldo A sea mamenor que sueldo B
		// Necesito hacer una funcion que contenga si entre sueldo A y B
		// Mostrar por pantalla que los sueldos que contengan entre A y B
		// Nota: Nose si se necesaria lanzar todos los datos completos pero por siacaso
		// si

		System.out.println("\n--- FILTRAR POR RANGO DE SUELDO ---");

		// Reutilizamos leerSueldo() que ya valida números positivos y comas/puntos
		System.out.println("Introduce el primer límite:");
		double s1 = leerSueldo();
		System.out.println("Introduce el segundo límite:");
		double s2 = leerSueldo();

		if (s1 == s2) {
			System.out.println("Error: Los sueldos no pueden ser iguales para un rango.");
			return;
		}

		// Aseguramos que 'min' sea el menor y 'max' el mayor
		double min = Math.min(s1, s2);
		double max = Math.max(s1, s2);

		boolean encontrado = false;
		try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
			String linea;
			System.out.printf("Empleados con sueldo entre %.2f y %.2f:\n", min, max);

			while ((linea = br.readLine()) != null) {
				String[] datos = linea.split(";");
				if (datos.length >= 5) {
					double sueldoEmp = Double.parseDouble(datos[4]);
					if (sueldoEmp >= min && sueldoEmp <= max) {
						System.out.println(linea.replace(";", " | "));
						encontrado = true;
					}
				}
			}
			if (!encontrado)
				System.out.println("No hay empleados en ese rango de sueldo.");

		} catch (IOException e) {
			System.out.println("Error al leer el archivo.");
		}
	}

	
	public static void BusquedaPorNombreyApelldio() {
		// Quiero ver toda la lista por el sueldo que tiene el fichero (leer)
		// Pedir por patantalla los nombre y apellidos (creo que seria mejor separarlos)
		// verificar que sea carater de nombre y no sea numero o algo raro junto con un
		// try(do-whiles)
		// necesito validar 2 cosas
		// -validar nombre (tiene que ser con los mismo caracteres con mayuscula y
		// minuscula)
		// -Validar que si en caso no lo haga separado hay una separacion de por medio
		// como un ";"
		// -creo que necesito almacenar los nombres para luego llamarlos tipo en un
		// array (duda)
		// -Mostrar por pantalla que los nombres que contenga a pesar que tengas otros
		// apelldidos
		// -Mostrar de igual manera los apellidos
		// NOTA:considero mejor que podria trasformar toda la palabra en minuscula para
		// que no hay aproblemas grande.

		System.out.println("\n--- BÚSQUEDA POR NOMBRE Y APELLIDOS ---");

		// Usamos leerTextoSeguro para evitar el ';' y campos vacíos
		String nomBusqueda = leerTextoSeguro("Introduce el nombre o apellido a buscar").toLowerCase();

		boolean encontrado = false;
		File f = new File(NOMBRE_FICHERO);
		if (!f.exists())
			return;

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				if (linea.trim().isEmpty())
					continue;

				String[] datos = linea.split(";");
				if (datos.length >= 3) {
					// Pasamos a minúsculas para que no importe si escribió "JUAN" o "juan"
					String nombreCompleto = (datos[1] + " " + datos[2]).toLowerCase();
					// cadena.contains(busqueda) ejmplo de como se usa
					if (nombreCompleto.contains(nomBusqueda)) {
						System.out.println("Coincidencia encontrada: " + linea.replace(";", " | "));
						encontrado = true;
					}
				}
			}
			if (!encontrado)
				System.out.println("No se encontraron empleados que coincidan con: " + nomBusqueda);

		} catch (IOException e) {
			System.out.println("Error al procesar la búsqueda.");
		}
	}
	
	
    //terminar para la siguinete clase
	public static void ModificarEmpleados () {
		//------------Systen.err.print(rojito)para mostrar errores---------------
		//Pedimos el Dni para modificar los datos del empleado
		//pedimos el salario que vamos a modificar 
		//validar que el DNI es valido y que lo escriba bien (le pasamos una funcion)
		//validar que el salario sea correcto (le pasamos una funcion)
		//creamos el nuevo fichero vacio temporarl
		//en un principio tenia pensando modificar toda la linea pero pensaba usar uno para modificar el que gustes 
		//pero en un princio solo modificare el salario que le vamos a pedir escaner
		//tenemos que hacer 2 ficheros los cuales los cuales uno va a estar vacio y el otro de los datos
		///en el cual en el vacio tenemos que pasarle los que no coincidad con lo que vamos a pedirle con el scanner
		// y vamos a pasar todas las linea que no coincidan con el Dni y le vamos a modificar al que coinciden con el dni
		// una vez que hallamos hecho todo vamos a eliminar el primer fichero lleno 
		// y vamos a modificar el segundo fichero(donde esta pasado modificado el nuevo salario) y vamos a cambiarle de nombre por el fichero principal
		// validar primeramente que exista los 2 ficheros
		System.out.println("\n--- MODIFICAR SALARIO DE EMPLEADO ---");

	    // 1. Pedir DNI y validar que exista
	    System.out.print("Introduce el DNI del empleado: ");
	    String dniBusqueda = sc.nextLine().trim().toUpperCase();
		
		if (!existeDni(dniBusqueda)) {
	        System.err.println("Error: El DNI " + dniBusqueda + " no existe en el sistema.");
	        return; // Salimos si no existe
	    }

	    // 2. Pedir el nuevo salario usando tu método validado
	    System.out.println("DNI encontrado. Procedamos al cambio.");
	    double nuevoSueldo = leerSueldo();

	    // 3. Definir los archivos
	    File fOriginal = new File(NOMBRE_FICHERO);
	    File fTemporal = new File("temp_" + NOMBRE_FICHERO);

	    // 4. Proceso de copia y modificación
	    // Usamos try-with-resources para que se cierren solos al terminar
	    try (BufferedReader br = new BufferedReader(new FileReader(fOriginal));
	         PrintWriter pw = new PrintWriter(new FileWriter(fTemporal))) {

	        String linea;
	        while ((linea = br.readLine()) != null) {
	            if (linea.trim().isEmpty()) continue;

	            String[] datos = linea.split(";");
	         // EL ESCUDO: Si la línea no tiene exactamente 5 campos, la ignoramos o avisamos
	            if (datos.length != 5) {
	                System.err.println("Saltando línea corrupta: " + linea);
	                pw.println(linea); // La copiamos tal cual para no perder datos, pero no la tocamos
	                continue; 
	            }
	            // Si el DNI coincide, escribimos la línea con el sueldo nuevo
	            if (datos[0].equalsIgnoreCase(dniBusqueda)) {
	                // Estructura: DNI(0);Nombre(1);Apellidos(2);Edad(3);Sueldo(4)
	                pw.println(datos[0] + ";" + datos[1] + ";" + datos[2] + ";" + datos[3] + ";" + nuevoSueldo);
	            } else {
	                // Si no coincide, copiamos la línea tal cual
	                pw.println(linea);
	            }
	        }

	    } catch (IOException e) {
	        System.err.println("Error durante la operación: " + e.getMessage());
	        return;
	    }

	 // 5. Sustitución de archivos con limpieza automática
	    if (fOriginal.delete()) {
	        if (fTemporal.renameTo(fOriginal)) {
	            System.out.println("¡Empleado modificado con éxito!");
	        } else {
	            System.err.println("Error: No se pudo renombrar el fichero temporal.");
	            // Limpiamos el temporal si no pudimos renombrarlo
	            if (fTemporal.exists()) fTemporal.delete(); 
	        }
	    } else {
	        System.err.println("Error: No se pudo eliminar el fichero original.");
	        // Si no pudimos borrar el original, el temporal no sirve de nada, lo borramos
	        if (fTemporal.exists()) fTemporal.delete(); 
	    }
		
	}
	
	public static void eliminarEmpleado() {
	    System.out.println("\n--- ELIMINAR EMPLEADO ---");
	    System.out.print("Introduce el DNI del empleado a eliminar: ");
	    String dniEliminar = sc.nextLine().trim().toUpperCase();

	    if (!existeDni(dniEliminar)) {
	        System.err.println("Error: No se encontró al empleado con DNI: " + dniEliminar);
	        return;
	    }

	    File fOriginal = new File(NOMBRE_FICHERO);
	    File fTemporal = new File("temp_" + NOMBRE_FICHERO);

	    try (BufferedReader br = new BufferedReader(new FileReader(fOriginal));
	         PrintWriter pw = new PrintWriter(new FileWriter(fTemporal))) {

	        String linea;
	        while ((linea = br.readLine()) != null) {
	            String[] datos = linea.split(";");
	            // Solo escribimos en el temporal si el DNI NO coincide
	            if (datos.length > 0 && !datos[0].equalsIgnoreCase(dniEliminar)) {
	                pw.println(linea);
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Error al procesar el archivo: " + e.getMessage());
	        return;
	    }

	    if (fOriginal.delete() && fTemporal.renameTo(fOriginal)) {
	        System.out.println("Empleado eliminado satisfactoriamente.");
	     // Dentro de eliminarEmpleado, en el bloque else final:	
	    } else {
	        System.err.println("Error crítico al actualizar el fichero.");
	        if (fTemporal.exists()) fTemporal.delete(); // Limpieza de seguridad
	    }
	}
	
	public static void generarInformeEconomico() {
	    String ficheroInforme = "análisis_Salarial.txt";
	    
	    // Variables para promedios
	    double sumaIni = 0, sumaMed = 0, sumaSen = 0;
	    int contIni = 0, contMed = 0, contSen = 0;

	    try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO));
	         PrintWriter pw = new PrintWriter(new FileWriter(ficheroInforme))) {

	        pw.println("========== INFORME ECONÓMICO DE LA EMPRESA ==========");
	        pw.println("NOMBRE - APELLIDO - SUELDO - SEGUROS SOCIALES");
	        pw.println("-----------------------------------------------------");

	        String linea;
	        while ((linea = br.readLine()) != null) {
	            String[] d = linea.split(";");
	            if (d.length < 5) continue;

	            String nombre = d[1];
	            String apellido = d[2];
	            int edad = Integer.parseInt(d[3]);
	            double sueldo = Double.parseDouble(d[4]);
	            double ss = 0;

	            // Lógica por rangos de edad
	            if (edad >= 18 && edad <= 25) {
	                ss = sueldo * 0.12;
	                sumaIni += sueldo;
	                contIni++;
	            } else if (edad >= 26 && edad <= 45) {
	                ss = sueldo * 0.18;
	                sumaMed += sueldo;
	                contMed++;
	            } else { // 46 en adelante
	                ss = sueldo * 0.23;
	                sumaSen += sueldo;
	                contSen++;
	            }

	            pw.printf("%s - %s - %.2f€ - %.2f€\n", nombre, apellido, sueldo, ss);
	        }

	        // Sección de sueldos medios
	        pw.println("\n--- SUELDOS MEDIOS POR RANGO ---");
	        pw.printf("Inicial (18-25): %.2f€\n", (contIni > 0 ? sumaIni / contIni : 0));
	        pw.printf("Medio (26-45): %.2f€\n", (contMed > 0 ? sumaMed / contMed : 0));
	        pw.printf("Senior (46+): %.2f€\n", (contSen > 0 ? sumaSen / contSen : 0));

	        System.out.println("Informe generado con éxito en: " + ficheroInforme);
	        //syso ctrol+espacio

	    } catch (IOException e) {
	        System.err.println("Error al generar el informe: " + e.getMessage());
	    }
	}
		
	/**
	 * Main de toda la vida
	 * 
	 * @param GAAAAA
	 */
	public static void main(String[] args) {
		
		inicializarArchivo();
		int opcion;
		do {
			SalidaPorPantalla();
			opcion = Verificar(); // Obtenemos la opción (1-5)

			switch (opcion) {
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
				ListaPorSueldo();
				break;
			case 6:
				BusquedaPorNombreyApelldio();
				break;
			case 7:
				ModificarEmpleados();
				break;
			case 8:
				eliminarEmpleado();
				break;
			case 9:
				generarInformeEconomico();
				break;
			case 10:
				System.out.println("Cerrando la aplicación :C ");
				break;
			}
		} while (opcion != 10);
	}
	
	public static void listarOrdenado() {
	    // Usamos un ArrayList para cargar las líneas
	    java.util.ArrayList<String> lista = new java.util.ArrayList<>();
	    try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_FICHERO))) {
	        String linea;
	        while ((linea = br.readLine()) != null) {
	            lista.add(linea);
	        }
	        // Ordenamos basándonos en el DNI (que es el primer campo datos[0])
	        lista.sort((a, b) -> a.split(";")[0].compareTo(b.split(";")[0]));
	        
	        System.out.println("\n--- LISTA ORDENADA POR DNI ---");
	        for (String l : lista) {
	            System.out.println(l.replace(";", " | "));
	        }
	    } catch (IOException e) {
	        System.err.println("Error al ordenar.");
	    }
	}
}

//Reemplaza tus split en los métodos de lectura por esto:
//String[] datos = linea.split(";", -1); // El -1 evita que ignore campos vacíos al final

     
