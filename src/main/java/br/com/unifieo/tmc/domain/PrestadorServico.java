package br.com.unifieo.tmc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import br.com.unifieo.tmc.domain.enumeration.Pessoa;

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
    
    @Column(name = "email")
    private String email;

    @NotNull        
    @Column(name = "documento", nullable = false)
    private String documento;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "pessoa")
    private Pessoa pessoa;

    @OneToOne
    private Cep cep;

    @OneToMany(mappedBy = "prestadorServico")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TelefonePrestadorServico> telefonePrestadorServicos = new HashSet<>();

    @OneToOne
    private CompetenciaPrestador competenciaPrestador;

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

    public Cep getCep() {
        return cep;
    }

    public void setCep(Cep cep) {
        this.cep = cep;
    }

    public Set<TelefonePrestadorServico> getTelefonePrestadorServicos() {
        return telefonePrestadorServicos;
    }

    public void setTelefonePrestadorServicos(Set<TelefonePrestadorServico> telefonePrestadorServicos) {
        this.telefonePrestadorServicos = telefonePrestadorServicos;
    }

    public CompetenciaPrestador getCompetenciaPrestador() {
        return competenciaPrestador;
    }

    public void setCompetenciaPrestador(CompetenciaPrestador competenciaPrestador) {
        this.competenciaPrestador = competenciaPrestador;
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
                ", documento='" + documento + "'" +
                ", pessoa='" + pessoa + "'" +
                '}';
    }
}
