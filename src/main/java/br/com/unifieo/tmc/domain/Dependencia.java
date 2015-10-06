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

/**
 * A Dependencia.
 */
@Entity
@Table(name = "DEPENDENCIA")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dependencia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Column(name = "nome", nullable = false)
    private String nome;
    
    @Column(name = "disponivel")
    private Boolean disponivel;

    @NotNull        
    @Column(name = "capacidade", nullable = false)
    private Integer capacidade;

    @NotNull
    @Min(value = 0)        
    @Column(name = "custo_adicional", nullable = false)
    private Double custoAdicional;
    
    @Column(name = "regra_uso")
    private String regraUso;

    @OneToMany(mappedBy = "dependencia")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DisponibilidadeDependencia> disponibilidadeDependencias = new HashSet<>();

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

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Double getCustoAdicional() {
        return custoAdicional;
    }

    public void setCustoAdicional(Double custoAdicional) {
        this.custoAdicional = custoAdicional;
    }

    public String getRegraUso() {
        return regraUso;
    }

    public void setRegraUso(String regraUso) {
        this.regraUso = regraUso;
    }

    public Set<DisponibilidadeDependencia> getDisponibilidadeDependencias() {
        return disponibilidadeDependencias;
    }

    public void setDisponibilidadeDependencias(Set<DisponibilidadeDependencia> disponibilidadeDependencias) {
        this.disponibilidadeDependencias = disponibilidadeDependencias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Dependencia dependencia = (Dependencia) o;

        if ( ! Objects.equals(id, dependencia.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dependencia{" +
                "id=" + id +
                ", nome='" + nome + "'" +
                ", disponivel='" + disponivel + "'" +
                ", capacidade='" + capacidade + "'" +
                ", custoAdicional='" + custoAdicional + "'" +
                ", regraUso='" + regraUso + "'" +
                '}';
    }
}
