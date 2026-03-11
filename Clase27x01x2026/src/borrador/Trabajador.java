import java.io.*;
import java.util.*;

// 1. Clase Trabajador (El molde)
class Trabajador {
    int id;
    String nombre;
    double sueldoBase;
    int horasExtra;
    double sueldoTotal;
    char categoria;

    public Trabajador(int id, String nombre, double sueldoBase, int horasExtra) {
        this.id = id;
        this.nombre = nombre;
        this.sueldoBase = sueldoBase;
        this.horasExtra = horasExtra;
    }

    public void calcularSueldo() {
        // Lógica: Sueldo base + (horas extra * 20€)
        this.sueldoTotal = this.sueldoBase + (this.horasExtra * 20);
        
        // Asignación de categoría
        if (this.sueldoTotal > 3000) this.categoria = 'A';
        else if (this.sueldoTotal >= 1500) this.categoria = 'B';
        else this.categoria = 'C';
    }
}

public class GestionNominas {
    public static void main(String[] args) {
        List<Trabajador> listaTrabajadores = new ArrayList<>();
        String archivo = "empleados.csv";

        // 2. Lectura del CSV y Validaciones
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try {
                    String[] datos = linea.split(",");
                    int id = Integer.parseInt(datos[0].trim());
                    String nombre = datos[1].trim();
                    double sueldoB = Double.parseDouble(datos[2].trim());
                    int horasE = Integer.parseInt(datos[3].trim());

                    Trabajador t = new Trabajador(id, nombre, sueldoB, horasE);
                    t.calcularSueldo();
                    listaTrabajadores.add(t);
                } catch (Exception e) {
                    System.err.println("Error en línea: " + linea + " (Datos no válidos)");
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo CSV.");
        }

        // 3. Ordenación por ID
        listaTrabajadores.sort(Comparator.comparingInt(t -> t.id));

        // 4. Mostrar Informe
        System.out.println("ID | Nombre | Sueldo Total | Cat");
        for (Trabajador t : listaTrabajadores) {
            System.out.printf("%d | %s | %.2f | %c%n", t.id, t.nombre, t.sueldoTotal, t.categoria);
        }
    }
}