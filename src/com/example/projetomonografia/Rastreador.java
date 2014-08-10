package com.example.projetomonografia;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Thiago on 10/08/2014.
 */
public class Rastreador implements LocationListener {

    private String latitude, longitude;

    private static final long TEMPO_ATUALIZACAO = 0;//1000 * 60 * 10; // 10 minutos

    private static final float MIN_DISTANCIA = 0; // 10 metros

    private LocationManager locationManager;

    private Mapa mapa;

    /**
     * Contrututor.
     *
     * @param locationManager
     * @param mapa
     */
    public Rastreador(LocationManager locationManager, Mapa mapa) {
        this.locationManager = locationManager;
        this.mapa = mapa;
    }

    /**
     * Carrega funções de localiza??o do Android.
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

    public void desliga() {
        // ver:
        // http://stackoverflow.com/questions/8539971/having-some-trouble-getting-my-gps-sensor-to-stop/8546115#8546115
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // obtendo coordenadas
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        mapa.atualiza(latitude, longitude);
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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
