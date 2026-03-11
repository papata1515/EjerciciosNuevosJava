package BancoBBVA;

import java.util.Scanner;

public class OpplusMain {
	static Scanner sc = new Scanner(System.in);

	private static void menu() {
		System.out.println("\n********** BANCO - GESTIÓN **********");
		System.out.println("1. Añadir    | 2. Listar      | 3. Buscar DNI");
		System.out.println("4. Sueldo M. | 5. Rango S.    | 6. Buscar Nombre");
		System.out.println("7. Modificar | 8. Eliminar   | 9. Informe SS");
		System.out.println("10. Salir");
		System.out.println("*************************************");
	}

	public static void main(String[] args) {
		
		GestionEmpleados.inicializarArchivo();
		
		int op;
		do {
			menu();
			op = CustomInput.pedirEntero(sc, "Selecciona una opción: ");
			switch (op) {
			case 1:
				GestionEmpleados.aniadir(sc);
				break;
			case 2:
				GestionEmpleados.listarTodo();
				break;
			case 3:
				GestionEmpleados.buscarPorDni(sc);
				break;
			case 4:
				GestionEmpleados.calcularMediaTotal();
				break;
			case 5:
				GestionEmpleados.filtrarPorSueldo(sc);
				break;
			case 6:
				GestionEmpleados.buscarPorNombre(sc);
				break;
			case 7:
				GestionEmpleados.modificarSueldo(sc);
				break;
			case 8:
				GestionEmpleados.eliminar(sc);
				break;
			case 9:
				GestionEmpleados.generarInforme();
				break;
			case 10:
				System.out.println("Saliendo...");
				break;
			default:
				System.out.println("Opción incorrecta.");
			}
		} while (op != 10);
	}

}
