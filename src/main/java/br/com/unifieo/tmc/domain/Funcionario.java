package br.com.unifieo.tmc.domain;

import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.domain.util.CustomDateTimeDeserializer;
import br.com.unifieo.tmc.domain.util.CustomDateTimeSerializer;
import br.com.unifieo.tmc.web.rest.dto.CondominioDTO;
import br.com.unifieo.tmc.web.rest.dto.FuncionarioDTO;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Sexo sexo;

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

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "responsavel")
    private Boolean responsavel;

    @ManyToOne
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

    public Funcionario() {
        this.dataCadastro = new DateTime();
        this.responsavel = false;
    }

    public Funcionario(String nome, String cpf, Sexo sexo, DateTime dataNascimento, String email,
                       String senha, Boolean ativo, DateTime dataCadastro, Integer numero, String complemento,
                       Boolean responsavel, Cep cep, Condominio condominio) {
        this.nome = nome;
        this.cpf = cpf;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
        this.numero = numero;
        this.complemento = complemento;
        this.responsavel = responsavel;
        this.cep = cep;
        this.condominio = condominio;
    }

    public Funcionario(CondominioDTO condominioDTO, Condominio condominioSave) {
        this.id = null;
        this.nome = condominioDTO.getResponsavelNome();
        this.cpf = String.valueOf(condominioDTO.getResponsavelCpf());
        this.sexo = condominioDTO.getResponsavelSexo();
        this.dataNascimento = new DateTime(condominioDTO.getResponsavelDataNascimento());
        this.email = condominioDTO.getResponsavelEmail();
        this.senha = condominioDTO.getResponsavelSenha();
        this.ativo = true;
        this.dataCadastro = new DateTime(new Date());
        this.numero = condominioDTO.getResponsavelNumero();
        this.complemento = condominioDTO.getResponsavelComplemento();
        this.responsavel = false;
        this.cep = new Cep(condominioDTO.getResponsavelLogradouro(), condominioDTO.getResponsavelBairro(), condominioDTO.getResponsavelCidade(), condominioDTO.getResponsavelUf(), condominioDTO.getResponsavelCep());
        this.condominio = condominioSave;
    }

    public Funcionario(FuncionarioDTO funcionarioDTO) {
        this.id = funcionarioDTO.getId();
        this.nome = funcionarioDTO.getNome();
        this.cpf = String.valueOf(funcionarioDTO.getCpf());
        this.sexo = funcionarioDTO.getSexo();
        this.dataNascimento = new DateTime(funcionarioDTO.getDataNascimento());
        this.email = funcionarioDTO.getEmail();
        this.senha = funcionarioDTO.getSenha();
        this.ativo = true;
        this.dataCadastro = new DateTime(new Date());
        this.numero = funcionarioDTO.getNumero();
        this.complemento = funcionarioDTO.getComplemento();
        this.responsavel = funcionarioDTO.getResponsavel();
        this.cep = new Cep(funcionarioDTO.getLogradouro(), funcionarioDTO.getBairro(), funcionarioDTO.getCidade(), funcionarioDTO.getUf(), funcionarioDTO.getCep());
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

    public Boolean getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Boolean responsavel) {
        this.responsavel = responsavel;
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

        return Objects.equals(id, funcionario.id);

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
            ", numero='" + numero + "'" +
            ", complemento='" + complemento + "'" +
            ", responsavel='" + responsavel + "'" +
            '}';
    }
}
