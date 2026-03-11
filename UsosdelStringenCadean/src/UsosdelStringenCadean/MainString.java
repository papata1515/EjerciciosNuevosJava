package UsosdelStringenCadean;

import java.util.Scanner;

public class MainString {

    static Scanner sc = new Scanner(System.in);

    public static String pedir(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    public static void main(String[] args) {
        StringUsosDos ej2 = new StringUsosDos();
        StringUsosTres ej3 = new StringUsosTres();
        StringUsosCuatro ej4 = new StringUsosCuatro();
        StringUsosCinco ej5 = new StringUsosCinco();

        // Ejercicio 2
        System.out.println("\nEjercicio 2");
        String p1 = pedir("Palabra 1: ");
        String p2 = pedir("Palabra 2: ");
        if (ej2.sonIguales(p1, p2)) {
            System.out.println("Son iguales!");
        } else {
            System.out.println("No son iguales");
        }

        // Ejercicio 3
        System.out.println("\nEjercicio 3");
        String frase3 = pedir("Frase: ");
        String buscar = pedir("Qué buscamos: ");
        int pos = ej3.encontrarPosicion(frase3, buscar);
        
        if (pos == -1) {
            System.out.println("No se encontró.");
        } else {
            System.out.println("Encontrada en la posición: " + pos);
            if (ej3.seRepite(frase3, buscar)) {
                System.out.println("Oye, la palabra aparece más veces!");
            }
        }

        // Ejercicio 4
        System.out.println("\nEjercicio 4");
        String mail = pedir("Correo: ");
        System.out.println("Usuario: " + ej4.sacarUsuario(mail));
        System.out.println("Dominio: " + ej4.sacarDominio(mail));

        // Ejercicio 5
        System.out.println("\nEjercicio 5");
        String nombreFeo = pedir("Nombre completo: ");
        System.out.println("Resultado: " + ej5.arreglarNombre(nombreFeo));

        System.out.println("\nListo! Todo terminado ♪");
    }
}