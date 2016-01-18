package br.com.unifieo.tmc.domain;

/**
 * Created by Arthemus on 16/01/2016.
 */
public enum StatusTopico {

    AGUARDANDO_APROVACAO,

    ABERTO,

    ENCERRADO, // apenas funcionario_admin encerra um topico

    ENCERRADO_COM_SOLUCAO,

    ENCERRADO_SEM_SOLUCAO,

    REPROVADO

}
