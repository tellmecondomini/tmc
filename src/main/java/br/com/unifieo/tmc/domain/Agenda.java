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

/**
 * A Agenda.
 */
@Entity
@Table(name = "AGENDA")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Agenda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data", nullable = false)
    private DateTime data;

    @NotNull        
    @Column(name = "hora_inicio", nullable = false)
    private String horaInicio;

    @NotNull        
    @Column(name = "hora_fim", nullable = false)
    private String horaFim;

    @ManyToOne
    private Morador morador;

    @ManyToOne
    private Dependencia dependencia;

    @OneToMany(mappedBy = "agenda")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Convidado> convidados = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getData() {
        return data;
    }

    public void setData(DateTime data) {
        this.data = data;
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

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Set<Convidado> getConvidados() {
        return convidados;
    }

    public void setConvidados(Set<Convidado> convidados) {
        this.convidados = convidados;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Agenda agenda = (Agenda) o;

        if ( ! Objects.equals(id, agenda.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Agenda{" +
                "id=" + id +
                ", data='" + data + "'" +
                ", horaInicio='" + horaInicio + "'" +
                ", horaFim='" + horaFim + "'" +
                '}';
    }
}
