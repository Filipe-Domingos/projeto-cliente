package com.projetomonografia;

/**
 * Created by Thiago on 12/08/2014.
 */
interface Rastreamento {

	/**
	 * Atualiza o mapa a partir das coordenadas fornecidas.
	 *
	 * @param latitude
	 * @param longitude
	 */
	public void atualiza(Double latitude, Double longitude);

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
