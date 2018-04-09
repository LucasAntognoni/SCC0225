package com.exemple.android.crud_exercicio;


import java.net.URL;

import okhttp3.HttpUrl;


/*
 * Classe criada para ajudar a acessar as URLs do Webserbice
 */

public class WebServiceUtil {

    /*
     * string_url armazena a URL raiz para acessar o webservice
     */
    private static final String string_url = "https://contacts-test-api.herokuapp.com/contacts/";

    /*
     * Objeto URL que aponta para a raiz do webserive
     */
    public static final URL WEBSERVICE_URL = HttpUrl.parse(string_url).url();

    /*
     * Método que gera a URL para realizar operações de PUT e DELETE sobre um contato especifico
     * @param contact -> o Contato para o qual a URL especifica será gerada.
     */
    public static URL getContactURL(Contact contact) {
        return HttpUrl.parse(string_url + contact.getId() + "/").url();
    }
}
