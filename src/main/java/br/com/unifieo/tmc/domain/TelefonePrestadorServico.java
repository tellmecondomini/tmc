package br.com.unifieo.tmc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A TelefonePrestadorServico.
 */
@Entity
@Table(name = "TELEFONE_PRESTADOR_SERVICO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TelefonePrestadorServico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Long numero;

    @ManyToOne
    @JsonIgnore
    private PrestadorServico prestadorServico;

    public TelefonePrestadorServico() {

    }

    public TelefonePrestadorServico(Long numero, PrestadorServico prestadorServico) {
        this.numero = numero;
        this.prestadorServico = prestadorServico;
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

    public PrestadorServico getPrestadorServico() {
        return prestadorServico;
    }

    public void setPrestadorServico(PrestadorServico prestadorServico) {
        this.prestadorServico = prestadorServico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelefonePrestadorServico that = (TelefonePrestadorServico) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (numero != null ? !numero.equals(that.numero) : that.numero != null) return false;
        return prestadorServico != null ? prestadorServico.equals(that.prestadorServico) : that.prestadorServico == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (numero != null ? numero.hashCode() : 0);
        result = 31 * result + (prestadorServico != null ? prestadorServico.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TelefonePrestadorServico{" +
            "id=" + id +
            ", numero='" + numero + "'" +
            '}';
    }
}
