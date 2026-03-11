package EjercicioUno;

import java.io.*;

public class borrador {
    public static void main(String[] args) {
        // Configuración de archivos
        String archivoEntrada = "ventas.csv";
        String archivoSalida = "informe_ventas.txt";

        // Requisitos Técnicos: Arrays fijos (máximo 50 productos únicos)
        String[] productos = new String[50];
        double[] totalesVentas = new double[50];
        int[] cantidadesTotales = new int[50]; // Para encontrar el más vendido
        
        int contadorUnicos = 0;
        double sumaVentasGlobal = 0;
        int sumaUnidadesGlobal = 0;

        // 1. Lectura con BufferedReader y parseo
        try (BufferedReader br = new BufferedReader(new FileReader(archivoEntrada))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    try {
                        String nombreAct = partes[0].trim();
                        int cantAct = Integer.parseInt(partes[1].trim());
                        double precioAct = Double.parseDouble(partes[2].trim());
                        double subtotal = cantAct * precioAct;

                        // Pista: Buscar si el producto ya existe para acumular
                        int indiceEncontrado = -1;
                        for (int i = 0; i < contadorUnicos; i++) {
                            if (productos[i].equalsIgnoreCase(nombreAct)) {
                                indiceEncontrado = i;
                                break;
                            }
                        }

                        if (indiceEncontrado != -1) {
                            // Si existe, acumulamos en su posición
                            totalesVentas[indiceEncontrado] += subtotal;
                            cantidadesTotales[indiceEncontrado] += cantAct;
                        } else if (contadorUnicos < 50) {
                            // Si es nuevo, lo registramos
                            productos[contadorUnicos] = nombreAct;
                            totalesVentas[contadorUnicos] = subtotal;
                            cantidadesTotales[contadorUnicos] = cantAct;
                            contadorUnicos++;
                        }

                        // Totales globales para el ticket medio
                        sumaVentasGlobal += subtotal;
                        sumaUnidadesGlobal += cantAct;

                    } catch (NumberFormatException e) {
                        System.out.println("Error de formato en línea: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        // 2. Comparación manual para encontrar el más vendido
        String nombreMasVendido = "";
        int maxCantidad = -1;
        for (int i = 0; i < contadorUnicos; i++) {
            if (cantidadesTotales[i] > maxCantidad) {
                maxCantidad = cantidadesTotales[i];
                nombreMasVendido = productos[i];
            }
        }

        // 3. Cálculo de Ticket Medio
        double ticketMedio = (sumaUnidadesGlobal > 0) ? sumaVentasGlobal / sumaUnidadesGlobal : 0;

        // 4. Escritura del informe con BufferedWriter
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {
            bw.write("INFORME DE VENTAS ==================\n");
            bw.write("Producto más vendido: " + nombreMasVendido + " (cantidad: " + maxCantidad + ")\n");
            bw.write("Total ventas: " + String.format("%.2f", sumaVentasGlobal) + " €\n");
            bw.write("Ticket medio: " + String.format("%.2f", ticketMedio) + " €\n");
            bw.write("Detalle:\n");

            for (int i = 0; i < contadorUnicos; i++) {
                String detalle = String.format("- %s: %.2f €\n", productos[i], totalesVentas[i]);
                bw.write(detalle);
            }
            
            System.out.println("Informe generado exitosamente en: " + archivoSalida);

        } catch (IOException e) {
            System.out.println("Error al escribir el informe: " + e.getMessage());
        }
    }
}