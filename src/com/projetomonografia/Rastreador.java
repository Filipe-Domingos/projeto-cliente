package com.projetomonografia;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Classe de geolocalização.
 * 
 * @author Thiago
 */
public class Rastreador implements Rastreamento, LocationListener {

	/**
	 * Coordenadas.
	 */
	private Double latitude = null, longitude = null;

	/*
	 * Tempo de mínimo para atualizações de localização.
	 */
	private static final long TEMPO_ATUALIZACAO = 0; // 1000 * 60 * 10; // 10
														// minutos

	/*
	 * Distância mínima para haver atualizações.
	 */
	private static final float MIN_DISTANCIA = 0; // 10 metros

	/*
	 * Classe Android que provê os recursos de localização.
	 */
	private LocationManager locationManager;

	/**
	 * Mapa.
	 *
	 */
	private Mapa mapa;

	/**
	 * Construtor.
	 *
	 * @param locationManager
	 * @param mapa
	 */
	public Rastreador(Activity activity, Mapa mapa) {
		this.locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		this.mapa = mapa;

	}

	/**
	 * Quando houver alguma mudança de localização obter coordenadas e atualizar
	 * mapa.
	 *
	 * @param location
	 */
	@Override
	public void onLocationChanged(Location location) {
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
		atualiza(latitude, longitude);
		Log.i("Rastreamento", "Liga rastreamento.");
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
		Location location;

		// GPS esta habilitado
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, TEMPO_ATUALIZACAO,
					MIN_DISTANCIA, this);

			if (locationManager != null) {
				location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					mapa.atualizaCoordenadas(latitude, longitude);
				}
			}
		}

		// 'NetWork Location Provider' esta habilitado
		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, TEMPO_ATUALIZACAO,
					MIN_DISTANCIA, this);

			if (locationManager != null) {
				location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					mapa.atualizaCoordenadas(latitude, longitude);
				}
			}
		}

		Log.i("Rastreamento", "Liga rastreamento.");
	} // fim: liga

	/**
	 * Pausa rastreamento.
	 *
	 * @link 
	 *       http://stackoverflow.com/questions/8539971/having-some-trouble-getting
	 *       -my-gps-sensor-to-stop/8546115#8546115
	 */
	public void desliga() {
		locationManager.removeUpdates(this);
		Log.i("Rastreamento", "Desliga rastreamento.");
	}

	/**
	 * Atualiza o rastreamento para o mapa.
	 */
	public void atualiza(Double latitude, Double longitude) {
		mapa.atualizaCoordenadas(latitude, longitude);
	}

	/**
	 * Latitude.
	 *
	 * @return
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Longitude.
	 *
	 * @return
	 */
	public Double getLongitude() {
		return longitude;
	}
}
