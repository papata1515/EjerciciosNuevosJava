package ModificandoFicheros;
import java.io.*;

public class ProcesarEdades {
    public static void main(String[] args) {
        String archivoEntrada = "entrada.txt";
        String archivoSalida = "resultado.txt";
        
        double sumaEdades = 0;
        int contadorPersonas = 0;

        // 1 y 2. Leer entrada.txt y procesar datos
        try (BufferedReader br = new BufferedReader(new FileReader(archivoEntrada))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    // Separamos por la coma: [Juan, 25]
                    String[] partes = linea.split(",");
                    if (partes.length == 2) {
                        double edad = Double.parseDouble(partes[1].trim());
                        sumaEdades += edad;
                        contadorPersonas++;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        // 3 y 4. Calcular media y escribir en resultado.txt
        if (contadorPersonas > 0) {
            double media = sumaEdades / contadorPersonas;
            
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivoSalida))) {
                pw.printf("La edad media es: %.2f años", media);
                System.out.println("Archivo 'resultado.txt' generado con éxito.\n");
            } catch (IOException e) {
                System.out.println("Error al escribir el archivo: " + e.getMessage());
            }
        }

        // 5. Leer y mostrar resultado.txt por consola
        System.out.println("--- Contenido del archivo de salida ---");
        try (BufferedReader brResultado = new BufferedReader(new FileReader(archivoSalida))) {
            String lineaResultado;
            while ((lineaResultado = brResultado.readLine()) != null) {
                System.out.println(lineaResultado);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el resultado: " + e.getMessage());
        }
    }
}