package LeyendoTexto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class EjemploFichero01 {

	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("malaga.txt"));
			String linea = "";
			int contadorLineas = 0;
			while (linea != null) {
				System.out.println(linea);
				contadorLineas++;
				linea = br.readLine();

			}
			System.out.print(" EL fiche tiene " + contadorLineas + " lineas ");
			br.close(); // ¡IMPORTANTE CERRAR EL FICHERO!
		} catch (IOException ioe) {
			System.out.println("Error al leer");
		}
	}
}
