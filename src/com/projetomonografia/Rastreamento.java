package com.projetomonografia;

/**
 * Interface Rastreamento.
 * 
 * @author Thiago
 */
interface Rastreamento {

	/**
	 * Atualiza o mapa a partir das coordenadas fornecidas.
	 *
	 * @param latitude
	 * @param longitude
	 */
	public void atualizaCoords(Double latitude, Double longitude);

	/**
	 * Latitude.
	 *
	 * @return
	 */
	public Double getLatitude();

	/**
	 * Longitude.
	 *
	 * @return
	 */
	public Double getLongitude();

}
