package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.DisponibilidadeDependencia;
import br.com.unifieo.tmc.repository.DisponibilidadeDependenciaRepository;

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
 * Test class for the DisponibilidadeDependenciaResource REST controller.
 *
 * @see DisponibilidadeDependenciaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DisponibilidadeDependenciaResourceTest {


    private static final Integer DEFAULT_DIA_SEMANA = 1;
    private static final Integer UPDATED_DIA_SEMANA = 2;
    private static final String DEFAULT_HORA_INICIO = "SAMPLE_TEXT";
    private static final String UPDATED_HORA_INICIO = "UPDATED_TEXT";
    private static final String DEFAULT_HORA_FIM = "SAMPLE_TEXT";
    private static final String UPDATED_HORA_FIM = "UPDATED_TEXT";

    @Inject
    private DisponibilidadeDependenciaRepository disponibilidadeDependenciaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDisponibilidadeDependenciaMockMvc;

    private DisponibilidadeDependencia disponibilidadeDependencia;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DisponibilidadeDependenciaResource disponibilidadeDependenciaResource = new DisponibilidadeDependenciaResource();
        ReflectionTestUtils.setField(disponibilidadeDependenciaResource, "disponibilidadeDependenciaRepository", disponibilidadeDependenciaRepository);
        this.restDisponibilidadeDependenciaMockMvc = MockMvcBuilders.standaloneSetup(disponibilidadeDependenciaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        disponibilidadeDependencia = new DisponibilidadeDependencia();
        disponibilidadeDependencia.setDiaSemana(DEFAULT_DIA_SEMANA);
        disponibilidadeDependencia.setHoraInicio(DEFAULT_HORA_INICIO);
        disponibilidadeDependencia.setHoraFim(DEFAULT_HORA_FIM);
    }

    @Test
    @Transactional
    public void createDisponibilidadeDependencia() throws Exception {
        int databaseSizeBeforeCreate = disponibilidadeDependenciaRepository.findAll().size();

        // Create the DisponibilidadeDependencia

        restDisponibilidadeDependenciaMockMvc.perform(post("/api/disponibilidadeDependencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disponibilidadeDependencia)))
                .andExpect(status().isCreated());

        // Validate the DisponibilidadeDependencia in the database
        List<DisponibilidadeDependencia> disponibilidadeDependencias = disponibilidadeDependenciaRepository.findAll();
        assertThat(disponibilidadeDependencias).hasSize(databaseSizeBeforeCreate + 1);
        DisponibilidadeDependencia testDisponibilidadeDependencia = disponibilidadeDependencias.get(disponibilidadeDependencias.size() - 1);
        assertThat(testDisponibilidadeDependencia.getDiaSemana()).isEqualTo(DEFAULT_DIA_SEMANA);
        assertThat(testDisponibilidadeDependencia.getHoraInicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testDisponibilidadeDependencia.getHoraFim()).isEqualTo(DEFAULT_HORA_FIM);
    }

    @Test
    @Transactional
    public void checkDiaSemanaIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibilidadeDependenciaRepository.findAll().size();
        // set the field null
        disponibilidadeDependencia.setDiaSemana(null);

        // Create the DisponibilidadeDependencia, which fails.

        restDisponibilidadeDependenciaMockMvc.perform(post("/api/disponibilidadeDependencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disponibilidadeDependencia)))
                .andExpect(status().isBadRequest());

        List<DisponibilidadeDependencia> disponibilidadeDependencias = disponibilidadeDependenciaRepository.findAll();
        assertThat(disponibilidadeDependencias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHoraInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibilidadeDependenciaRepository.findAll().size();
        // set the field null
        disponibilidadeDependencia.setHoraInicio(null);

        // Create the DisponibilidadeDependencia, which fails.

        restDisponibilidadeDependenciaMockMvc.perform(post("/api/disponibilidadeDependencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disponibilidadeDependencia)))
                .andExpect(status().isBadRequest());

        List<DisponibilidadeDependencia> disponibilidadeDependencias = disponibilidadeDependenciaRepository.findAll();
        assertThat(disponibilidadeDependencias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHoraFimIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibilidadeDependenciaRepository.findAll().size();
        // set the field null
        disponibilidadeDependencia.setHoraFim(null);

        // Create the DisponibilidadeDependencia, which fails.

        restDisponibilidadeDependenciaMockMvc.perform(post("/api/disponibilidadeDependencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disponibilidadeDependencia)))
                .andExpect(status().isBadRequest());

        List<DisponibilidadeDependencia> disponibilidadeDependencias = disponibilidadeDependenciaRepository.findAll();
        assertThat(disponibilidadeDependencias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDisponibilidadeDependencias() throws Exception {
        // Initialize the database
        disponibilidadeDependenciaRepository.saveAndFlush(disponibilidadeDependencia);

        // Get all the disponibilidadeDependencias
        restDisponibilidadeDependenciaMockMvc.perform(get("/api/disponibilidadeDependencias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilidadeDependencia.getId().intValue())))
                .andExpect(jsonPath("$.[*].diaSemana").value(hasItem(DEFAULT_DIA_SEMANA)))
                .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO.toString())))
                .andExpect(jsonPath("$.[*].horaFim").value(hasItem(DEFAULT_HORA_FIM.toString())));
    }

    @Test
    @Transactional
    public void getDisponibilidadeDependencia() throws Exception {
        // Initialize the database
        disponibilidadeDependenciaRepository.saveAndFlush(disponibilidadeDependencia);

        // Get the disponibilidadeDependencia
        restDisponibilidadeDependenciaMockMvc.perform(get("/api/disponibilidadeDependencias/{id}", disponibilidadeDependencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(disponibilidadeDependencia.getId().intValue()))
            .andExpect(jsonPath("$.diaSemana").value(DEFAULT_DIA_SEMANA))
            .andExpect(jsonPath("$.horaInicio").value(DEFAULT_HORA_INICIO.toString()))
            .andExpect(jsonPath("$.horaFim").value(DEFAULT_HORA_FIM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDisponibilidadeDependencia() throws Exception {
        // Get the disponibilidadeDependencia
        restDisponibilidadeDependenciaMockMvc.perform(get("/api/disponibilidadeDependencias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDisponibilidadeDependencia() throws Exception {
        // Initialize the database
        disponibilidadeDependenciaRepository.saveAndFlush(disponibilidadeDependencia);

		int databaseSizeBeforeUpdate = disponibilidadeDependenciaRepository.findAll().size();

        // Update the disponibilidadeDependencia
        disponibilidadeDependencia.setDiaSemana(UPDATED_DIA_SEMANA);
        disponibilidadeDependencia.setHoraInicio(UPDATED_HORA_INICIO);
        disponibilidadeDependencia.setHoraFim(UPDATED_HORA_FIM);
        

        restDisponibilidadeDependenciaMockMvc.perform(put("/api/disponibilidadeDependencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disponibilidadeDependencia)))
                .andExpect(status().isOk());

        // Validate the DisponibilidadeDependencia in the database
        List<DisponibilidadeDependencia> disponibilidadeDependencias = disponibilidadeDependenciaRepository.findAll();
        assertThat(disponibilidadeDependencias).hasSize(databaseSizeBeforeUpdate);
        DisponibilidadeDependencia testDisponibilidadeDependencia = disponibilidadeDependencias.get(disponibilidadeDependencias.size() - 1);
        assertThat(testDisponibilidadeDependencia.getDiaSemana()).isEqualTo(UPDATED_DIA_SEMANA);
        assertThat(testDisponibilidadeDependencia.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testDisponibilidadeDependencia.getHoraFim()).isEqualTo(UPDATED_HORA_FIM);
    }

    @Test
    @Transactional
    public void deleteDisponibilidadeDependencia() throws Exception {
        // Initialize the database
        disponibilidadeDependenciaRepository.saveAndFlush(disponibilidadeDependencia);

		int databaseSizeBeforeDelete = disponibilidadeDependenciaRepository.findAll().size();

        // Get the disponibilidadeDependencia
        restDisponibilidadeDependenciaMockMvc.perform(delete("/api/disponibilidadeDependencias/{id}", disponibilidadeDependencia.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DisponibilidadeDependencia> disponibilidadeDependencias = disponibilidadeDependenciaRepository.findAll();
        assertThat(disponibilidadeDependencias).hasSize(databaseSizeBeforeDelete - 1);
    }
}
