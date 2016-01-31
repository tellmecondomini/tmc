package br.com.unifieo.tmc.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for http header creation.
 *
 */
public class HeaderUtil {

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-tmcApp-alert", message);
        headers.add("X-tmcApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("tmcApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("tmcApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("tmcApp." + entityName + ".deleted", param);
    }

    public static HttpHeaders createSolicitacaoJaExisteAlert() {
        return createAlert("Já existe uma solicitação de remoção para este comentário.", null);
    }

    public static HttpHeaders createEmailJaExisteAlert() {
        return createAlert("e-mail address already in use", null);
    }

}
