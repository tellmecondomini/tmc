package br.com.unifieo.tmc.web.rest.dto;

import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.domain.enumeration.Uf;
import org.joda.time.DateTime;

public class FuncionarioDTO {

    private Long id;
    private String nome;
    private String cpf;
    private Sexo sexo;
    private DateTime dataNascimento;
    private String email;
    private String senha;
    private Boolean ativo;
    private Boolean responsavel;

    // cep funcionario

    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private Uf uf;
    private Integer numero;
    private String complemento;

    public FuncionarioDTO() {

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

    public DateTime getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(DateTime dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Boolean responsavel) {
        this.responsavel = responsavel;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Uf getUf() {
        return uf;
    }

    public void setUf(Uf uf) {
        this.uf = uf;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FuncionarioDTO that = (FuncionarioDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (nome != null ? !nome.equals(that.nome) : that.nome != null) return false;
        if (cpf != null ? !cpf.equals(that.cpf) : that.cpf != null) return false;
        if (sexo != that.sexo) return false;
        if (dataNascimento != null ? !dataNascimento.equals(that.dataNascimento) : that.dataNascimento != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (senha != null ? !senha.equals(that.senha) : that.senha != null) return false;
        if (ativo != null ? !ativo.equals(that.ativo) : that.ativo != null) return false;
        if (responsavel != null ? !responsavel.equals(that.responsavel) : that.responsavel != null) return false;
        if (cep != null ? !cep.equals(that.cep) : that.cep != null) return false;
        if (logradouro != null ? !logradouro.equals(that.logradouro) : that.logradouro != null) return false;
        if (bairro != null ? !bairro.equals(that.bairro) : that.bairro != null) return false;
        if (cidade != null ? !cidade.equals(that.cidade) : that.cidade != null) return false;
        if (uf != that.uf) return false;
        if (numero != null ? !numero.equals(that.numero) : that.numero != null) return false;
        return !(complemento != null ? !complemento.equals(that.complemento) : that.complemento != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (cpf != null ? cpf.hashCode() : 0);
        result = 31 * result + (sexo != null ? sexo.hashCode() : 0);
        result = 31 * result + (dataNascimento != null ? dataNascimento.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (senha != null ? senha.hashCode() : 0);
        result = 31 * result + (ativo != null ? ativo.hashCode() : 0);
        result = 31 * result + (responsavel != null ? responsavel.hashCode() : 0);
        result = 31 * result + (cep != null ? cep.hashCode() : 0);
        result = 31 * result + (logradouro != null ? logradouro.hashCode() : 0);
        result = 31 * result + (bairro != null ? bairro.hashCode() : 0);
        result = 31 * result + (cidade != null ? cidade.hashCode() : 0);
        result = 31 * result + (uf != null ? uf.hashCode() : 0);
        result = 31 * result + (numero != null ? numero.hashCode() : 0);
        result = 31 * result + (complemento != null ? complemento.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FuncionarioDTO{" +
            "id=" + id +
            ", nome='" + nome + '\'' +
            ", cpf='" + cpf + '\'' +
            ", sexo=" + sexo +
            ", dataNascimento=" + dataNascimento +
            ", email='" + email + '\'' +
            ", senha='" + senha + '\'' +
            ", ativo=" + ativo +
            ", responsavel=" + responsavel +
            ", cep=" + cep +
            ", logradouro='" + logradouro + '\'' +
            ", bairro='" + bairro + '\'' +
            ", cidade='" + cidade + '\'' +
            ", uf=" + uf +
            ", numero=" + numero +
            ", complemento='" + complemento + '\'' +
            '}';
    }
}
