package br.com.unifieo.tmc.domain;

import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.domain.enumeration.Tipo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private Tipo tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Sexo sexo;

    @Column(name="site")
    private String site;

    @ManyToOne
    private Condominio condominio;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "complemento")
    private String complemento;

    @ManyToOne
    private Cep cep;

    @ManyToOne
    private Morador morador;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "PRESTADOR_COMPETENCIA",
        joinColumns = @JoinColumn(name = "prestadores_id"),
        inverseJoinColumns = @JoinColumn(name = "competencias_id"))
    private Set<CompetenciaPrestador> competencias = new HashSet<>();

    @OneToMany(mappedBy = "prestadorServico")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TelefonePrestadorServico> telefonePrestadorServicos = new HashSet<>();

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


    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
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

    public Set<CompetenciaPrestador> getCompetencias() {
        return competencias;
    }

    public void setCompetencias(Set<CompetenciaPrestador> competencias) {
        this.competencias = competencias;
    }

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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

        PrestadorServico prestadorServico = (PrestadorServico) o;

        if (!Objects.equals(id, prestadorServico.id)) return false;

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
            ", tipo='" + tipo + "'" +
            ", numero='" + numero + "'" +
            ", complemento='" + complemento + "'" +
            '}';
    }
}
