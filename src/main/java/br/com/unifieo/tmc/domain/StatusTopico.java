package br.com.unifieo.tmc.domain;

public enum StatusTopico {

    AGUARDANDO_APROVACAO("Aguardando aprovação"),

    ABERTO("Aberto"),

    ENCERRADO("Encerrado"),

    ENCERRADO_COM_SOLUCAO("Encerrado com solução"),

    ENCERRADO_SEM_SOLUCAO("Encerrado sem solução"),

    REPROVADO("Reprovado");

    public String descricao;

    StatusTopico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
