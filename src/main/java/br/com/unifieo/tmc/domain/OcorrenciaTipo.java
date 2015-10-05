package br.com.unifieo.tmc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OcorrenciaTipo.
 */
@Entity
@Table(name = "OCORRENCIA_TIPO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OcorrenciaTipo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private OcorrenciaItem ocorrenciaItem;

    @OneToOne
    private OcorrenciaSubItem ocorrenciaSubItem;

    @OneToOne
    private OcorrenciaPrioridade ocorrenciaPrioridade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OcorrenciaItem getOcorrenciaItem() {
        return ocorrenciaItem;
    }

    public void setOcorrenciaItem(OcorrenciaItem ocorrenciaItem) {
        this.ocorrenciaItem = ocorrenciaItem;
    }

    public OcorrenciaSubItem getOcorrenciaSubItem() {
        return ocorrenciaSubItem;
    }

    public void setOcorrenciaSubItem(OcorrenciaSubItem ocorrenciaSubItem) {
        this.ocorrenciaSubItem = ocorrenciaSubItem;
    }

    public OcorrenciaPrioridade getOcorrenciaPrioridade() {
        return ocorrenciaPrioridade;
    }

    public void setOcorrenciaPrioridade(OcorrenciaPrioridade ocorrenciaPrioridade) {
        this.ocorrenciaPrioridade = ocorrenciaPrioridade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OcorrenciaTipo ocorrenciaTipo = (OcorrenciaTipo) o;

        if ( ! Objects.equals(id, ocorrenciaTipo.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OcorrenciaTipo{" +
                "id=" + id +
                '}';
    }
}
