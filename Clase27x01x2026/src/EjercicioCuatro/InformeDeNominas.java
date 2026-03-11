package EjercicioCuatro;

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
	 * Nombres de cada trabjador
	 */
	String nombre;

	/**
	 * Sueldo base de cada trabajdor
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
	 * Categoria en la que pertece el trabajador
	 */
	char categoria;

	/**
	 * Contructos para asignar los 4 valores 
	 * @param id de los trabajadores
	 * @param nombre de cada uno de los trabajadores
	 * @param sueldoBase que recibe cada trabajador 
	 * @param horasExtra que ha realizado cada trabajor
	 */
    public Empleado(int id, String nombre, double sueldoBase, int horasExtra) {
        this.nombre = nombre;
        this.sueldoBase = sueldoBase;
        this.horasExtra = horasExtra;
        this.id = id;
        //para calcular directamente el sueldo y asiganar las categorias 
        this.calcularSueldo();
    }

    private void calcularSueldo() {
        // Sueldo total = base + (horas * 25€)
        this.sueldoTotal = this.sueldoBase + (this.horasExtra * 25);

        // Asignación de categorías
        if (this.sueldoTotal > 3000) {
            this.categoria = 'A'; // A (>3000€),
        } else if (this.sueldoTotal >= 2000) {
            this.categoria = 'B'; // B (2000-3000€)
        } else {
            this.categoria = 'C'; // C (<2000€)
        }
    }
}


/**
 * <body bgcolor="#222"> <font color="white">
 * <h1>Ejercicio 4: salario empleados</h1>
 * 
 * <p>
 * Desarrolla un programa en Java que lea datos de empleados desde CSV y genere
 * un informe de nóminas.
 * </p>
 * 
 * <p>
 * Lee "empleados.csv" con formato: id,nombre,sueldoBase,horasExtra (ej:
 * "001,Juan Pérez,2000,15").
 * </p>
 * 
 * <h2>Cálculos:</h2>
 * <ul>
 * <li>Sueldo total = sueldoBase + (horasExtra × 25€)</li>
 * <li>Categoría: A (>3000€), B (2000-3000€), C (<2000€)</li>
 * <li>Empleado mejor pagado por categoría</li>
 * <li>Total nómina por categoría</li>
 * </ul>
 * 
 * <h2>Reglas y Validaciones:</h2>
 * <ol>
 * <li><strong>Ordenación:</strong> Se ha de mostrar ordenado por id.</li>
 * <li><strong>Formato:</strong> Si una línea tiene más o menos campos de los
 * solicitados, se descarta.</li>
 * <li><strong>Duplicados:</strong> Si hay un trabajador repetido, sólo se
 * considera la primera aparición.</li>
 * <li><strong>Límite:</strong> Si se alcanza el máximo de trabajadores, parar
 * de leer y mostrar lo que se tenga.</li>
 * <li><strong>Numéricos:</strong> Si un campo que debe ser número no lo es, se
 * descarta la línea.</li>
 * </ol>
 * 
 * <h2>Salida:</h2>
 * <p>
 * Escribe los resultados en: <strong>nominas.txt</strong>
 * </p>
 * </font> </body>
 */
public class InformeDeNominas {
	/**
	 * Cantidad maxima de empleados que puede leer el Archivio
	 */
    static final int MAX_EMPLEADOS = 150;
    //creamos :D
    static Empleado[] lista = new Empleado[MAX_EMPLEADOS];
    /**
     * Contador que vamos a usar para saber cuantas lineas vamos a usar
     */
    static int contador = 0;

    /**
     * Vamos a leer el archivo que deseamos 
     * @param Le vamos a pasar el archivo que queremos leer
     */
    public static void leerCSV(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null && contador < MAX_EMPLEADOS) {//le indicamos que no pueda leer mas de 150 
                String[] datos = linea.split(",");//split para separarlo 

                // Validamos que tenga al menos los 4 campos principales de tu imagen
                if (datos.length < 4) continue;
                
                //un contrador por si peta al comento de hacer el parseo 
                try {
                    int id = Integer.parseInt(datos[0].trim());
                    String nombre = datos[1].trim();
                    double sueldoB = Double.parseDouble(datos[2].trim());
                    int horasE = Integer.parseInt(datos[3].trim());

                    // controlado que el sueldo o las horas sean negativo por logica 
                    if (sueldoB > 0 && horasE >= 0) {
                        lista[contador++] = new Empleado(id, nombre, sueldoB, horasE);
                    }
                } catch (NumberFormatException e) {
                    // Si el sueldo tiene letras que no explote xd
                }
            }
        } catch (IOException e) { //en en el peor de los casos 
            System.out.println("Error al leer el archivo.");
        }
    }
    
    /**
     * Vamos a imprimir lo que queremos que salga por pantalla
     * @param vamos a a pasarle el texto csv que queremos 
     */
    public static void generarReporte(String ruta) {
    	// le añadi un try por el hecho que nose por que explotaba el programa y me di cuenta que era aca
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            pw.println("REPORTE DE NÓMINAS");
            pw.println("==================");
            
            pw.printf("CATEGORIA A (>3000€): \n");
            pw.printf("CATEGORIA B (2000-3000€): \n");
            pw.printf("CATEGORIA C (<2000€): \n");
            pw.println("");
            pw.println("");
            double granTotal = 0;
            
            // Añadimos la columna ID al formato
            pw.printf("%-5s | %-20s | %-12s | %-8s | %-12s | %-4s%n", 
                      "ID", "Nombre", "Sueldo Base", "Horas", "Total", "Cat");
            pw.println("-----------------------------------------------------------------------");

            for (int i = 0; i < contador; i++) {
                Empleado e = lista[i];
                pw.printf("%03d   | %-20.20s | %12.2f | %8d | %12.2f | %-4c%n", 
                          e.id, e.nombre, e.sueldoBase, e.horasExtra, e.sueldoTotal, e.categoria);
                granTotal += e.sueldoTotal;
            }

            pw.println("-----------------------------------------------------------------------");
            pw.printf("COSTES TOTALES: %.2f€%n", granTotal);

        } catch (IOException e) {
            System.out.println("Error al escribir el reporte.");//saber donde estaba petando
        }
    }
    /**
     * Vamos a hacer el llamado de las funciones 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("procesando xd ...");
        
        leerCSV("empleados.csv");
        generarReporte("nominas.txt");
        
        System.out.println("Proceso finalizado :D");
        System.out.println("Se encontro: " + contador + " empleados.");
    }
}