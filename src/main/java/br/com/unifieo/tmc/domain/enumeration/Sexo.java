package br.com.unifieo.tmc.domain.enumeration;

/**
 * The Sexo enumeration.
 */
public enum Sexo {

    M("Masculino"),

    F("Feminino");

    private String descricao;

    /**
     * Construtor do enum, com parametro nomeExtenso.
     *
     * @param descricao String - O descricao por extenso do enum
     */
    Sexo(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna o descricao em extenso do enum.
     *
     * @return String - O descricao por extenso
     */
    public String getDescricao() {
        return descricao;
    }
}
