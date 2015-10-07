package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Dependente;
import br.com.unifieo.tmc.repository.DependenteRepository;

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


/**
 * Test class for the DependenteResource REST controller.
 *
 * @see DependenteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DependenteResourceTest {

    private static final String DEFAULT_NOME = "SAMPLE_TEXT";
    private static final String UPDATED_NOME = "UPDATED_TEXT";
    private static final String DEFAULT_DOCUMENTO = "SAMPLE_TEXT";
    private static final String UPDATED_DOCUMENTO = "UPDATED_TEXT";

    @Inject
    private DependenteRepository dependenteRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDependenteMockMvc;

    private Dependente dependente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DependenteResource dependenteResource = new DependenteResource();
        ReflectionTestUtils.setField(dependenteResource, "dependenteRepository", dependenteRepository);
        this.restDependenteMockMvc = MockMvcBuilders.standaloneSetup(dependenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dependente = new Dependente();
        dependente.setNome(DEFAULT_NOME);
        dependente.setDocumento(DEFAULT_DOCUMENTO);
    }

    @Test
    @Transactional
    public void createDependente() throws Exception {
        int databaseSizeBeforeCreate = dependenteRepository.findAll().size();

        // Create the Dependente

        restDependenteMockMvc.perform(post("/api/dependentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dependente)))
                .andExpect(status().isCreated());

        // Validate the Dependente in the database
        List<Dependente> dependentes = dependenteRepository.findAll();
        assertThat(dependentes).hasSize(databaseSizeBeforeCreate + 1);
        Dependente testDependente = dependentes.get(dependentes.size() - 1);
        assertThat(testDependente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDependente.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependenteRepository.findAll().size();
        // set the field null
        dependente.setNome(null);

        // Create the Dependente, which fails.

        restDependenteMockMvc.perform(post("/api/dependentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dependente)))
                .andExpect(status().isBadRequest());

        List<Dependente> dependentes = dependenteRepository.findAll();
        assertThat(dependentes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDependentes() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependentes
        restDependenteMockMvc.perform(get("/api/dependentes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dependente.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO.toString())));
    }

    @Test
    @Transactional
    public void getDependente() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get the dependente
        restDependenteMockMvc.perform(get("/api/dependentes/{id}", dependente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dependente.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDependente() throws Exception {
        // Get the dependente
        restDependenteMockMvc.perform(get("/api/dependentes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDependente() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

		int databaseSizeBeforeUpdate = dependenteRepository.findAll().size();

        // Update the dependente
        dependente.setNome(UPDATED_NOME);
        dependente.setDocumento(UPDATED_DOCUMENTO);
        

        restDependenteMockMvc.perform(put("/api/dependentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dependente)))
                .andExpect(status().isOk());

        // Validate the Dependente in the database
        List<Dependente> dependentes = dependenteRepository.findAll();
        assertThat(dependentes).hasSize(databaseSizeBeforeUpdate);
        Dependente testDependente = dependentes.get(dependentes.size() - 1);
        assertThat(testDependente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDependente.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    public void deleteDependente() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

		int databaseSizeBeforeDelete = dependenteRepository.findAll().size();

        // Get the dependente
        restDependenteMockMvc.perform(delete("/api/dependentes/{id}", dependente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dependente> dependentes = dependenteRepository.findAll();
        assertThat(dependentes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
