package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Cep;
import br.com.unifieo.tmc.repository.CepRepository;

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
 * Test class for the CepResource REST controller.
 *
 * @see CepResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CepResourceTest {

    private static final String DEFAULT_LOGRADOURO = "SAMPLE_TEXT";
    private static final String UPDATED_LOGRADOURO = "UPDATED_TEXT";
    private static final String DEFAULT_BAIRRO = "SAMPLE_TEXT";
    private static final String UPDATED_BAIRRO = "UPDATED_TEXT";
    private static final String DEFAULT_CIDADE = "SAMPLE_TEXT";
    private static final String UPDATED_CIDADE = "UPDATED_TEXT";

    private static final Uf DEFAULT_UF = Uf.AC;
    private static final Uf UPDATED_UF = Uf.AL;

    private static final Integer DEFAULT_CEP = 1;
    private static final Integer UPDATED_CEP = 2;

    @Inject
    private CepRepository cepRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCepMockMvc;

    private Cep cep;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CepResource cepResource = new CepResource();
        ReflectionTestUtils.setField(cepResource, "cepRepository", cepRepository);
        this.restCepMockMvc = MockMvcBuilders.standaloneSetup(cepResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cep = new Cep();
        cep.setLogradouro(DEFAULT_LOGRADOURO);
        cep.setBairro(DEFAULT_BAIRRO);
        cep.setCidade(DEFAULT_CIDADE);
        cep.setUf(DEFAULT_UF);
        cep.setCep(DEFAULT_CEP);
    }

    @Test
    @Transactional
    public void createCep() throws Exception {
        int databaseSizeBeforeCreate = cepRepository.findAll().size();

        // Create the Cep

        restCepMockMvc.perform(post("/api/ceps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cep)))
                .andExpect(status().isCreated());

        // Validate the Cep in the database
        List<Cep> ceps = cepRepository.findAll();
        assertThat(ceps).hasSize(databaseSizeBeforeCreate + 1);
        Cep testCep = ceps.get(ceps.size() - 1);
        assertThat(testCep.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testCep.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testCep.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testCep.getUf()).isEqualTo(DEFAULT_UF);
        assertThat(testCep.getCep()).isEqualTo(DEFAULT_CEP);
    }

    @Test
    @Transactional
    public void checkLogradouroIsRequired() throws Exception {
        int databaseSizeBeforeTest = cepRepository.findAll().size();
        // set the field null
        cep.setLogradouro(null);

        // Create the Cep, which fails.

        restCepMockMvc.perform(post("/api/ceps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cep)))
                .andExpect(status().isBadRequest());

        List<Cep> ceps = cepRepository.findAll();
        assertThat(ceps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBairroIsRequired() throws Exception {
        int databaseSizeBeforeTest = cepRepository.findAll().size();
        // set the field null
        cep.setBairro(null);

        // Create the Cep, which fails.

        restCepMockMvc.perform(post("/api/ceps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cep)))
                .andExpect(status().isBadRequest());

        List<Cep> ceps = cepRepository.findAll();
        assertThat(ceps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cepRepository.findAll().size();
        // set the field null
        cep.setCidade(null);

        // Create the Cep, which fails.

        restCepMockMvc.perform(post("/api/ceps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cep)))
                .andExpect(status().isBadRequest());

        List<Cep> ceps = cepRepository.findAll();
        assertThat(ceps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUfIsRequired() throws Exception {
        int databaseSizeBeforeTest = cepRepository.findAll().size();
        // set the field null
        cep.setUf(null);

        // Create the Cep, which fails.

        restCepMockMvc.perform(post("/api/ceps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cep)))
                .andExpect(status().isBadRequest());

        List<Cep> ceps = cepRepository.findAll();
        assertThat(ceps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCepIsRequired() throws Exception {
        int databaseSizeBeforeTest = cepRepository.findAll().size();
        // set the field null
        cep.setCep(null);

        // Create the Cep, which fails.

        restCepMockMvc.perform(post("/api/ceps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cep)))
                .andExpect(status().isBadRequest());

        List<Cep> ceps = cepRepository.findAll();
        assertThat(ceps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCeps() throws Exception {
        // Initialize the database
        cepRepository.saveAndFlush(cep);

        // Get all the ceps
        restCepMockMvc.perform(get("/api/ceps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cep.getId().intValue())))
                .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO.toString())))
                .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
                .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
                .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF.toString())))
                .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)));
    }

    @Test
    @Transactional
    public void getCep() throws Exception {
        // Initialize the database
        cepRepository.saveAndFlush(cep);

        // Get the cep
        restCepMockMvc.perform(get("/api/ceps/{id}", cep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cep.getId().intValue()))
            .andExpect(jsonPath("$.logradouro").value(DEFAULT_LOGRADOURO.toString()))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP));
    }

    @Test
    @Transactional
    public void getNonExistingCep() throws Exception {
        // Get the cep
        restCepMockMvc.perform(get("/api/ceps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCep() throws Exception {
        // Initialize the database
        cepRepository.saveAndFlush(cep);

		int databaseSizeBeforeUpdate = cepRepository.findAll().size();

        // Update the cep
        cep.setLogradouro(UPDATED_LOGRADOURO);
        cep.setBairro(UPDATED_BAIRRO);
        cep.setCidade(UPDATED_CIDADE);
        cep.setUf(UPDATED_UF);
        cep.setCep(UPDATED_CEP);
        

        restCepMockMvc.perform(put("/api/ceps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cep)))
                .andExpect(status().isOk());

        // Validate the Cep in the database
        List<Cep> ceps = cepRepository.findAll();
        assertThat(ceps).hasSize(databaseSizeBeforeUpdate);
        Cep testCep = ceps.get(ceps.size() - 1);
        assertThat(testCep.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testCep.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testCep.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testCep.getUf()).isEqualTo(UPDATED_UF);
        assertThat(testCep.getCep()).isEqualTo(UPDATED_CEP);
    }

    @Test
    @Transactional
    public void deleteCep() throws Exception {
        // Initialize the database
        cepRepository.saveAndFlush(cep);

		int databaseSizeBeforeDelete = cepRepository.findAll().size();

        // Get the cep
        restCepMockMvc.perform(delete("/api/ceps/{id}", cep.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Cep> ceps = cepRepository.findAll();
        assertThat(ceps).hasSize(databaseSizeBeforeDelete - 1);
    }
}
