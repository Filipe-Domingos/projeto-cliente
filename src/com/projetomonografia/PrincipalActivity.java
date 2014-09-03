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
	 * @link <http://pt.wikipedia.org/wiki/International_Mobile_Equipment_Identity>
	 */
	private String tel_IMEI;

	/**
	 * WebView mapa.
	 */
	private MapaWebView mapa;

	/**
	 * Url para teste.
	 */
	private final static String URL_SERVIDOR = "http://tracking.comoj.com/postdata.php";

	/**
	 * API para acessor webservice (TESTE).
	 */
	private final static String API_KEY = "teste";

	/**
	 * Telefone info.
	 */
	private TelefoneInfo telInfo;

	/**
	 * Aviso ao usuário.
	 *
	 * @param titulo
	 * @param msg
	 */
	private void _mostraMessagem(String titulo, String msg) {
		new AlertDialog.Builder(PrincipalActivity.this).setTitle(titulo)
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

		telInfo = new TelefoneInfo(this);
		tel_IMEI = telInfo.getIMEI();

		mapa = new MapaWebView(this, R.id.mapa);
		rastreador = new Rastreador(this, mapa);

		fazerCheckIn = (Button) findViewById(R.id.fazerCheckIn);
		fazerCheckIn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Double lat = rastreador.getLatitude();
		Double lng = rastreador.getLongitude();

		if (lat == null && lng == null) {
			_mostraMessagem("Erro",
					"Desculpe, não foi possível obter a longitude e latitude.");
		} else {
			new ConexaoHttpAsync(PrincipalActivity.this).execute(
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

}
