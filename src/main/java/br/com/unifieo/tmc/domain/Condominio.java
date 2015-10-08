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

import br.com.unifieo.tmc.domain.enumeration.Disposicao;

/**
 * A Condominio.
 */
@Entity
@Table(name = "CONDOMINIO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Condominio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @NotNull        
    @Column(name = "cnpj", nullable = false)
    private String cnpj;
    
    @Column(name = "ativo")
    private Boolean ativo;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;
    
    @Column(name = "telefone")
    private Integer telefone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "disposicao")
    private Disposicao disposicao;

    @OneToOne
    private Cep cep;

    @OneToMany(mappedBy = "condominio")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Funcionario> funcionarios = new HashSet<>();

    @OneToMany(mappedBy = "condominio")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dependencia> dependencias = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Integer getTelefone() {
        return telefone;
    }

    public void setTelefone(Integer telefone) {
        this.telefone = telefone;
    }

    public Disposicao getDisposicao() {
        return disposicao;
    }

    public void setDisposicao(Disposicao disposicao) {
        this.disposicao = disposicao;
    }

    public Cep getCep() {
        return cep;
    }

    public void setCep(Cep cep) {
        this.cep = cep;
    }

    public Set<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Set<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public Set<Dependencia> getDependencias() {
        return dependencias;
    }

    public void setDependencias(Set<Dependencia> dependencias) {
        this.dependencias = dependencias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Condominio condominio = (Condominio) o;

        if ( ! Objects.equals(id, condominio.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Condominio{" +
                "id=" + id +
                ", razaoSocial='" + razaoSocial + "'" +
                ", cnpj='" + cnpj + "'" +
                ", ativo='" + ativo + "'" +
                ", dataCadastro='" + dataCadastro + "'" +
                ", telefone='" + telefone + "'" +
                ", disposicao='" + disposicao + "'" +
                '}';
    }
}
