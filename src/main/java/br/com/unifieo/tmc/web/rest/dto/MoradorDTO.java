package br.com.unifieo.tmc.web.rest.dto;

import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.domain.enumeration.TipoMorador;
import org.joda.time.DateTime;

/**
 * Created by Arthemus on 20/01/2016.
 */
public class MoradorDTO {

    private Long id;

    private String nome;

    private String cpf;

    private Sexo sexo;

    private String email;

    private String senha;

    private DateTime dataNascimento;

    private Boolean ativo;

    private TipoMorador tipo;

    private Long condominioId;

    public MoradorDTO() {

    }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public DateTime getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(DateTime dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public TipoMorador getTipo() {
        return tipo;
    }

    public void setTipo(TipoMorador tipo) {
        this.tipo = tipo;
    }

    public Long getCondominioId() {
        return condominioId;
    }

    public void setCondominioId(Long condominioId) {
        this.condominioId = condominioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoradorDTO that = (MoradorDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (nome != null ? !nome.equals(that.nome) : that.nome != null) return false;
        if (cpf != null ? !cpf.equals(that.cpf) : that.cpf != null) return false;
        if (sexo != that.sexo) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (senha != null ? !senha.equals(that.senha) : that.senha != null) return false;
        if (dataNascimento != null ? !dataNascimento.equals(that.dataNascimento) : that.dataNascimento != null)
            return false;
        if (ativo != null ? !ativo.equals(that.ativo) : that.ativo != null) return false;
        if (tipo != that.tipo) return false;
        return condominioId != null ? condominioId.equals(that.condominioId) : that.condominioId == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (cpf != null ? cpf.hashCode() : 0);
        result = 31 * result + (sexo != null ? sexo.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (senha != null ? senha.hashCode() : 0);
        result = 31 * result + (dataNascimento != null ? dataNascimento.hashCode() : 0);
        result = 31 * result + (ativo != null ? ativo.hashCode() : 0);
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        result = 31 * result + (condominioId != null ? condominioId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MoradorDTO{" +
            "id=" + id +
            ", nome='" + nome + '\'' +
            ", cpf='" + cpf + '\'' +
            ", sexo=" + sexo +
            ", email='" + email + '\'' +
            ", senha='" + senha + '\'' +
            ", dataNascimento=" + dataNascimento +
            ", ativo=" + ativo +
            ", tipo=" + tipo +
            ", condominioId=" + condominioId +
            '}';
    }
}
