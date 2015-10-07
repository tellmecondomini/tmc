package br.com.unifieo.tmc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import br.com.unifieo.tmc.domain.util.CustomLocalDateSerializer;
import br.com.unifieo.tmc.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import br.com.unifieo.tmc.domain.enumeration.Sexo;

import br.com.unifieo.tmc.domain.enumeration.TipoMorador;

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
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;
    
    @Column(name = "ativo")
    private Boolean ativo;
    
    @Column(name = "bloqueia_agendamento")
    private Boolean bloqueiaAgendamento;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoMorador tipo;

    @ManyToOne
    private Imovel imovel;

    @OneToMany(mappedBy = "morador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TelefoneMorador> telefoneMoradors = new HashSet<>();

    @OneToMany(mappedBy = "morador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ocorrencia> ocorrencias = new HashSet<>();

    @OneToMany(mappedBy = "morador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Agenda> agendas = new HashSet<>();

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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getBloqueiaAgendamento() {
        return bloqueiaAgendamento;
    }

    public void setBloqueiaAgendamento(Boolean bloqueiaAgendamento) {
        this.bloqueiaAgendamento = bloqueiaAgendamento;
    }

    public TipoMorador getTipo() {
        return tipo;
    }

    public void setTipo(TipoMorador tipo) {
        this.tipo = tipo;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public Set<TelefoneMorador> getTelefoneMoradors() {
        return telefoneMoradors;
    }

    public void setTelefoneMoradors(Set<TelefoneMorador> telefoneMoradors) {
        this.telefoneMoradors = telefoneMoradors;
    }

    public Set<Ocorrencia> getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(Set<Ocorrencia> ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public Set<Agenda> getAgendas() {
        return agendas;
    }

    public void setAgendas(Set<Agenda> agendas) {
        this.agendas = agendas;
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

        if ( ! Objects.equals(id, morador.id)) return false;

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
                ", bloqueiaAgendamento='" + bloqueiaAgendamento + "'" +
                ", tipo='" + tipo + "'" +
                '}';
    }
}
