package com.projetomonografia;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Classe rastreadora.
 * 
 * @author Thiago
 */
public class Rastreador extends AbstractRastreador implements LocationListener {

	/*
	 * Tempo de mínimo para atualizações de localização.
	 */
	private static final long TEMPO_ATUALIZACAO = 0; // 1000 * 60 * 10; // 10 minutos

	/*
	 * Distância mínima para haver atualizações.
	 */
	private static final float MIN_DISTANCIA = 0; // 10 metros

	/*
	 * Classe Android que provê os recursos de localização.
	 */
	private LocationManager locationManager;

	/**
	 * Construtor.
	 *
	 * @param locationManager
	 * @param mapa
	 */
	public Rastreador(LocationManager locationManager, MapaInterface mapa) {
		super(mapa);
		this.locationManager = locationManager;
	}

	/**
	 * Quando houver alguma mudança de localização obter coordenadas e atualizar
	 * mapa.
	 *
	 * @param location
	 */
	@Override
	public void onLocationChanged(Location location) {
		Double lat = location.getLatitude();
		Double lng = location.getLongitude();
		atualizaMapa(lat, lng);
	} // fim:onLocationChanged

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * Carrega funções de localização do Android.
	 */
	public void liga() {
		// GPS esta habilitado
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, TEMPO_ATUALIZACAO,
					MIN_DISTANCIA, this);
		}

		// 'NetWork Location Provider' esta habilitado
		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, TEMPO_ATUALIZACAO,
					MIN_DISTANCIA, this);

		}

		Log.w("Rastreamento", "Liga rastreamento.");
	} // fim: liga

	/**
	 * Pausa rastreamento.
	 *
	 * @link 
	 *       http://stackoverflow.com/questions/8539971/having-some-trouble-getting-my-gps-sensor-to-stop/8546115#8546115
	 */
	public void desliga() {
		locationManager.removeUpdates(this);
		Log.w("Rastreamento", "Desliga rastreamento.");
	} // fim: desliga
}