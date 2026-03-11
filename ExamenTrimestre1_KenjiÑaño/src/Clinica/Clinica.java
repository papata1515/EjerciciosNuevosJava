package Clinica;

import java.util.Scanner; //importamos el scanner para pedir la empresa
import java.util.Random; //importamos el random para general el dni,colesterol.

public class Clinica {
	/*
	 * inicializamos el Scanner
	 */
	static Scanner sc = new Scanner(System.in);

	/**
	 * matricula
	 */
	int[] Dni;
	/**
	 * semanas
	 */
	int[] Empresa;
	/**
	 * Velocidades
	 */
	int[] Colesterol;

	/**
	 * Numero de Pacientes de la Clinica.
	 */
	public static final int NUM_PACIENTES = 100;
	/**
	 * Maxima cantidad de Colesterol.
	 */
	public static final int MAXCOLES = 330;
	/**
	 * Minima cantidad de Colesterol.
	 */
	public static final int MINCOLES = 120;
	/**
	 * Minima cantidad de DNI
	 */
	public static final int MINDNI = 1;
	/**
	 * Maxima cantidad de DNI
	 */
	public static final int MAXDNI = 100000000;
	/**
	 * Cantidad de empresas que existen
	 */
	public static final String NUMERODEEMPRESAS[] = { "EmpresaUno", "EmpresaDos", "EmpresaTres", "EmpresaCuatro",
			"EmpresaCinco" };

	/**
	 * Crear los datos en un Contructor
	 */
	public Clinica() {
		Dni = new int[NUM_PACIENTES];
		Empresa = new int[NUM_PACIENTES];
		Colesterol = new int[NUM_PACIENTES];
		RellenarArray();
	}

	/**
	 * Rellenamos lo que vamos a introducir en los Array
	 */
	public void RellenarArray() {
		Random ram = new Random(); // inicializamos el random
		for (int i = 0; i < Dni.length; i++) {
			Dni[i] = ram.nextInt(MINDNI, MAXDNI);
			Empresa[i] = ram.nextInt(0, NUMERODEEMPRESAS.length);
			Colesterol[i] = ram.nextInt(MINCOLES, MAXCOLES);
		}
	}

	/**
	 * Imprimos lo que hemos generado para saber si lo hemos hecho bien
	 */
	public void ImprimirArray() {

		for (int i = 0; i < NUM_PACIENTES; i++) {
			System.out.printf("%d  mg/dL. |DNI [%07d] |%s | %n", Colesterol[i], Dni[i], NUMERODEEMPRESAS[Empresa[i]]);
		}

		System.out.print("-------------------------------------------------------------");
		System.out.println("");
	}

	/**
	 * Vamos a pedir por pantalla la empresa que queremos
	 * 
	 * @return
	 */
	public static int PedirPorPantalla() {
		int NumeroPedido;

		do {
			System.out.println("Introduce una empresa (1–5):");
			NumeroPedido = sc.nextInt();
			if (NumeroPedido > 5 || NumeroPedido < 1) { // Aca estamos verificando que no se introdusca mas de 5 o menos
														// de 1
				System.out.println("Error: Debes introducir un valor entre 1 y 5.");
			}
		} while (NumeroPedido > 5 || NumeroPedido < 1);
		return NumeroPedido - 1;// el array empieza en 0 entonces x eso le restamos 1
	}

	/**
	 * Vamos a hallar la media del colesterol de todas las empresas
	 * 
	 * @param Numero pedido por teclado
	 * @return
	 */
	public double AnalizarMediaDeEmpresas(int NumeroEmpresa) {
		double sumaColes = 0;
		int contador = 0;

		for (int i = 0; i < Colesterol.length; i++) {
			// Solo procesamos los datos si coinciden con el día seleccionado
			if (Empresa[i] == NumeroEmpresa) {

				int ColesActual = Colesterol[i];

				sumaColes += ColesActual;
				contador++;

			}
		}
		return sumaColes / contador;
	}

	/**
	 * Vamos a hallar el numero Maximo de colesterol de todas las empresas
	 * 
	 * @param Numero pedido por teclado
	 * @return
	 */
	public int AnalizarMaxDeEmpresas(int NumeroEmpresa) {

		int maxColes = 0;

		for (int i = 0; i < Colesterol.length; i++) {
			if (Empresa[i] == NumeroEmpresa) {
				int ColesActual = Colesterol[i];
				if (ColesActual > maxColes) {
					maxColes = ColesActual;
				}

			}
		}
		return maxColes;
	}

	/**
	 * Imprimos el max y el min del total de todas las empresas
	 */
	public void ImprimirMediaMaximo(int pedido) {

		int max = AnalizarMaxDeEmpresas(pedido);
		double media = AnalizarMediaDeEmpresas(pedido);

		System.out.println("");
		System.out.printf("Colesterol medio de todas las personas: %.0f mg/dL \n", media);
		System.out.println("Colesterol máximo: " + max + "mg/dL" + "|" + Dni[pedido]);

	}

	/**
	 * Imprimos las condiciones de los pacientes de todas las empresas.
	 */
	public void ImprimirCondicionDeSalud() {

		int contadorOptimo = 0;
		int contadorNormal = 0;
		int contadorPeligroso = 0;

		for (int i = 0; i < Colesterol.length; i++) {
			if (this.Colesterol[i] < 200) {
				contadorOptimo++;
			} else if (Colesterol[i] >= 200 && Colesterol[i] <= 239) {
				contadorNormal++;
			} else {
				contadorPeligroso++;
			}
		}
		System.out.println("Óptimo (<200): " + contadorOptimo);
		System.out.println("Normal (200-239): " + contadorNormal);
		System.out.println("Peligroso (>=240): " + contadorPeligroso);

	}

	/**
	 * Imprimir Distribución por nivel de esa empresa: Mostrar cuántas personas de
	 * esa empresa están en: Óptimo, Normal, Peligroso
	 */
	public void ImprimirCondicionDeSaludPorUnaEmpresa(int EmpresaBuscado) {
		// int EmpresaBuscado = PedirPorPantalla();
		int contadorOptimo = 0;
		int contadorNormal = 0;
		int contadorPeligroso = 0;
		//for (int E = 0; E < 5; E++) {
			for (int i = 0; i < Colesterol.length; i++) {
				if (Empresa[i] == EmpresaBuscado && Colesterol[i] < 200) {
					contadorOptimo++;
				} else if (Empresa[i] == EmpresaBuscado && Colesterol[i] >= 200 && Colesterol[i] <= 239) {
					contadorNormal++;
				} else if (Empresa[i] == EmpresaBuscado && Colesterol[i] >= 240) {
					contadorPeligroso++;
				}
			}
		
		System.out.println("=== INFORME PARA EMPRESA " + NUMERODEEMPRESAS[Empresa[EmpresaBuscado]] + " ===");

		System.out.println("Óptimo (<200): " + contadorOptimo);
		System.out.println("Normal (200-239): " + contadorNormal);
		System.out.println("Peligroso (>=240): " + contadorPeligroso);
	}
	//AAAAAA lo logre creo
	public void ImprimirNivelPeligroso(int EmpresaBuscado) {

		
			for (int i = 0; i < Colesterol.length; i++) {
				if (Empresa[i] == EmpresaBuscado && Colesterol[i] >= 240) {
					System.out.println("DNI:" + Dni[i]);
				}
			}
		}

	

	public static void main(String[] args) {
		Clinica Essalud = new Clinica(); // Coloque el Essalud por que es un hospital de Peru :D
		// Essalud.ImprimirArray();
		int EmpresaBuscado = PedirPorPantalla();

		System.out.println("=== PROCESAMIENTO DE LECTURAS DE COLESTEROL (100 PERSONAS) ===");
		System.out.println("");
		System.out.println("--- Estadísticas generales ---");
		Essalud.ImprimirMediaMaximo(EmpresaBuscado);
		System.out.println("");
		System.out.println("--- Distribución global por niveles ---");
		Essalud.ImprimirCondicionDeSalud();
		System.out.println("");
		Essalud.ImprimirCondicionDeSaludPorUnaEmpresa(EmpresaBuscado);
		System.out.println("");
		Essalud.ImprimirNivelPeligroso(EmpresaBuscado);
		System.out.println("=== FIN DEL INFORME ===");

	}

}
