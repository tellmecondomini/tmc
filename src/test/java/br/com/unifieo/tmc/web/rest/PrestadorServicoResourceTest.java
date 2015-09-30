package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.PrestadorServico;
import br.com.unifieo.tmc.domain.enumeration.Pessoa;
import br.com.unifieo.tmc.repository.PrestadorServicoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PrestadorServicoResource REST controller.
 *
 * @see PrestadorServicoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrestadorServicoResourceTest {

    private static final String DEFAULT_NOME = "SAMPLE_TEXT";
    private static final String UPDATED_NOME = "UPDATED_TEXT";
    private static final String DEFAULT_EMAIL = "tellme@condominium.com.br";
    private static final String UPDATED_EMAIL = "tellme@condominium.com.br";

    private static final Integer DEFAULT_TELEFONE = 1;
    private static final Integer UPDATED_TELEFONE = 2;
    private static final String DEFAULT_DOCUMENTO = "66.741.263/0001-60";
    private static final String UPDATED_DOCUMENTO = "66.741.263/0001-60";

    private static final Pessoa DEFAULT_PESSOA = Pessoa.FISICA;
    private static final Pessoa UPDATED_PESSOA = Pessoa.JURIDICA;

    @Inject
    private PrestadorServicoRepository prestadorServicoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPrestadorServicoMockMvc;

    private PrestadorServico prestadorServico;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrestadorServicoResource prestadorServicoResource = new PrestadorServicoResource();
        ReflectionTestUtils.setField(prestadorServicoResource, "prestadorServicoRepository", prestadorServicoRepository);
        this.restPrestadorServicoMockMvc = MockMvcBuilders.standaloneSetup(prestadorServicoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        prestadorServico = new PrestadorServico();
        prestadorServico.setNome(DEFAULT_NOME);
        prestadorServico.setEmail(DEFAULT_EMAIL);
        prestadorServico.setTelefone(DEFAULT_TELEFONE);
        prestadorServico.setDocumento(DEFAULT_DOCUMENTO);
        prestadorServico.setPessoa(DEFAULT_PESSOA);
    }

    @Test
    @Transactional
    public void createPrestadorServico() throws Exception {
        int databaseSizeBeforeCreate = prestadorServicoRepository.findAll().size();

        // Create the PrestadorServico

        restPrestadorServicoMockMvc.perform(post("/api/prestadorServicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prestadorServico)))
                .andExpect(status().isCreated());

        // Validate the PrestadorServico in the database
        List<PrestadorServico> prestadorServicos = prestadorServicoRepository.findAll();
        assertThat(prestadorServicos).hasSize(databaseSizeBeforeCreate + 1);
        PrestadorServico testPrestadorServico = prestadorServicos.get(prestadorServicos.size() - 1);
        assertThat(testPrestadorServico.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPrestadorServico.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPrestadorServico.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testPrestadorServico.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testPrestadorServico.getPessoa()).isEqualTo(DEFAULT_PESSOA);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = prestadorServicoRepository.findAll().size();
        // set the field null
        prestadorServico.setNome(null);

        // Create the PrestadorServico, which fails.

        restPrestadorServicoMockMvc.perform(post("/api/prestadorServicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prestadorServico)))
                .andExpect(status().isBadRequest());

        List<PrestadorServico> prestadorServicos = prestadorServicoRepository.findAll();
        assertThat(prestadorServicos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = prestadorServicoRepository.findAll().size();
        // set the field null
        prestadorServico.setDocumento(null);

        // Create the PrestadorServico, which fails.

        restPrestadorServicoMockMvc.perform(post("/api/prestadorServicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prestadorServico)))
                .andExpect(status().isBadRequest());

        List<PrestadorServico> prestadorServicos = prestadorServicoRepository.findAll();
        assertThat(prestadorServicos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrestadorServicos() throws Exception {
        // Initialize the database
        prestadorServicoRepository.saveAndFlush(prestadorServico);

        // Get all the prestadorServicos
        restPrestadorServicoMockMvc.perform(get("/api/prestadorServicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(prestadorServico.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
                .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO.toString())))
                .andExpect(jsonPath("$.[*].pessoa").value(hasItem(DEFAULT_PESSOA.toString())));
    }

    @Test
    @Transactional
    public void getPrestadorServico() throws Exception {
        // Initialize the database
        prestadorServicoRepository.saveAndFlush(prestadorServico);

        // Get the prestadorServico
        restPrestadorServicoMockMvc.perform(get("/api/prestadorServicos/{id}", prestadorServico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prestadorServico.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO.toString()))
            .andExpect(jsonPath("$.pessoa").value(DEFAULT_PESSOA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrestadorServico() throws Exception {
        // Get the prestadorServico
        restPrestadorServicoMockMvc.perform(get("/api/prestadorServicos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrestadorServico() throws Exception {
        // Initialize the database
        prestadorServicoRepository.saveAndFlush(prestadorServico);

		int databaseSizeBeforeUpdate = prestadorServicoRepository.findAll().size();

        // Update the prestadorServico
        prestadorServico.setNome(UPDATED_NOME);
        prestadorServico.setEmail(UPDATED_EMAIL);
        prestadorServico.setTelefone(UPDATED_TELEFONE);
        prestadorServico.setDocumento(UPDATED_DOCUMENTO);
        prestadorServico.setPessoa(UPDATED_PESSOA);


        restPrestadorServicoMockMvc.perform(put("/api/prestadorServicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prestadorServico)))
                .andExpect(status().isOk());

        // Validate the PrestadorServico in the database
        List<PrestadorServico> prestadorServicos = prestadorServicoRepository.findAll();
        assertThat(prestadorServicos).hasSize(databaseSizeBeforeUpdate);
        PrestadorServico testPrestadorServico = prestadorServicos.get(prestadorServicos.size() - 1);
        assertThat(testPrestadorServico.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPrestadorServico.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPrestadorServico.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testPrestadorServico.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testPrestadorServico.getPessoa()).isEqualTo(UPDATED_PESSOA);
    }

    @Test
    @Transactional
    public void deletePrestadorServico() throws Exception {
        // Initialize the database
        prestadorServicoRepository.saveAndFlush(prestadorServico);

		int databaseSizeBeforeDelete = prestadorServicoRepository.findAll().size();

        // Get the prestadorServico
        restPrestadorServicoMockMvc.perform(delete("/api/prestadorServicos/{id}", prestadorServico.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PrestadorServico> prestadorServicos = prestadorServicoRepository.findAll();
        assertThat(prestadorServicos).hasSize(databaseSizeBeforeDelete - 1);
    }
}