package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.OcorrenciaPrioridade;
import br.com.unifieo.tmc.repository.OcorrenciaPrioridadeRepository;

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
 * Test class for the OcorrenciaPrioridadeResource REST controller.
 *
 * @see OcorrenciaPrioridadeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OcorrenciaPrioridadeResourceTest {

    private static final String DEFAULT_DESCRICAO = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRICAO = "UPDATED_TEXT";

    @Inject
    private OcorrenciaPrioridadeRepository ocorrenciaPrioridadeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOcorrenciaPrioridadeMockMvc;

    private OcorrenciaPrioridade ocorrenciaPrioridade;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OcorrenciaPrioridadeResource ocorrenciaPrioridadeResource = new OcorrenciaPrioridadeResource();
        ReflectionTestUtils.setField(ocorrenciaPrioridadeResource, "ocorrenciaPrioridadeRepository", ocorrenciaPrioridadeRepository);
        this.restOcorrenciaPrioridadeMockMvc = MockMvcBuilders.standaloneSetup(ocorrenciaPrioridadeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ocorrenciaPrioridade = new OcorrenciaPrioridade();
        ocorrenciaPrioridade.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createOcorrenciaPrioridade() throws Exception {
        int databaseSizeBeforeCreate = ocorrenciaPrioridadeRepository.findAll().size();

        // Create the OcorrenciaPrioridade

        restOcorrenciaPrioridadeMockMvc.perform(post("/api/ocorrenciaPrioridades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaPrioridade)))
                .andExpect(status().isCreated());

        // Validate the OcorrenciaPrioridade in the database
        List<OcorrenciaPrioridade> ocorrenciaPrioridades = ocorrenciaPrioridadeRepository.findAll();
        assertThat(ocorrenciaPrioridades).hasSize(databaseSizeBeforeCreate + 1);
        OcorrenciaPrioridade testOcorrenciaPrioridade = ocorrenciaPrioridades.get(ocorrenciaPrioridades.size() - 1);
        assertThat(testOcorrenciaPrioridade.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ocorrenciaPrioridadeRepository.findAll().size();
        // set the field null
        ocorrenciaPrioridade.setDescricao(null);

        // Create the OcorrenciaPrioridade, which fails.

        restOcorrenciaPrioridadeMockMvc.perform(post("/api/ocorrenciaPrioridades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaPrioridade)))
                .andExpect(status().isBadRequest());

        List<OcorrenciaPrioridade> ocorrenciaPrioridades = ocorrenciaPrioridadeRepository.findAll();
        assertThat(ocorrenciaPrioridades).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOcorrenciaPrioridades() throws Exception {
        // Initialize the database
        ocorrenciaPrioridadeRepository.saveAndFlush(ocorrenciaPrioridade);

        // Get all the ocorrenciaPrioridades
        restOcorrenciaPrioridadeMockMvc.perform(get("/api/ocorrenciaPrioridades"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ocorrenciaPrioridade.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getOcorrenciaPrioridade() throws Exception {
        // Initialize the database
        ocorrenciaPrioridadeRepository.saveAndFlush(ocorrenciaPrioridade);

        // Get the ocorrenciaPrioridade
        restOcorrenciaPrioridadeMockMvc.perform(get("/api/ocorrenciaPrioridades/{id}", ocorrenciaPrioridade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ocorrenciaPrioridade.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOcorrenciaPrioridade() throws Exception {
        // Get the ocorrenciaPrioridade
        restOcorrenciaPrioridadeMockMvc.perform(get("/api/ocorrenciaPrioridades/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOcorrenciaPrioridade() throws Exception {
        // Initialize the database
        ocorrenciaPrioridadeRepository.saveAndFlush(ocorrenciaPrioridade);

		int databaseSizeBeforeUpdate = ocorrenciaPrioridadeRepository.findAll().size();

        // Update the ocorrenciaPrioridade
        ocorrenciaPrioridade.setDescricao(UPDATED_DESCRICAO);
        

        restOcorrenciaPrioridadeMockMvc.perform(put("/api/ocorrenciaPrioridades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaPrioridade)))
                .andExpect(status().isOk());

        // Validate the OcorrenciaPrioridade in the database
        List<OcorrenciaPrioridade> ocorrenciaPrioridades = ocorrenciaPrioridadeRepository.findAll();
        assertThat(ocorrenciaPrioridades).hasSize(databaseSizeBeforeUpdate);
        OcorrenciaPrioridade testOcorrenciaPrioridade = ocorrenciaPrioridades.get(ocorrenciaPrioridades.size() - 1);
        assertThat(testOcorrenciaPrioridade.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void deleteOcorrenciaPrioridade() throws Exception {
        // Initialize the database
        ocorrenciaPrioridadeRepository.saveAndFlush(ocorrenciaPrioridade);

		int databaseSizeBeforeDelete = ocorrenciaPrioridadeRepository.findAll().size();

        // Get the ocorrenciaPrioridade
        restOcorrenciaPrioridadeMockMvc.perform(delete("/api/ocorrenciaPrioridades/{id}", ocorrenciaPrioridade.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OcorrenciaPrioridade> ocorrenciaPrioridades = ocorrenciaPrioridadeRepository.findAll();
        assertThat(ocorrenciaPrioridades).hasSize(databaseSizeBeforeDelete - 1);
    }
}
