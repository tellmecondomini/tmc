package br.com.unifieo.tmc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TelefoneCondominio.
 */
@Entity
@Table(name = "TELEFONE_CONDOMINIO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TelefoneCondominio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @ManyToOne
    private Condominio condominio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Condominio getCondominio() {
        return condominio;
    }

    public void setCondominio(Condominio condominio) {
        this.condominio = condominio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TelefoneCondominio telefoneCondominio = (TelefoneCondominio) o;

        if ( ! Objects.equals(id, telefoneCondominio.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TelefoneCondominio{" +
                "id=" + id +
                ", numero='" + numero + "'" +
                '}';
    }
}
