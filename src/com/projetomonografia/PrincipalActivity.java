package com.projetomonografia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Desenvolvimento de um prótotipo de rastreamento utilizando a API do Android
 * para conclusão do projeto de monografia de rastreamento de dispositivos
 * móveis.
 *
 * @author Thiago Silva
 * 
 */
public class PrincipalActivity extends Activity implements
		android.view.View.OnClickListener {

	private Button fazerCheckIn;

	private Rastreador rastreador;

	/**
	 * Identificação do aparelho móvel.
	 *
	 * @link 
	 *       <http://pt.wikipedia.org/wiki/International_Mobile_Equipment_Identity
	 *       >
	 */
	private String tel_IMEI;

	private MapaWebView mapa;

	private final static String URL_SERVIDOR = "http://tracking.comoj.com/postdata.php";// "http://192.168.56.1/projeto_monografia/postdata.php";

	private final static String API_KEY = "teste"; // API webservice

	private TelefoneInfo telInfo;

	/**
	 * Aviso ao usuário.
	 *
	 * @param title
	 * @param msg
	 */
	private void _mostraMessagem(String title, String msg) {
		new AlertDialog.Builder(PrincipalActivity.this).setTitle(title)
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
		telInfo = new TelefoneInfo(this);
		tel_IMEI = telInfo.getId();

		// rastreador
		mapa = new MapaWebView(this, R.id.mapa);
		rastreador = new Rastreador(this, mapa);

		// botao check-in
		fazerCheckIn = (Button) findViewById(R.id.fazerCheckIn);
		fazerCheckIn.setOnClickListener(this);
	} // fim: onCreate

	@Override
	public void onClick(View v) {
		Double lat = rastreador.getLatitude();
		Double lng = rastreador.getLongitude();

		if (lat == null && lng == null) {
			_mostraMessagem("Erro",
					"Desculpe, não foi possível obter a longitude e latitude.");
		} else {
			new ConexaoHttpAssincrona(PrincipalActivity.this).execute(
					URL_SERVIDOR, String.valueOf(lat), String.valueOf(lng),
					tel_IMEI, API_KEY);
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
