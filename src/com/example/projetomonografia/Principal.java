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
 * m?veis.
 *
 * @author Thiago Silva Prates
 * @since 02/05/2013
 */
public class Principal extends Activity implements
        android.view.View.OnClickListener {

    private Button fazerCheckIn;

    private Rastreador rastreador;

    private String numeroTelefone;

    private Mapa mapa;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // telefone
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        numeroTelefone = telManager.getDeviceId();

        mapa = new Mapa(this, R.id.mapa);
        rastreador = new Rastreador((LocationManager) getSystemService(Context.LOCATION_SERVICE), mapa);

        // botao check-in
        fazerCheckIn = (Button) findViewById(R.id.fazerCheckIn);
        fazerCheckIn.setOnClickListener(this);
    } // fim: onCreate


    @Override
    public void onClick(View v) {
        if (rastreador.getLatitude() == null && rastreador.getLongitude() == null) {
            _mostraMessagem("Erro",
                    "Desculpe, não foi poss?vel obter a longitude e latitude.");
        } else {
            new ConexaoHttpAssincrona(Principal.this).execute(URL_SERVIDOR,
                    rastreador.getLatitude(), rastreador.getLongitude(), numeroTelefone, API_KEY);
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
