package com.example.projetomonografia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

/**
 * Desenvolvimento de um pr?tipo de rastreamento utilizando a API do Android
 * para conclus?o do projeto de monografia de rastreamento de dispositivos
 * m?veis.
 *
 * @author Thiago Silva Prates
 * @since 02/05/2013
 */
public class Principal extends Activity implements LocationListener,
        android.view.View.OnClickListener {

    private Button fazerCheckIn;

    private LocationManager locManager;

    private long TEMPO_ATUALIZACAO = 0;//1000 * 60 * 10; // 10 minutos

    private float MIN_DISTANCIA = 0; // 10 metros

    private String numeroTelefone;

    private Mapa mapa;

    private String latitude, longitude;

    private final static String URL_SERVIDOR = "http://tracking.comoj.com/postdata.php";// "http://192.168.56.1/projeto_monografia/postdata.php";

    private final static String API_KEY = "teste"; // chave para utiliza??o do servidor

    /**
     * Caixa de aviso
     *
     * @param title
     * @param msg
     */
    public void _mostraMessagem(String title, String msg) {
        new AlertDialog.Builder(Principal.this).setTitle(title)
                .setMessage(msg)
                .setNeutralButton("Fechar", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                }).create().show();
    }

    /**
     * Carrega fun??es de localiza??o do Android.
     */
    private void _carregaLocationProvider() {

        //boolean isEnabledNetWork = false;
        boolean isEnabledGPS = false;

        // GPS esta habilitado
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    TEMPO_ATUALIZACAO, MIN_DISTANCIA, this);

            isEnabledGPS = true;
        }

        // 'NetWork Location Provider' esta habilitado
        if (!isEnabledGPS && locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    TEMPO_ATUALIZACAO, MIN_DISTANCIA, this);

            //isEnabledNetWork = true;
        }

        // GPS e/ou Network imposs?vel a localiza??o
        //if ((!isEnabledGPS) && (!isEnabledNetWork)) {
        //    _mostraMessagem("Erro de Location Provider",
        //            "Desculpe, mas n?o foi poss?vel carregar o GPS.");
        //}
    } // fim: _carregaLocationProvider

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // GPS e Network
        //_carregaLocationProvider();

        // telefone
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        numeroTelefone = telManager.getDeviceId();

        // mapa
        /*mapa = (WebView) findViewById(R.id.mapa);
        mapa.setWebViewClient(new WebViewClient() {
            private ProgressDialog carregando = new ProgressDialog(Principal.this);

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                carregando.setTitle("Por favor, aguarde uns instantes.");
                carregando.setMessage("Carregando...");
                carregando.setCancelable(false);
                carregando.show();
            }

            public void onPageFinished(WebView view, String url) {
                carregando.dismiss();
            }
        }); // fim: setWebViewClient */

        mapa = new Mapa(this, R.id.mapa);

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // botao check-in
        fazerCheckIn = (Button) findViewById(R.id.fazerCheckIn);
        fazerCheckIn.setOnClickListener(this);


    } // fim: onCreate

    @Override
    public void onLocationChanged(Location location) {
        // obtendo coordenadas
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        // contruindo mapa
        /*String url = "http://maps.googleapis.com/maps/api/staticmap?"
                + "size=300x300&sensor=true&markers=color:blue|" + latitude
                + "," + longitude;
        mapa.loadUrl(url); */

        mapa.atualiza(latitude, longitude);
    } // fim:onLocationChanged

    @Override
    public void onProviderDisabled(String provider) {
        _mostraMessagem("Erro",
                "Desculpe, mas n?o foi poss?vel carregar o GPS.");
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onClick(View v) {
        if (latitude == null && longitude == null) {
            _mostraMessagem("Erro",
                    "Desculpe, n?o foi poss?vel obter a longitude e latitude.");
        } else {
            new ConexaoHttpAsyncTask(Principal.this).execute(URL_SERVIDOR,
                    latitude, longitude, numeroTelefone, API_KEY);
        }
    }

    @Override
    public void onResume() {
        _carregaLocationProvider();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // ver:
        // http://stackoverflow.com/questions/8539971/having-some-trouble-getting-my-gps-sensor-to-stop/8546115#8546115
        locManager.removeUpdates(this);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        // ver:
        // http://stackoverflow.com/questions/8539971/having-some-trouble-getting-my-gps-sensor-to-stop/8546115#8546115
        locManager.removeUpdates(this);
        super.onPause();
    }

} // fim: Principal
