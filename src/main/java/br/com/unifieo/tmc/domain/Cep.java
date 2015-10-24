package br.com.unifieo.tmc.domain;

import br.com.unifieo.tmc.web.rest.dto.CondominioDTO;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import br.com.unifieo.tmc.domain.enumeration.Uf;

/**
 * A Cep.
 */
@Entity
@Table(name = "CEP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cep implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "logradouro", nullable = false)
    private String logradouro;

    @NotNull
    @Column(name = "bairro", nullable = false)
    private String bairro;

    @NotNull
    @Column(name = "cidade", nullable = false)
    private String cidade;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "uf", nullable = false)
    private Uf uf;

    @NotNull
    @Column(name = "cep", nullable = false)
    private Integer cep;

    public Cep() {
    }

    public Cep(String logradouro, String bairro, String cidade, Uf uf, Integer cep) {
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Uf getUf() {
        return uf;
    }

    public void setUf(Uf uf) {
        this.uf = uf;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Cep cep = (Cep) o;

        if ( ! Objects.equals(id, cep.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cep{" +
                "id=" + id +
                ", logradouro='" + logradouro + "'" +
                ", bairro='" + bairro + "'" +
                ", cidade='" + cidade + "'" +
                ", uf='" + uf + "'" +
                ", cep='" + cep + "'" +
                '}';
    }
}
