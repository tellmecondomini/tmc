package br.com.unifieo.tmc.domain;

import br.com.unifieo.tmc.domain.util.CustomDateTimeDeserializer;
import br.com.unifieo.tmc.domain.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Topico.
 */
@Entity
@Table(name = "TOPICO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Topico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotNull
    @Column(name = "conteudo", nullable = false)
    private String conteudo;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data", nullable = false)
    private DateTime data;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_topico")
    private StatusTopico statusTopico;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data_fechamento", nullable = false)
    private DateTime dataFechamento;

    @Column(name = "prioritario")
    private Boolean prioritario;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data_inicio", nullable = false)
    private DateTime dataInicio;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "data_fim", nullable = false)
    private DateTime dataFim;

    @Column(name="mensagem_aprovacao")
    private String mensagemAprovacao;

    @ManyToOne
    private Assunto assunto;

    @ManyToOne
    private Categoria categoria;

    @ManyToOne
    private Morador morador;

    @ManyToOne
    private Funcionario funcionario;

    @ManyToOne
    private Funcionario funcionarioAprovacao;

    @OneToMany(mappedBy = "topico")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comentario> comentarios = new HashSet<>();

    @OneToMany(mappedBy = "topico")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ImagemTopico> imagens = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public DateTime getData() {
        return data;
    }

    public void setData(DateTime data) {
        this.data = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public StatusTopico getStatusTopico() {
        return statusTopico;
    }

    public void setStatusTopico(StatusTopico statusTopico) {
        this.statusTopico = statusTopico;
    }

    public Assunto getAssunto() {
        return assunto;
    }

    public void setAssunto(Assunto assunto) {
        this.assunto = assunto;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public DateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(DateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Boolean getPrioritario() {
        return prioritario;
    }

    public void setPrioritario(Boolean prioritario) {
        this.prioritario = prioritario;
    }

    public DateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(DateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public DateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(DateTime dataFim) {
        this.dataFim = dataFim;
    }

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Set<ImagemTopico> getImagens() {
        return imagens;
    }

    public void setImagens(Set<ImagemTopico> imagens) {
        this.imagens = imagens;
    }

    public String getMensagemAprovacao() {
        return mensagemAprovacao;
    }

    public void setMensagemAprovacao(String mensagemAprovacao) {
        this.mensagemAprovacao = mensagemAprovacao;
    }

    public Funcionario getFuncionarioAprovacao() {
        return funcionarioAprovacao;
    }

    public void setFuncionarioAprovacao(Funcionario funcionarioAprovacao) {
        this.funcionarioAprovacao = funcionarioAprovacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Topico topico = (Topico) o;

        if (!Objects.equals(id, topico.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Topico{" +
            "id=" + id +
            ", conteudo='" + conteudo + "'" +
            ", data='" + data + "'" +
            '}';
    }
}
