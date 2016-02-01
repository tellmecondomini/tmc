package br.com.unifieo.tmc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A TelefoneMorador.
 */
@Entity
@Table(name = "TELEFONE_MORADOR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TelefoneMorador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Long numero;

    @ManyToOne
    @JsonIgnore
    private Morador morador;

    public TelefoneMorador() {

    }

    public TelefoneMorador(Long numero, Morador morador) {
        this.numero = numero;
        this.morador = morador;
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

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelefoneMorador that = (TelefoneMorador) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (numero != null ? !numero.equals(that.numero) : that.numero != null) return false;
        return morador != null ? morador.equals(that.morador) : that.morador == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (numero != null ? numero.hashCode() : 0);
        result = 31 * result + (morador != null ? morador.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TelefoneMorador{" +
            "id=" + id +
            ", numero='" + numero + "'" +
            '}';
    }
}
