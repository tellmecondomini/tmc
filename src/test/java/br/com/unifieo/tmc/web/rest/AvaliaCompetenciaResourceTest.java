package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.AvaliaCompetencia;
import br.com.unifieo.tmc.repository.AvaliaCompetenciaRepository;
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
 * Test class for the AvaliaCompetenciaResource REST controller.
 *
 * @see AvaliaCompetenciaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AvaliaCompetenciaResourceTest {

    private static final Integer DEFAULT_NOTA = 1;
    private static final Integer UPDATED_NOTA = 2;
    private static final String DEFAULT_MENSAGEM = "SAMPLE_TEXT";
    private static final String UPDATED_MENSAGEM = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    @Inject
    private AvaliaCompetenciaRepository avaliaCompetenciaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAvaliaCompetenciaMockMvc;

    private AvaliaCompetencia avaliaCompetencia;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AvaliaCompetenciaResource avaliaCompetenciaResource = new AvaliaCompetenciaResource();
        ReflectionTestUtils.setField(avaliaCompetenciaResource, "avaliaCompetenciaRepository", avaliaCompetenciaRepository);
        this.restAvaliaCompetenciaMockMvc = MockMvcBuilders.standaloneSetup(avaliaCompetenciaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        avaliaCompetencia = new AvaliaCompetencia();
        avaliaCompetencia.setNota(DEFAULT_NOTA);
        avaliaCompetencia.setMensagem(DEFAULT_MENSAGEM);
        avaliaCompetencia.setAtivo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    public void createAvaliaCompetencia() throws Exception {
        int databaseSizeBeforeCreate = avaliaCompetenciaRepository.findAll().size();

        // Create the AvaliaCompetencia

        restAvaliaCompetenciaMockMvc.perform(post("/api/avaliacoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(avaliaCompetencia)))
            .andExpect(status().isCreated());

        // Validate the AvaliaCompetencia in the database
        List<AvaliaCompetencia> avaliaCompetencias = avaliaCompetenciaRepository.findAll();
        assertThat(avaliaCompetencias).hasSize(databaseSizeBeforeCreate + 1);
        AvaliaCompetencia testAvaliaCompetencia = avaliaCompetencias.get(avaliaCompetencias.size() - 1);
        assertThat(testAvaliaCompetencia.getNota()).isEqualTo(DEFAULT_NOTA);
        assertThat(testAvaliaCompetencia.getMensagem()).isEqualTo(DEFAULT_MENSAGEM);
        assertThat(testAvaliaCompetencia.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    public void getAllAvaliaCompetencias() throws Exception {
        // Initialize the database
        avaliaCompetenciaRepository.saveAndFlush(avaliaCompetencia);

        // Get all the avaliacoes
        restAvaliaCompetenciaMockMvc.perform(get("/api/avaliacoes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avaliaCompetencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA)))
            .andExpect(jsonPath("$.[*].mensagem").value(hasItem(DEFAULT_MENSAGEM.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void getAvaliaCompetencia() throws Exception {
        // Initialize the database
        avaliaCompetenciaRepository.saveAndFlush(avaliaCompetencia);

        // Get the avaliaCompetencia
        restAvaliaCompetenciaMockMvc.perform(get("/api/avaliacoes/{id}", avaliaCompetencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(avaliaCompetencia.getId().intValue()))
            .andExpect(jsonPath("$.nota").value(DEFAULT_NOTA))
            .andExpect(jsonPath("$.mensagem").value(DEFAULT_MENSAGEM.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAvaliaCompetencia() throws Exception {
        // Get the avaliaCompetencia
        restAvaliaCompetenciaMockMvc.perform(get("/api/avaliacoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvaliaCompetencia() throws Exception {
        // Initialize the database
        avaliaCompetenciaRepository.saveAndFlush(avaliaCompetencia);

        int databaseSizeBeforeUpdate = avaliaCompetenciaRepository.findAll().size();

        // Update the avaliaCompetencia
        avaliaCompetencia.setNota(UPDATED_NOTA);
        avaliaCompetencia.setMensagem(UPDATED_MENSAGEM);
        avaliaCompetencia.setAtivo(UPDATED_ATIVO);


        restAvaliaCompetenciaMockMvc.perform(put("/api/avaliacoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(avaliaCompetencia)))
            .andExpect(status().isOk());

        // Validate the AvaliaCompetencia in the database
        List<AvaliaCompetencia> avaliaCompetencias = avaliaCompetenciaRepository.findAll();
        assertThat(avaliaCompetencias).hasSize(databaseSizeBeforeUpdate);
        AvaliaCompetencia testAvaliaCompetencia = avaliaCompetencias.get(avaliaCompetencias.size() - 1);
        assertThat(testAvaliaCompetencia.getNota()).isEqualTo(UPDATED_NOTA);
        assertThat(testAvaliaCompetencia.getMensagem()).isEqualTo(UPDATED_MENSAGEM);
        assertThat(testAvaliaCompetencia.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    public void deleteAvaliaCompetencia() throws Exception {
        // Initialize the database
        avaliaCompetenciaRepository.saveAndFlush(avaliaCompetencia);

        int databaseSizeBeforeDelete = avaliaCompetenciaRepository.findAll().size();

        // Get the avaliaCompetencia
        restAvaliaCompetenciaMockMvc.perform(delete("/api/avaliacoes/{id}", avaliaCompetencia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AvaliaCompetencia> avaliaCompetencias = avaliaCompetenciaRepository.findAll();
        assertThat(avaliaCompetencias).hasSize(databaseSizeBeforeDelete - 1);
    }
}
