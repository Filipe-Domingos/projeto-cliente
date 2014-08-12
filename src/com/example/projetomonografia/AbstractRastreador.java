package com.example.projetomonografia;

/**
 * Created by Thiago on 12/08/2014.
 */
public abstract class AbstractRastreador {
    protected Double latitude = null;
    protected Double longitude = null;

    /**
     * Construtor.
     *
     */
    private MapaInterface mapa;

    public AbstractRastreador(MapaInterface mapa) {
        this.mapa = mapa;
    }

    /**
     * Atualiza o mapa a partir das coordenadas fornecidas.
     *
     * @param latitude
     * @param longitude
     */
    public void atualizaMapa(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
