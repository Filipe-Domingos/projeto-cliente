package com.example.projetomonografia;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Classe rastreador.
 *
 * Created by Thiago on 10/08/2014.
 */
public class Rastreador implements LocationListener {

    private Double latitude = null;
    private Double longitude = null;

    /**
     * Tempo de latência para atualizações de localização.
     */
    private static final long TEMPO_ATUALIZACAO = 0;//1000 * 60 * 10; // 10 minutos

    /**
     * Distância mínima para haver atualizações.
     */
    private static final float MIN_DISTANCIA = 0; // 10 metros

    private LocationManager locationManager;

    private MapaRastreador mapa;

    /**
     * Contrututor.
     *
     * @param locationManager
     * @param mapa
     */
    public Rastreador(LocationManager locationManager, MapaRastreador mapa) {
        this.locationManager = locationManager;
        this.mapa = mapa;
    }

    /**
     * Carrega funções de localização do Android.
     */
    public void liga() {

        boolean isEnabledGPS = false;

        // GPS esta habilitado
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    TEMPO_ATUALIZACAO, MIN_DISTANCIA, this);

            isEnabledGPS = true;
        }

        // 'NetWork Location Provider' esta habilitado
        if (!isEnabledGPS && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    TEMPO_ATUALIZACAO, MIN_DISTANCIA, this);

        }

        Log.w("Impossível location provider.", "Impossível location provider.");
    } // fim

    /**
     * Pausa rastreamento.
     *
     * @link http://stackoverflow.com/questions/8539971/having-some-trouble-getting-my-gps-sensor-to-stop/8546115#8546115
     */
    public void desliga() {
        locationManager.removeUpdates(this);
    }

    /**
     * Quando houver alguma mudança de localização obter coordenadas e atualizar mapa.
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        // obtendo coordenadas
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        mapa.atualizaCoordenadas(latitude, longitude);
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