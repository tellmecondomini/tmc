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
 * A Imovel.
 */
@Entity
@Table(name = "IMOVEL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Imovel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Column(name = "rua_bloco", nullable = false)
    private String ruaBloco;

    @NotNull        
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @OneToMany(mappedBy = "imovel")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Morador> moradors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuaBloco() {
        return ruaBloco;
    }

    public void setRuaBloco(String ruaBloco) {
        this.ruaBloco = ruaBloco;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Set<Morador> getMoradors() {
        return moradors;
    }

    public void setMoradors(Set<Morador> moradors) {
        this.moradors = moradors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Imovel imovel = (Imovel) o;

        if ( ! Objects.equals(id, imovel.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Imovel{" +
                "id=" + id +
                ", ruaBloco='" + ruaBloco + "'" +
                ", numero='" + numero + "'" +
                '}';
    }
}
