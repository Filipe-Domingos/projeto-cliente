package com.example.projetomonografia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Desenvolvimento de um prótipo de rastreamento utilizando a API do Android
 * para conclusão do projeto de monografia de rastreamento de dispositivos
 * móveis.
 * 
 * @author Thiago Silva Prates
 * @since 02/05/2013
 */
public class MainActivity extends Activity implements LocationListener,
		android.view.View.OnClickListener {

	private WebView mapa;

	private Button fazerCheckIn;

	private LocationManager locManager;

	private long tempoDeAtualizacao = 1000 * 60 * 10; // 10 minutos
	
	private float distanciaMinima = 10; // 10 metros

	private String numeroTelefone;

	private String latitude, longitude;

	private final static String URL_SERVIDOR = "http://tracking.comoj.com/postdata.php";// "http://192.168.56.1/projeto_monografia/postdata.php";

	private final static String API_KEY = "teste"; // chave para utilização do servidor
	
	/**
	 * Caixa de aviso
	 * 
	 * @param title
	 * @param msg
	 */
	public void _mostraMessagem(String title, String msg) {
		new AlertDialog.Builder(MainActivity.this).setTitle(title)
				.setMessage(msg)
				.setNeutralButton("Fechar", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				}).create().show();
	}

	/**
	 * Carrega funções de localização do Android.
	 */
	private void _carregaLocationProvider() {
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		boolean isEnabledNetWork = false;
		boolean isEnabledGPS = false;

		// GPS esta habilitado
		if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					tempoDeAtualizacao, distanciaMinima, this);

			isEnabledGPS = true;
		}

		// 'NetWork Location Provider' esta habilitado
		if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					tempoDeAtualizacao, distanciaMinima, this);

			isEnabledNetWork = true;
		}

		// GPS e/ou Network impossível a localização
		if ((!isEnabledGPS) && (!isEnabledNetWork)) {
			_mostraMessagem("Erro de Location Provider",
					"Desculpe, mas não foi possível carregar o GPS.");
		}
	} // fim: _carregaLocationProvider

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// telefone
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		numeroTelefone = telManager.getDeviceId();

		// mapa
		mapa = (WebView) findViewById(R.id.mapa);
		mapa.setWebViewClient(new WebViewClient() {
			private ProgressDialog carregando = new ProgressDialog(MainActivity.this);
			
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
		}); // fim: setWebViewClient

		
		// botao check-in
		fazerCheckIn = (Button) findViewById(R.id.fazerCheckIn);
		fazerCheckIn.setOnClickListener(this);

		// GPS e Network
		_carregaLocationProvider();

	} // fim: onCreate

	@Override
	public void onLocationChanged(Location location) {
		// obtendo coordenadas
		latitude = String.valueOf(location.getLatitude());
		longitude = String.valueOf(location.getLongitude());

		// contruindo mapa
		String url = "http://maps.googleapis.com/maps/api/staticmap?"
				+ "size=300x300&sensor=true&markers=color:red|" + latitude
				+ "," + longitude;
		mapa.loadUrl(url);
	} // fim:onLocationChanged

	@Override
	public void onProviderDisabled(String provider) {
		_mostraMessagem("Erro",
				"Desculpe, mas não foi possível carregar o GPS.");
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
					"Desculpe, não foi possível obter a longitude e latitude.");
		} else {
			new ConexaoHttpAsyncTask(MainActivity.this).execute(URL_SERVIDOR,
					latitude, longitude, numeroTelefone, API_KEY);
		}
	}
	
	@Override
	public void onDestroy() {
		// ver:
		// http://stackoverflow.com/questions/8539971/having-some-trouble-getting-my-gps-sensor-to-stop/8546115#8546115
		locManager.removeUpdates(this);
		super.onDestroy();
	}
	
} // fim: MainActivity
