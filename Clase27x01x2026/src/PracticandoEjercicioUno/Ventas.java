package PracticandoEjercicioUno;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Ventas {
	
	static String[] producto = new String[50]; // Nombre de los productos
	static int[] cantidad = new int[50]; // Cantidad de cada producto
	static double[] precio = new double[50]; // Precio de cada producto
	static int indiceProducto = 0; // Con esto evitamos recorrer todas las posiciones del array producto
	static final int MAX_PRODUCTOS = 50; // Número máximo de productos
	
	/* Método para leer el fichero
	 * @param nombreFichero
	 */
	public static void leer(String nombreFichero) {
		int contadorLineas = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(nombreFichero));
			
			String linea = br.readLine(); // Línea actual
			
			
			System.out.println("*Contenido del fichero*");
			while (linea != null) {
				
				// Mostramos la línea actual
				System.out.println(linea);
				
				// Separamos los distintos elementos
				String[] productoActual = linea.split(",");
				
				// Variables temporales para trabajar con la línea actual
				String productoTemp = productoActual[0].trim();
				int cantidadTemp = Integer.parseInt(productoActual[1].trim());
				double precioTemp = Double.parseDouble(productoActual[2].trim());
				boolean productoExiste = false;
				
				// Recorremos el array producto
				for (int i = 0; i < indiceProducto; i++) {
					// Buscamos si el producto ya existe en nuestro array y le sumamos la cantidad y la venta
					if (producto[i].equals(productoTemp)) {
						cantidad[i] += cantidadTemp;
						precio[i] = precioTemp;
						productoExiste = true;
					}
				}
				
				// Si el producto no existe lo creamos e incrementamos el contador
				if (!productoExiste) {
					producto[indiceProducto] = productoTemp;
					cantidad[indiceProducto] = cantidadTemp;
					precio[indiceProducto] = precioTemp;
					indiceProducto++;
				}
				
				
				linea = br.readLine();
				contadorLineas++;
				
				
				// Controlamos el máximo de productos
				if (indiceProducto == MAX_PRODUCTOS) {
					System.out.println("Has llegado al número máximo de productos a trabajar (50).");
					break;
				} else if (contadorLineas == 200) {
					System.out.println("Has llegado al número máximo de líneas a trabajar (200)");
				}
				
				
			}
			br.close(); // ¡IMPORTANTE CERRAR EL FICHERO!
		} catch (IOException e) {
			System.out.println("Error al leer el fichero");
			e.printStackTrace();
		}
	}
	
	/* Método para escribir en el fichero
	 * @param nombreFichero
	 */
	public static void escribir(String nombreFichero) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
			
			int indiceMasVendido = productoMasVendido(); // Índice del producto más vendido
			
			
			bw.write("INFORME DE VENTAS =================\n");
			bw.write("Producto más vendido: " + producto[indiceMasVendido] + " (cantidad: " + cantidad[indiceMasVendido] + ")\n");
			bw.write("Total ventas: " + String.format("%.2f", totalVentas()) + "\n");
			bw.write("Ticket medio: " + String.format("%.2f", ticketMedio()) + "\n");
			bw.write("Detalle:\n");
			
			
			for (int i = 0; i < indiceProducto; i++) {
				double dinero = (cantidad[i] * precio[i]);
				bw.write("- " + producto[i] + ": " + String.format("%.2f", dinero) +" €\n");
				
			}
			
			bw.close();
		} catch (IOException e) {
			System.out.println("Error al escribir en el fichero");
			e.printStackTrace();
		}
	}
	
	/* Método para devolver el ÍNDICE del producto más vendido
	 * @return int indiceProd. El índice del producto más vendido
	 */
	public static int productoMasVendido() {
		
		int cantidadMax = 0;
		int indiceProd = 0;
		
		for (int i = 0; i < indiceProducto; i++) {
			if (cantidad[i] > cantidadMax) {
				cantidadMax = cantidad[i];
				indiceProd = i;
			}
		}
		return indiceProd;
		
	}
	
	/* Método para devolver el ticket medio
	 * @return double Media.
	 */
	public static double ticketMedio() {
		
		int cantidadTotal = 0;
		double sumaTotal = 0;
		
		for (int i = 0; i < indiceProducto; i++) {
			cantidadTotal += cantidad[i];
			sumaTotal += cantidad[i] * precio[i];
		}
		
		return sumaTotal/cantidadTotal;
	}
	
	
	/* Método para el total de las ventas
	 * @return double totalVentas
	 */
	public static double totalVentas() {
		double totalVentas = 0;
		
		for (int i = 0; i < indiceProducto; i++) {
			totalVentas += (precio[i] * cantidad[i]);
		}
		return totalVentas;
	}
	
	
	public static void main(String[] args) {
		
		leer("ventas.csv");
		escribir("informe_ventas.txt");
		

	}

}
