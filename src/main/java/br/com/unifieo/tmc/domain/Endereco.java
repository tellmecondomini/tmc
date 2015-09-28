package br.com.unifieo.tmc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import br.com.unifieo.tmc.domain.enumeration.Uf;

/**
 * A Endereco.
 */
@Entity
@Table(name = "ENDERECO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Endereco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Column(name = "logradouro", nullable = false)
    private String logradouro;

    @NotNull        
    @Column(name = "numero", nullable = false)
    private Integer numero;
    
    @Column(name = "bairro")
    private String bairro;

    @NotNull        
    @Column(name = "cidade", nullable = false)
    private String cidade;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "uf")
    private Uf uf;

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

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Endereco endereco = (Endereco) o;

        if ( ! Objects.equals(id, endereco.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "id=" + id +
                ", logradouro='" + logradouro + "'" +
                ", numero='" + numero + "'" +
                ", bairro='" + bairro + "'" +
                ", cidade='" + cidade + "'" +
                ", uf='" + uf + "'" +
                '}';
    }
}
