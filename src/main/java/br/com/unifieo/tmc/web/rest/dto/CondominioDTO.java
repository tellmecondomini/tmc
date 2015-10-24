package br.com.unifieo.tmc.web.rest.dto;

import br.com.unifieo.tmc.domain.enumeration.Disposicao;
import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.domain.enumeration.Uf;

import java.util.Date;

public class CondominioDTO {

    private Long id;
    private String razaoSocial;
    private String cnpj;
    private Disposicao disposicao;

    // Cep Condominio

    private Integer condominioCep;
    private String condominioLogradouro;
    private String condominioBairro;
    private String condominioCidade;
    private Uf condominioUf;
    private Integer condominioNumero;
    private String condominioComplemento;

    // Dados do responsavel

    private String responsavelNome;
    private Integer responsavelCpf;
    private Sexo responsavelSexo;
    private Date responsavelDataNascimento;
    private String responsavelEmail;
    private String responsavelSenha;

    // Cep responsavel

    private Integer responsavelCep;
    private String responsavelLogradouro;
    private String responsavelBairro;
    private String responsavelCidade;
    private Uf responsavelUf;
    private Integer responsavelNumero;
    private String responsavelComplemento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Disposicao getDisposicao() {
        return disposicao;
    }

    public void setDisposicao(Disposicao disposicao) {
        this.disposicao = disposicao;
    }

    public Integer getCondominioCep() {
        return condominioCep;
    }

    public void setCondominioCep(Integer condominioCep) {
        this.condominioCep = condominioCep;
    }

    public String getCondominioLogradouro() {
        return condominioLogradouro;
    }

    public void setCondominioLogradouro(String condominioLogradouro) {
        this.condominioLogradouro = condominioLogradouro;
    }

    public String getCondominioBairro() {
        return condominioBairro;
    }

    public void setCondominioBairro(String condominioBairro) {
        this.condominioBairro = condominioBairro;
    }

    public String getCondominioCidade() {
        return condominioCidade;
    }

    public void setCondominioCidade(String condominioCidade) {
        this.condominioCidade = condominioCidade;
    }

    public Uf getCondominioUf() {
        return condominioUf;
    }

    public void setCondominioUf(Uf condominioUf) {
        this.condominioUf = condominioUf;
    }

    public Integer getCondominioNumero() {
        return condominioNumero;
    }

    public void setCondominioNumero(Integer condominioNumero) {
        this.condominioNumero = condominioNumero;
    }

    public String getCondominioComplemento() {
        return condominioComplemento;
    }

    public void setCondominioComplemento(String condominioComplemento) {
        this.condominioComplemento = condominioComplemento;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public void setResponsavelNome(String responsavelNome) {
        this.responsavelNome = responsavelNome;
    }

    public Integer getResponsavelCpf() {
        return responsavelCpf;
    }

    public void setResponsavelCpf(Integer responsavelCpf) {
        this.responsavelCpf = responsavelCpf;
    }

    public Sexo getResponsavelSexo() {
        return responsavelSexo;
    }

    public void setResponsavelSexo(Sexo responsavelSexo) {
        this.responsavelSexo = responsavelSexo;
    }

    public Date getResponsavelDataNascimento() {
        return responsavelDataNascimento;
    }

    public void setResponsavelDataNascimento(Date responsavelDataNascimento) {
        this.responsavelDataNascimento = responsavelDataNascimento;
    }

    public String getResponsavelEmail() {
        return responsavelEmail;
    }

    public void setResponsavelEmail(String responsavelEmail) {
        this.responsavelEmail = responsavelEmail;
    }

    public String getResponsavelSenha() {
        return responsavelSenha;
    }

    public void setResponsavelSenha(String responsavelSenha) {
        this.responsavelSenha = responsavelSenha;
    }

    public Integer getResponsavelCep() {
        return responsavelCep;
    }

    public void setResponsavelCep(Integer responsavelCep) {
        this.responsavelCep = responsavelCep;
    }

    public String getResponsavelLogradouro() {
        return responsavelLogradouro;
    }

    public void setResponsavelLogradouro(String responsavelLogradouro) {
        this.responsavelLogradouro = responsavelLogradouro;
    }

    public String getResponsavelBairro() {
        return responsavelBairro;
    }

    public void setResponsavelBairro(String responsavelBairro) {
        this.responsavelBairro = responsavelBairro;
    }

    public String getResponsavelCidade() {
        return responsavelCidade;
    }

    public void setResponsavelCidade(String responsavelCidade) {
        this.responsavelCidade = responsavelCidade;
    }

    public Uf getResponsavelUf() {
        return responsavelUf;
    }

    public void setResponsavelUf(Uf responsavelUf) {
        this.responsavelUf = responsavelUf;
    }

    public Integer getResponsavelNumero() {
        return responsavelNumero;
    }

    public void setResponsavelNumero(Integer responsavelNumero) {
        this.responsavelNumero = responsavelNumero;
    }

    public String getResponsavelComplemento() {
        return responsavelComplemento;
    }

    public void setResponsavelComplemento(String responsavelComplemento) {
        this.responsavelComplemento = responsavelComplemento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CondominioDTO that = (CondominioDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (razaoSocial != null ? !razaoSocial.equals(that.razaoSocial) : that.razaoSocial != null) return false;
        return !(cnpj != null ? !cnpj.equals(that.cnpj) : that.cnpj != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (razaoSocial != null ? razaoSocial.hashCode() : 0);
        result = 31 * result + (cnpj != null ? cnpj.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CondominioDTO{" +
            "id=" + id +
            ", razaoSocial='" + razaoSocial + '\'' +
            ", cnpj='" + cnpj + '\'' +
            '}';
    }
}
