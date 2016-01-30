package br.com.unifieo.tmc.web.rest.dto;

import br.com.unifieo.tmc.domain.AvaliaCompetencia;
import br.com.unifieo.tmc.domain.Comentario;
import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.domain.Topico;

import java.util.List;

public class AtividadesMoradorDTO {

    private Morador morador;
    private List<Topico> topicosAbertos;
    private List<Comentario> comentariosRealizados;
    private List<AvaliaCompetencia> avaliacoesRealizadas;

    public AtividadesMoradorDTO() {

    }

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
    }

    public List<Topico> getTopicosAbertos() {
        return topicosAbertos;
    }

    public void setTopicosAbertos(List<Topico> topicosAbertos) {
        this.topicosAbertos = topicosAbertos;
    }

    public List<Comentario> getComentariosRealizados() {
        return comentariosRealizados;
    }

    public void setComentariosRealizados(List<Comentario> comentariosRealizados) {
        this.comentariosRealizados = comentariosRealizados;
    }

    public List<AvaliaCompetencia> getAvaliacoesRealizadas() {
        return avaliacoesRealizadas;
    }

    public void setAvaliacoesRealizadas(List<AvaliaCompetencia> avaliacoesRealizadas) {
        this.avaliacoesRealizadas = avaliacoesRealizadas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AtividadesMoradorDTO that = (AtividadesMoradorDTO) o;

        if (morador != null ? !morador.equals(that.morador) : that.morador != null) return false;
        if (topicosAbertos != null ? !topicosAbertos.equals(that.topicosAbertos) : that.topicosAbertos != null)
            return false;
        if (comentariosRealizados != null ? !comentariosRealizados.equals(that.comentariosRealizados) : that.comentariosRealizados != null)
            return false;
        return avaliacoesRealizadas != null ? avaliacoesRealizadas.equals(that.avaliacoesRealizadas) : that.avaliacoesRealizadas == null;

    }

    @Override
    public int hashCode() {
        int result = morador != null ? morador.hashCode() : 0;
        result = 31 * result + (topicosAbertos != null ? topicosAbertos.hashCode() : 0);
        result = 31 * result + (comentariosRealizados != null ? comentariosRealizados.hashCode() : 0);
        result = 31 * result + (avaliacoesRealizadas != null ? avaliacoesRealizadas.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AtividadesMoradorDTO{" +
            "morador=" + morador +
            ", topicosAbertos=" + topicosAbertos +
            ", comentariosRealizados=" + comentariosRealizados +
            ", avaliacoesRealizadas=" + avaliacoesRealizadas +
            '}';
    }
}
