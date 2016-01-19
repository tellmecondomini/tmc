package br.com.unifieo.tmc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AvaliaCompetencia.
 */
@Entity
@Table(name = "AVALIA_COMPETENCIA")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AvaliaCompetencia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nota")
    private Integer nota;

    @Column(name = "mensagem")
    private String mensagem;

    @Column(name = "ativo")
    private Boolean ativo;

    @NotNull
    @Column(name = "data", nullable = false)
    private Integer data;

    @ManyToOne
    private Morador morador;

    @ManyToOne
    private PrestadorServico prestadorServico;

    @ManyToOne
    private CompetenciaPrestador competenciaPrestador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
    }

    public PrestadorServico getPrestadorServico() {
        return prestadorServico;
    }

    public void setPrestadorServico(PrestadorServico prestadorServico) {
        this.prestadorServico = prestadorServico;
    }

    public CompetenciaPrestador getCompetenciaPrestador() {
        return competenciaPrestador;
    }

    public void setCompetenciaPrestador(CompetenciaPrestador competenciaPrestador) {
        this.competenciaPrestador = competenciaPrestador;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AvaliaCompetencia avaliaCompetencia = (AvaliaCompetencia) o;

        if ( ! Objects.equals(id, avaliaCompetencia.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AvaliaCompetencia{" +
                "id=" + id +
                ", nota='" + nota + "'" +
                ", mensagem='" + mensagem + "'" +
                ", ativo='" + ativo + "'" +
                '}';
    }
}
