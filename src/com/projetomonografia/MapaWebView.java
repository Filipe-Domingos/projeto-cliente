package com.projetomonografia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
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

				carregando.setTitle("Por favor, aguarde uns instantes.");
				carregando.setMessage("Carregando...");
				carregando.setCancelable(false);
				carregando.show();
			}

			public void onPageFinished(WebView view, String url) {
				carregando.dismiss();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void desenhaEm(Double latitude, Double longitude) {
		// contruindo mapa
		String url = "http://maps.googleapis.com/maps/api/staticmap?"
				+ "size=300x300&sensor=true&markers=color:red|" + latitude
				+ "," + longitude;
		mapa.loadUrl(url);
	} // fim: atualizaCoordenadas

}
