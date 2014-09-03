package com.projetomonografia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Classe Mapa.
 * 
 * @author Thiago
 */
public class MapaWebView implements Mapa {

	private WebView mapa;

	private ProgressDialog carregando;

	/**
	 * Construtor.
	 * 
	 * @param activity
	 * @param view
	 */
	public MapaWebView(Activity activity, int view) {
		carregando = new ProgressDialog(activity);

		mapa = (WebView) activity.findViewById(view);
		mapa.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

				carregando.setTitle("Por favor, aguarde um instante.");
				carregando.setMessage("Carregando...");
				carregando.setCancelable(false);
				carregando.show();
			}

			public void onPageFinished(WebView view, String url) {
				carregando.dismiss();
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				mapa.loadData("<h1>Sem Conex&atilde;o</h1>", "text/html; charset=utf-8", "utf-8");
				Log.i("Erro webview", "Erro:" + errorCode);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void desenhaEm(Double latitude, Double longitude) {
		String url = "http://maps.googleapis.com/maps/api/staticmap?"
				+ "size=300x300&sensor=true&markers=color:red|" + latitude
				+ "," + longitude;
		mapa.loadUrl(url);
	}

}
