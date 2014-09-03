package com.projetomonografia;

/**
 * Mapa interface.
 *
 * @author Thiago
 */
public interface Mapa {

	/**
	 * Atualiza coordenadas no mapa.
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public void desenhaEm(Double latitude, Double longitude);
}
