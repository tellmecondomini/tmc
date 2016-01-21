package br.com.unifieo.tmc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A TelefoneFuncionario.
 */
@Entity
@Table(name = "TELEFONE_FUNCIONARIO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TelefoneFuncionario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Long numero;

    @ManyToOne
    private Funcionario funcionario;

    public TelefoneFuncionario() {

    }

    public TelefoneFuncionario(Long numero, Funcionario funcionario) {
        this.numero = numero;
        this.funcionario = funcionario;
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

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelefoneFuncionario that = (TelefoneFuncionario) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (numero != null ? !numero.equals(that.numero) : that.numero != null) return false;
        return funcionario != null ? funcionario.equals(that.funcionario) : that.funcionario == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (numero != null ? numero.hashCode() : 0);
        result = 31 * result + (funcionario != null ? funcionario.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TelefoneFuncionario{" +
            "id=" + id +
            ", numero='" + numero + "'" +
            '}';
    }
}
