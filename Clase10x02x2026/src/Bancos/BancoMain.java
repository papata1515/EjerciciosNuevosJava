package Bancos;

public class BancoMain {
	public static void SalidaPorPantalla() {
		System.out.println("\n-------- GESTIÓN DE EMPLEADOS --------");
		System.out.println(
				"1. Añadir empleado\n2. Listar empleados\n3. Buscar por DNI\n4. Sueldo medio\n5. Listar por Salarios\n6. Búsqueda por nombre/apellidos\n7. Salir");
	}

	public static int Verificar() {
		int realizar = 0;
		boolean entradaValida = false;
		do {
			System.out.print("Selecciona una opción: ");
			String entrada = Utilidades.sc.nextLine();
			try {
				realizar = Integer.parseInt(entrada);
				if (realizar >= 1 && realizar <= 7)
					entradaValida = true;
				else
					System.out.println("Error: Introduce un valor entre 1 y 7.");
			} catch (NumberFormatException e) {
				System.out.println("Error: ¡Debes introducir un número!");
			}
		} while (!entradaValida);
		return realizar;
	}

	public static void main(String[] args) {
		int opcion;
		do {
			SalidaPorPantalla();
			opcion = Verificar();

			switch (opcion) {
			case 1:
				GestionEmpleados.aniadirEmpleado();
				break;
			case 2:
				GestionEmpleados.listarEmpleados();
				break;
			case 3:
				GestionEmpleados.buscarPorDni();
				break;
			case 4:
				GestionEmpleados.calcularSueldoMedio();
				break;
			case 5:
				GestionEmpleados.ListaPorSueldo();
				break;
			case 6:
				GestionEmpleados.BusquedaPorNombreyApelldio();
				break;
			case 7:
				System.out.println("Cerrando la aplicación :C ");
				break;
			}
		} while (opcion != 7);
	}
}