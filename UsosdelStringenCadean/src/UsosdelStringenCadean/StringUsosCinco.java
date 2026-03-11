package UsosdelStringenCadean;

public class StringUsosCinco {

	public String arreglarNombre(String nombreSucio) {
		// 1. Limpiamos espacios fuera y pasamos a minúsculas
		String limpio = nombreSucio.trim().toLowerCase();
		
		// 2. Buscamos los espacios para separar nombre y apellidos
		int primerEspacio = limpio.indexOf(" ");
		int ultimoEspacio = limpio.lastIndexOf(" ");
		
		// 3. Extraemos las partes
		String nombre = limpio.substring(0, primerEspacio);
		String apellidos = limpio.substring(primerEspacio + 1);
		
		// 4. Lo juntamos como pide el ejercicio: APELLIDOS, nombre
		return apellidos.toUpperCase() + ", " + nombre;
	}
}