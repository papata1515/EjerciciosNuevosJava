package UsosdelStringenCadean;

public class StringUsosCuatro {

	// Buscamos lo que hay antes del @
	public String sacarUsuario(String correo) {
		int arroba = correo.indexOf("@");
		return correo.substring(0, arroba);
	}

	// Buscamos lo que hay después del @
	public String sacarDominio(String correo) {
		int arroba = correo.indexOf("@");
		return correo.substring(arroba + 1);
	}
}
