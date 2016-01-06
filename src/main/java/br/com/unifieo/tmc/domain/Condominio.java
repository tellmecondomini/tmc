package br.com.unifieo.tmc.domain;

import br.com.unifieo.tmc.web.rest.dto.CondominioDTO;
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

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "ativo")
    private Boolean ativo;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data_cadastro", nullable = false)
    private DateTime dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(name = "disposicao")
    private Disposicao disposicao;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "complemento")
    private String complemento;

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

    @OneToMany(mappedBy = "condominio")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TelefoneCondominio> telefoneCondominios = new HashSet<>();

    public Condominio() {
    }

    public Condominio(CondominioDTO condominioDto) {
        this.id = condominioDto.getId();
        this.razaoSocial = condominioDto.getRazaoSocial();
        this.cnpj = condominioDto.getCnpj();
        this.ativo = true;
        this.dataCadastro = new DateTime();
        this.disposicao = condominioDto.getDisposicao();
        this.numero = condominioDto.getCondominioNumero();
        this.complemento = condominioDto.getCondominioComplemento();
        this.cep = new Cep(condominioDto.getCondominioLogradouro(), condominioDto.getCondominioBairro(), condominioDto.getCondominioCidade(), condominioDto.getCondominioUf(), condominioDto.getCondominioCep());
    }

    public Condominio(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

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

    public DateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(DateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Disposicao getDisposicao() {
        return disposicao;
    }

    public void setDisposicao(Disposicao disposicao) {
        this.disposicao = disposicao;
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

    public Set<TelefoneCondominio> getTelefoneCondominios() {
        return telefoneCondominios;
    }

    public void setTelefoneCondominios(Set<TelefoneCondominio> telefoneCondominios) {
        this.telefoneCondominios = telefoneCondominios;
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
                ", disposicao='" + disposicao + "'" +
                ", numero='" + numero + "'" +
                ", complemento='" + complemento + "'" +
                '}';
    }
}
