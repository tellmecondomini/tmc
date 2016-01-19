package br.com.unifieo.tmc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Assunto.
 */
@Entity
@Table(name = "ASSUNTO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Assunto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 5, max = 100)
    @Column(name = "descricao", length = 100, nullable = false, unique = true)
    private String descricao;

    @ManyToOne
    private Condominio condominio;

    @OneToMany(mappedBy = "assunto")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Topico> topicos = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "ASSUNTO_CATEGORIA",
        joinColumns = @JoinColumn(name = "assuntos_id"),
        inverseJoinColumns = @JoinColumn(name = "categorias_id"))
    private Set<Categoria> categorias = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Topico> getTopicos() {
        return topicos;
    }

    public void setTopicos(Set<Topico> topicos) {
        this.topicos = topicos;
    }

    public Condominio getCondominio() {
        return condominio;
    }

    public void setCondominio(Condominio condominio) {
        this.condominio = condominio;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Assunto assunto = (Assunto) o;

        if (!Objects.equals(id, assunto.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Assunto{" +
            "id=" + id +
            ", descricao='" + descricao + "'" +
            '}';
    }
}
