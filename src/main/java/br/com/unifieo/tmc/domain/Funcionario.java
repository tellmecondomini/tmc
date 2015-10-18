package br.com.unifieo.tmc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import br.com.unifieo.tmc.domain.util.CustomDateTimeDeserializer;
import br.com.unifieo.tmc.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import br.com.unifieo.tmc.domain.enumeration.Sexo;

/**
 * A Funcionario.
 */
@Entity
@Table(name = "FUNCIONARIO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Funcionario implements Serializable {

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
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data_nascimento", nullable = false)
    private DateTime dataNascimento;

    @NotNull        
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull        
    @Column(name = "senha", nullable = false)
    private String senha;
    
    @Column(name = "ativo")
    private Boolean ativo;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data_cadastro", nullable = false)
    private DateTime dataCadastro;

    @OneToOne
    private Cep cep;

    @ManyToOne
    private Condominio condominio;

    @OneToMany(mappedBy = "funcionario")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TelefoneFuncionario> telefoneFuncionarios = new HashSet<>();

    @OneToMany(mappedBy = "funcionario")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comentario> comentarios = new HashSet<>();

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

    public DateTime getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(DateTime dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public DateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(DateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Cep getCep() {
        return cep;
    }

    public void setCep(Cep cep) {
        this.cep = cep;
    }

    public Condominio getCondominio() {
        return condominio;
    }

    public void setCondominio(Condominio condominio) {
        this.condominio = condominio;
    }

    public Set<TelefoneFuncionario> getTelefoneFuncionarios() {
        return telefoneFuncionarios;
    }

    public void setTelefoneFuncionarios(Set<TelefoneFuncionario> telefoneFuncionarios) {
        this.telefoneFuncionarios = telefoneFuncionarios;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Funcionario funcionario = (Funcionario) o;

        if ( ! Objects.equals(id, funcionario.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", nome='" + nome + "'" +
                ", cpf='" + cpf + "'" +
                ", sexo='" + sexo + "'" +
                ", dataNascimento='" + dataNascimento + "'" +
                ", email='" + email + "'" +
                ", senha='" + senha + "'" +
                ", ativo='" + ativo + "'" +
                ", dataCadastro='" + dataCadastro + "'" +
                '}';
    }
}
