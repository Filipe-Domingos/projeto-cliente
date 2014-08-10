package com.example.projetomonografia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Classe de utilit�rio para conex�o via HTTP.
 *
 * @author Thiago
 * @category Projeto Monografia
 * @since 08/05/2013
 */
public class ConexaoHttp {

    // endereço URL
    static URL baseUrl = null;

    // Classe Http de conexão
    static HttpURLConnection httpUrl = null;

    // string de retorno
    static StringBuilder resultado = null;

    /**
     * Método que retorna que envia parâmetros do mapa e retorna resposta doservidor.
     *
     * @param url
     * @param parametros
     * @return
     */
    public static String em(String url, Map<String, String> parametros) {

        resultado = new StringBuilder();

        try {
            baseUrl = new URL(url);
            httpUrl = (HttpURLConnection) baseUrl.openConnection();

            String dadosPost = "";
            Set<String> paramKeys = parametros.keySet();
            for (String key : paramKeys) {
                dadosPost += ("&" + key + "=" + URLEncoder.encode(
                        parametros.get(key), "UTF-8"));
            }

            // remove '&' do inicio -> &key1=value1&key2=value2
            if (dadosPost.length() > 0) {
                dadosPost = dadosPost.substring(1);
            }

            // set return output and input
            httpUrl.setDoInput(true);
            httpUrl.setDoOutput(true);

            // http headers
            httpUrl.setRequestMethod("POST");
            httpUrl.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpUrl.setRequestProperty("Content-Length",
                    Integer.toString(dadosPost.getBytes().length));

            // enviando dados
            OutputStream out = httpUrl.getOutputStream();
            out.write(dadosPost.getBytes());
            out.flush();
            out.close();

            // lendo retorna em string
            String line;
            InputStreamReader reader = new InputStreamReader(
                    httpUrl.getInputStream());
            BufferedReader buffer = new BufferedReader(reader);
            while ((line = buffer.readLine()) != null) {
                resultado.append(line);
                resultado.append('\r');
            }
            buffer.close();

            int code = httpUrl.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                return "ERROR";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpUrl != null) {
                httpUrl.disconnect(); // fecha conexão
            }
        }

        return resultado.toString();

    } // fim: connect
} // fim: HttpConnection
