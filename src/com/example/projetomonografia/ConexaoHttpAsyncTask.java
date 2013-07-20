package com.example.projetomonografia;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Classe assíncrona de conexão com servidor remoto.
 * 
 * @author Thiago
 */
public class ConexaoHttpAsyncTask extends AsyncTask<String, Void, String> {

	private ProgressDialog carregando;

	private Context contexto;

	public ConexaoHttpAsyncTask(Context c) {
		contexto = c;
		carregando = new ProgressDialog(c);
	}

	protected void onPostExecute(String result) {
		String textoResultado = "";

		// remove progresso
		carregando.dismiss();

		// verifica resposta do servidor
		String conteudoRetornado = result.trim().replace("<[^>]*>", "");
		String resposta;
		
			try {
				resposta = (String) new JSONObject(conteudoRetornado).get("result");
			} catch (JSONException e) {
				//e.printStackTrace();
				resposta = "";
			}
		
		
		//if (Boolean.parseBoolean(result.trim().substring(0, 4)) == true) {
		if (Boolean.parseBoolean(resposta) == true) {
			textoResultado = "Coordenadas enviada com sucesso.";
		} else {
			textoResultado = "Ocorreu um erro.";
		}

		Toast.makeText(contexto, textoResultado, Toast.LENGTH_LONG).show();
	} // fim: onPostExecute

	protected void onPreExecute() {
		carregando.setTitle("Por favor, aguarde uns instantes.");
		carregando.setMessage("Carregando...");
		carregando.setCancelable(false);
		carregando.show();
	} // fim: onPreExecute

	@Override
	protected String doInBackground(String... params) {
		String url = params[0];
		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("latitude", params[1]);
		parametros.put("longitude", params[2]);
		parametros.put("telefone", params[3]);
		parametros.put("api", params[4]);

		return ConexaoHttp.em(url, parametros);
	} // fim: doInBackground

} // fim: AsyncHttpConnection
