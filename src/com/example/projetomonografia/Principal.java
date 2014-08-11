package com.example.projetomonografia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

/**
 * Desenvolvimento de um pr?tipo de rastreamento utilizando a API do Android
 * para conclus?o do projeto de monografia de rastreamento de dispositivos
 * móveis.
 *
 * @author Thiago Silva Prates
 * @since 02/05/2013
 */
public class Principal extends Activity implements
        android.view.View.OnClickListener {

    private Button fazerCheckIn;

    private Rastreador rastreador;

    /**
     * Identificação do aparelho móvel.
     *
     * @link http://pt.wikipedia.org/wiki/International_Mobile_Equipment_Identity
     */
    private String tel_IMEI;

    private Mapa mapa;

    private final static String URL_SERVIDOR = "http://tracking.comoj.com/postdata.php";// "http://192.168.56.1/projeto_monografia/postdata.php";

    private final static String API_KEY = "teste"; // chave para utilização do webservice

    private TelefoneInfo telInfo;

    /**
     * Caixa de aviso ao usuário.
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // telefone
        telInfo = new TelefoneInfo((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
        tel_IMEI = telInfo.getId();

        // rastreador
        mapa = new Mapa(this, R.id.mapa);
        rastreador = new Rastreador((LocationManager) getSystemService(Context.LOCATION_SERVICE), mapa);

        // botao check-in
        fazerCheckIn = (Button) findViewById(R.id.fazerCheckIn);
        fazerCheckIn.setOnClickListener(this);
    } // fim: onCreate


    @Override
    public void onClick(View v) {
        String lat = rastreador.getLatitude();
        String lng = rastreador.getLongitude();

        if (lat == null && lng == null) {
            _mostraMessagem("Erro",
                    "Desculpe, não foi possível obter a longitude e latitude.");
        } else {
            new ConexaoHttpAssincrona(Principal.this).execute(URL_SERVIDOR,
                    lat, lng, tel_IMEI, API_KEY);
        }
    }

    @Override
    public void onResume() {
        rastreador.liga();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        rastreador.desliga();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        rastreador.desliga();
        super.onPause();
    }

} // fim: Principal
