package br.com.unifieo.tmc.domain;

import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.domain.enumeration.TipoMorador;
import br.com.unifieo.tmc.domain.util.CustomDateTimeDeserializer;
import br.com.unifieo.tmc.domain.util.CustomDateTimeSerializer;
import br.com.unifieo.tmc.web.rest.dto.MoradorDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Morador.
 */
@Entity
@Table(name = "MORADOR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Morador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Sexo sexo;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "senha", nullable = false)
    private String senha;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data_nascimento", nullable = false)
    private DateTime dataNascimento;

    @Column(name = "ativo")
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoMorador tipo;

    @ManyToOne
    private Condominio condominio;

    @OneToMany(mappedBy = "morador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PrestadorServico> prestadoresServicos = new HashSet<>();

    @OneToMany(mappedBy = "morador", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TelefoneMorador> telefoneMoradors = new HashSet<>();

    @OneToMany(mappedBy = "morador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comentario> comentarios = new HashSet<>();

    public Morador() {
    }

    public Morador(MoradorDTO moradorDTO) {
        this.nome = moradorDTO.getNome();
        this.cpf = moradorDTO.getCpf();
        this.sexo = moradorDTO.getSexo();
        this.email = moradorDTO.getEmail();
        this.senha = moradorDTO.getSenha();
        this.dataNascimento = moradorDTO.getDataNascimento();
        this.ativo = moradorDTO.getAtivo();
        this.tipo = moradorDTO.getTipo();
    }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public DateTime getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(DateTime dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public TipoMorador getTipo() {
        return tipo;
    }

    public void setTipo(TipoMorador tipo) {
        this.tipo = tipo;
    }

    public Set<TelefoneMorador> getTelefoneMoradors() {
        return telefoneMoradors;
    }

    public void setTelefoneMoradors(Set<TelefoneMorador> telefoneMoradors) {
        this.telefoneMoradors = telefoneMoradors;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public Condominio getCondominio() {
        return condominio;
    }

    public void setCondominio(Condominio condominio) {
        this.condominio = condominio;
    }

    public Set<PrestadorServico> getPrestadoresServicos() {
        return prestadoresServicos;
    }

    public void setPrestadoresServicos(Set<PrestadorServico> prestadoresServicos) {
        this.prestadoresServicos = prestadoresServicos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Morador morador = (Morador) o;

        if (!Objects.equals(id, morador.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Morador{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", cpf='" + cpf + "'" +
            ", sexo='" + sexo + "'" +
            ", email='" + email + "'" +
            ", senha='" + senha + "'" +
            ", dataNascimento='" + dataNascimento + "'" +
            ", ativo='" + ativo + "'" +
            ", tipo='" + tipo + "'" +
            '}';
    }
}
