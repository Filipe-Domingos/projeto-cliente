package com.projetomonografia;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Classe para informações do telefone.
 * 
 * @author Thiago
 */
public class TelefoneInfo {
	/**
	 * Telefone manager.
	 */
	private TelephonyManager telephonyManager;

	/**
	 * Construtor.
	 *
	 * @param telephonyManager
	 */
	public TelefoneInfo(Activity activity) {
		this.telephonyManager = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * Retorna o IMEI do aparelho. 
	 * Obs.: O IMEI pode ser encontrado no espaço destinado à bateria ou digitando *#06# no celular.
	 *
	 * @link <http://pt.wikipedia.org/wiki/International_Mobile_Equipment_Identity>
	 * @return IMEI
	 */
	public String getIMEI() {
		return telephonyManager.getDeviceId();
	}
}
