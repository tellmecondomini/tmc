package br.com.unifieo.tmc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DisponibilidadeDependencia.
 */
@Entity
@Table(name = "DISPONIBILIDADE_DEPENDENCIA")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DisponibilidadeDependencia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana;

    @NotNull        
    @Column(name = "hora_inicio", nullable = false)
    private String horaInicio;

    @NotNull        
    @Column(name = "hora_fim", nullable = false)
    private String horaFim;

    @ManyToOne
    private Dependencia dependencia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DisponibilidadeDependencia disponibilidadeDependencia = (DisponibilidadeDependencia) o;

        if ( ! Objects.equals(id, disponibilidadeDependencia.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DisponibilidadeDependencia{" +
                "id=" + id +
                ", diaSemana='" + diaSemana + "'" +
                ", horaInicio='" + horaInicio + "'" +
                ", horaFim='" + horaFim + "'" +
                '}';
    }
}
