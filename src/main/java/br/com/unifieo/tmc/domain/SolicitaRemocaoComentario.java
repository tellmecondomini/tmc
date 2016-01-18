package br.com.unifieo.tmc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import br.com.unifieo.tmc.domain.util.CustomDateTimeDeserializer;
import br.com.unifieo.tmc.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SolicitaRemocaoComentario.
 */
@Entity
@Table(name = "SOLICITA_REMOCAO_COMENTARIO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SolicitaRemocaoComentario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data")
    private DateTime data;
    
    @Column(name = "motivo")
    private String motivo;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data_atendimento")
    private DateTime dataAtendimento;
    
    @Column(name = "observacao")
    private String observacao;

    @OneToOne
    private Comentario comentario;

    @ManyToOne
    private Morador morador;

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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public DateTime getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(DateTime dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Comentario getComentario() {
        return comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SolicitaRemocaoComentario solicitaRemocaoComentario = (SolicitaRemocaoComentario) o;

        if ( ! Objects.equals(id, solicitaRemocaoComentario.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SolicitaRemocaoComentario{" +
                "id=" + id +
                ", data='" + data + "'" +
                ", motivo='" + motivo + "'" +
                ", dataAtendimento='" + dataAtendimento + "'" +
                ", observacao='" + observacao + "'" +
                '}';
    }
}
