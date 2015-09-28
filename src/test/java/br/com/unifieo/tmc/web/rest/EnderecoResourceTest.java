package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Endereco;
import br.com.unifieo.tmc.repository.EnderecoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.unifieo.tmc.domain.enumeration.Uf;

/**
 * Test class for the EnderecoResource REST controller.
 *
 * @see EnderecoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EnderecoResourceTest {

    private static final String DEFAULT_LOGRADOURO = "SAMPLE_TEXT";
    private static final String UPDATED_LOGRADOURO = "UPDATED_TEXT";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;
    private static final String DEFAULT_BAIRRO = "SAMPLE_TEXT";
    private static final String UPDATED_BAIRRO = "UPDATED_TEXT";
    private static final String DEFAULT_CIDADE = "SAMPLE_TEXT";
    private static final String UPDATED_CIDADE = "UPDATED_TEXT";

    private static final Uf DEFAULT_UF = Uf.SP;
    private static final Uf UPDATED_UF = Uf.SP;

    @Inject
    private EnderecoRepository enderecoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEnderecoMockMvc;

    private Endereco endereco;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnderecoResource enderecoResource = new EnderecoResource();
        ReflectionTestUtils.setField(enderecoResource, "enderecoRepository", enderecoRepository);
        this.restEnderecoMockMvc = MockMvcBuilders.standaloneSetup(enderecoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        endereco = new Endereco();
        endereco.setLogradouro(DEFAULT_LOGRADOURO);
        endereco.setNumero(DEFAULT_NUMERO);
        endereco.setBairro(DEFAULT_BAIRRO);
        endereco.setCidade(DEFAULT_CIDADE);
        endereco.setUf(DEFAULT_UF);
    }

    @Test
    @Transactional
    public void createEndereco() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();

        // Create the Endereco

        restEnderecoMockMvc.perform(post("/api/enderecos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(endereco)))
                .andExpect(status().isCreated());

        // Validate the Endereco in the database
        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeCreate + 1);
        Endereco testEndereco = enderecos.get(enderecos.size() - 1);
        assertThat(testEndereco.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testEndereco.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEndereco.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testEndereco.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testEndereco.getUf()).isEqualTo(DEFAULT_UF);
    }

    @Test
    @Transactional
    public void checkLogradouroIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setLogradouro(null);

        // Create the Endereco, which fails.

        restEnderecoMockMvc.perform(post("/api/enderecos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(endereco)))
                .andExpect(status().isBadRequest());

        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setNumero(null);

        // Create the Endereco, which fails.

        restEnderecoMockMvc.perform(post("/api/enderecos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(endereco)))
                .andExpect(status().isBadRequest());

        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setCidade(null);

        // Create the Endereco, which fails.

        restEnderecoMockMvc.perform(post("/api/enderecos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(endereco)))
                .andExpect(status().isBadRequest());

        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnderecos() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecos
        restEnderecoMockMvc.perform(get("/api/enderecos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
                .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO.toString())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
                .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
                .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
                .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF.toString())));
    }

    @Test
    @Transactional
    public void getEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get the endereco
        restEnderecoMockMvc.perform(get("/api/enderecos/{id}", endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(endereco.getId().intValue()))
            .andExpect(jsonPath("$.logradouro").value(DEFAULT_LOGRADOURO.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEndereco() throws Exception {
        // Get the endereco
        restEnderecoMockMvc.perform(get("/api/enderecos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

		int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Update the endereco
        endereco.setLogradouro(UPDATED_LOGRADOURO);
        endereco.setNumero(UPDATED_NUMERO);
        endereco.setBairro(UPDATED_BAIRRO);
        endereco.setCidade(UPDATED_CIDADE);
        endereco.setUf(UPDATED_UF);
        

        restEnderecoMockMvc.perform(put("/api/enderecos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(endereco)))
                .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecos.get(enderecos.size() - 1);
        assertThat(testEndereco.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEndereco.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testEndereco.getUf()).isEqualTo(UPDATED_UF);
    }

    @Test
    @Transactional
    public void deleteEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

		int databaseSizeBeforeDelete = enderecoRepository.findAll().size();

        // Get the endereco
        restEnderecoMockMvc.perform(delete("/api/enderecos/{id}", endereco.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
