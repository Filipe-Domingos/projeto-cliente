package com.projetomonografia;

import android.telephony.TelephonyManager;

/**
 * Classe para gerenciamento de informações do telefone.
 * 
 * @author Thiago
 */
public class TelefoneInfo {
    private TelephonyManager telephonyManager;

    /**
     * Construtor.
     *
     * @param telephonyManager
     */
    public TelefoneInfo(TelephonyManager telephonyManager) {
        this.telephonyManager = telephonyManager;
    }

    /**
     * Retorna o IMEI do aparelho.
     * O IMEI pode ser encontrado no espaço destinado à bateria ou digitando *#06# no celular.
     *
     * @return IMEI
     * @link http://pt.wikipedia.org/wiki/International_Mobile_Equipment_Identity
     */
    public String getId() {
        return telephonyManager.getDeviceId();
    }
}
