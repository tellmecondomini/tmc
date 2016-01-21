package br.com.unifieo.tmc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
    private Long numero;

    @ManyToOne
    private Condominio condominio;

    public TelefoneCondominio() {
    }

    public TelefoneCondominio(Condominio condominio, Long numero) {
        this.condominio = condominio;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelefoneCondominio that = (TelefoneCondominio) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (numero != null ? !numero.equals(that.numero) : that.numero != null) return false;
        return condominio != null ? condominio.equals(that.condominio) : that.condominio == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (numero != null ? numero.hashCode() : 0);
        result = 31 * result + (condominio != null ? condominio.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TelefoneCondominio{" +
            "id=" + id +
            ", numero='" + numero + "'" +
            '}';
    }
}
