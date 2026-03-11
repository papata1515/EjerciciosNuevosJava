package UsosdelStringenCadean;

public class StringUsosTres {

	// Nos dice dónde aparece la palabra por primera vez
	public int encontrarPosicion(String frase, String buscar) {
		return frase.indexOf(buscar);
	}

	// Comprobamos si la palabra sale más de una vez comparando la primera y última posición
	public boolean seRepite(String frase, String buscar) {
		int primera = frase.indexOf(buscar);
		int ultima = frase.lastIndexOf(buscar);
		
		// Si la primera y la última posición son distintas, es que está repetida
		return primera != ultima && primera != -1;
	}
}
