package br.com.unifieo.tmc.web.rest.dto;

import br.com.unifieo.tmc.domain.Cep;
import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.domain.enumeration.Uf;
import org.joda.time.DateTime;

public class FuncionarioDTO {

    private Long id;
    private String nome;
    private String cpf;
    private Sexo sexo;
    private DateTime dataNascimento;
    private DateTime dataCadastro;
    private String email;
    private String senha;
    private Boolean ativo;

    private Long cepId;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private Uf uf;
    private Integer numero;
    private String complemento;

    private Long condominioId;

    public FuncionarioDTO() {

    }

    public FuncionarioDTO(Funcionario funcionario) {
        this.id = funcionario.getId();
        this.nome = funcionario.getNome();
        this.cpf = String.valueOf(funcionario.getCpf());
        this.sexo = funcionario.getSexo();
        this.dataNascimento = new DateTime(funcionario.getDataNascimento());
        this.dataCadastro = new DateTime(funcionario.getDataCadastro());
        this.email = funcionario.getEmail();
        this.senha = funcionario.getSenha();
        this.ativo = true;
        this.numero = funcionario.getNumero();
        this.complemento = funcionario.getComplemento();
        this.condominioId = funcionario.getCondominio().getId();
        Cep cep = funcionario.getCep();
        if (cep != null) {
            this.cepId = cep.getId();
            this.cep = cep.getCep();
            this.logradouro = cep.getLogradouro();
            this.bairro = cep.getBairro();
            this.cidade = cep.getCidade();
            this.uf = cep.getUf();
        }
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

    public DateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(DateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
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

    public Long getCepId() {
        return cepId;
    }

    public void setCepId(Long cepId) {
        this.cepId = cepId;
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

        FuncionarioDTO that = (FuncionarioDTO) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
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
