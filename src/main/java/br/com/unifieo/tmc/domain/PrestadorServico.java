package br.com.unifieo.tmc.domain;

import br.com.unifieo.tmc.domain.enumeration.Pessoa;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PrestadorServico.
 */
@Entity
@Table(name = "PRESTADOR_SERVICO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PrestadorServico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$")
    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private Integer telefone;

    @NotNull
    @Pattern(regexp = "([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}[-]?[0-9]{2})|([0-9]{2}[.]?[0-9]{3}[.]?[0-9]{3}[/]?[0-9]{4}[-]?[0-9]{2})")
    @Column(name = "documento", nullable = false)
    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(name = "pessoa")
    private Pessoa pessoa;

    @OneToOne
    private Endereco endereco;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTelefone() {
        return telefone;
    }

    public void setTelefone(Integer telefone) {
        this.telefone = telefone;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrestadorServico prestadorServico = (PrestadorServico) o;

        if ( ! Objects.equals(id, prestadorServico.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PrestadorServico{" +
                "id=" + id +
                ", nome='" + nome + "'" +
                ", email='" + email + "'" +
                ", telefone='" + telefone + "'" +
                ", documento='" + documento + "'" +
                ", pessoa='" + pessoa + "'" +
                '}';
    }
}
