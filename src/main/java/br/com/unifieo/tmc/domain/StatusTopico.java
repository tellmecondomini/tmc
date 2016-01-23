package br.com.unifieo.tmc.domain;

public enum StatusTopico {

    AGUARDANDO_APROVACAO("Aguardando Aprovação"),

    ABERTO("Aberto"),

    ENCERRADO_COM_SUCESSO("Encerrado com Sucesso"),

    ENCERRADO_SEM_SUCESSO("Encerrado sem Sucesso"),

    REPROVADO("Reprovado");

    public String descricao;

    StatusTopico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
