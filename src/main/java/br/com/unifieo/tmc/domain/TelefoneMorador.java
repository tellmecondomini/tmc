package br.com.unifieo.tmc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

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
    private Integer numero;

    @ManyToOne
    private Morador morador;

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

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TelefoneMorador telefoneMorador = (TelefoneMorador) o;

        if ( ! Objects.equals(id, telefoneMorador.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TelefoneMorador{" +
                "id=" + id +
                ", numero='" + numero + "'" +
                '}';
    }
}
