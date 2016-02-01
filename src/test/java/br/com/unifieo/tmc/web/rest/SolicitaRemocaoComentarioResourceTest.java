package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.SolicitaRemocaoComentario;
import br.com.unifieo.tmc.repository.SolicitaRemocaoComentarioRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
 * Test class for the SolicitaRemocaoComentarioResource REST controller.
 *
 * @see SolicitaRemocaoComentarioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SolicitaRemocaoComentarioResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_DATA = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATA = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATA_STR = dateTimeFormatter.print(DEFAULT_DATA);
    private static final String DEFAULT_MOTIVO = "SAMPLE_TEXT";
    private static final String UPDATED_MOTIVO = "UPDATED_TEXT";

    private static final DateTime DEFAULT_DATA_ATENDIMENTO = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATA_ATENDIMENTO = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATA_ATENDIMENTO_STR = dateTimeFormatter.print(DEFAULT_DATA_ATENDIMENTO);
    private static final String DEFAULT_OBSERVACAO = "SAMPLE_TEXT";
    private static final String UPDATED_OBSERVACAO = "UPDATED_TEXT";

    @Inject
    private SolicitaRemocaoComentarioRepository solicitaRemocaoComentarioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSolicitaRemocaoComentarioMockMvc;

    private SolicitaRemocaoComentario solicitaRemocaoComentario;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SolicitaRemocaoComentarioResource solicitaRemocaoComentarioResource = new SolicitaRemocaoComentarioResource();
        ReflectionTestUtils.setField(solicitaRemocaoComentarioResource, "solicitaRemocaoComentarioRepository", solicitaRemocaoComentarioRepository);
        this.restSolicitaRemocaoComentarioMockMvc = MockMvcBuilders.standaloneSetup(solicitaRemocaoComentarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        solicitaRemocaoComentario = new SolicitaRemocaoComentario();
        solicitaRemocaoComentario.setData(DEFAULT_DATA);
        solicitaRemocaoComentario.setMotivo(DEFAULT_MOTIVO);
        solicitaRemocaoComentario.setDataAtendimento(DEFAULT_DATA_ATENDIMENTO);
        solicitaRemocaoComentario.setObservacao(DEFAULT_OBSERVACAO);
    }

    @Test
    @Transactional
    public void createSolicitaRemocaoComentario() throws Exception {
        int databaseSizeBeforeCreate = solicitaRemocaoComentarioRepository.findAll().size();

        // Create the SolicitaRemocaoComentario

        restSolicitaRemocaoComentarioMockMvc.perform(post("/api/solicitaRemocaoComentarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitaRemocaoComentario)))
            .andExpect(status().isCreated());

        // Validate the SolicitaRemocaoComentario in the database
        List<SolicitaRemocaoComentario> solicitaRemocaoComentarios = solicitaRemocaoComentarioRepository.findAll();
        assertThat(solicitaRemocaoComentarios).hasSize(databaseSizeBeforeCreate + 1);
        SolicitaRemocaoComentario testSolicitaRemocaoComentario = solicitaRemocaoComentarios.get(solicitaRemocaoComentarios.size() - 1);
        assertThat(testSolicitaRemocaoComentario.getData().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATA);
        assertThat(testSolicitaRemocaoComentario.getMotivo()).isEqualTo(DEFAULT_MOTIVO);
        assertThat(testSolicitaRemocaoComentario.getDataAtendimento().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATA_ATENDIMENTO);
        assertThat(testSolicitaRemocaoComentario.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllSolicitaRemocaoComentarios() throws Exception {
        // Initialize the database
        solicitaRemocaoComentarioRepository.saveAndFlush(solicitaRemocaoComentario);

        // Get all the solicitaRemocaoComentarios
        restSolicitaRemocaoComentarioMockMvc.perform(get("/api/solicitaRemocaoComentarios"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitaRemocaoComentario.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA_STR)))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO.toString())))
            .andExpect(jsonPath("$.[*].dataAtendimento").value(hasItem(DEFAULT_DATA_ATENDIMENTO_STR)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())));
    }

    @Test
    @Transactional
    public void getSolicitaRemocaoComentario() throws Exception {
        // Initialize the database
        solicitaRemocaoComentarioRepository.saveAndFlush(solicitaRemocaoComentario);

        // Get the solicitaRemocaoComentario
        restSolicitaRemocaoComentarioMockMvc.perform(get("/api/solicitaRemocaoComentarios/{id}", solicitaRemocaoComentario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(solicitaRemocaoComentario.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA_STR))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO.toString()))
            .andExpect(jsonPath("$.dataAtendimento").value(DEFAULT_DATA_ATENDIMENTO_STR))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSolicitaRemocaoComentario() throws Exception {
        // Get the solicitaRemocaoComentario
        restSolicitaRemocaoComentarioMockMvc.perform(get("/api/solicitaRemocaoComentarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSolicitaRemocaoComentario() throws Exception {
        // Initialize the database
        solicitaRemocaoComentarioRepository.saveAndFlush(solicitaRemocaoComentario);

        int databaseSizeBeforeUpdate = solicitaRemocaoComentarioRepository.findAll().size();

        // Update the solicitaRemocaoComentario
        solicitaRemocaoComentario.setData(UPDATED_DATA);
        solicitaRemocaoComentario.setMotivo(UPDATED_MOTIVO);
        solicitaRemocaoComentario.setDataAtendimento(UPDATED_DATA_ATENDIMENTO);
        solicitaRemocaoComentario.setObservacao(UPDATED_OBSERVACAO);


        restSolicitaRemocaoComentarioMockMvc.perform(put("/api/solicitaRemocaoComentarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitaRemocaoComentario)))
            .andExpect(status().isOk());

        // Validate the SolicitaRemocaoComentario in the database
        List<SolicitaRemocaoComentario> solicitaRemocaoComentarios = solicitaRemocaoComentarioRepository.findAll();
        assertThat(solicitaRemocaoComentarios).hasSize(databaseSizeBeforeUpdate);
        SolicitaRemocaoComentario testSolicitaRemocaoComentario = solicitaRemocaoComentarios.get(solicitaRemocaoComentarios.size() - 1);
        assertThat(testSolicitaRemocaoComentario.getData().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATA);
        assertThat(testSolicitaRemocaoComentario.getMotivo()).isEqualTo(UPDATED_MOTIVO);
        assertThat(testSolicitaRemocaoComentario.getDataAtendimento().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATA_ATENDIMENTO);
        assertThat(testSolicitaRemocaoComentario.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    public void deleteSolicitaRemocaoComentario() throws Exception {
        // Initialize the database
        solicitaRemocaoComentarioRepository.saveAndFlush(solicitaRemocaoComentario);

        int databaseSizeBeforeDelete = solicitaRemocaoComentarioRepository.findAll().size();

        // Get the solicitaRemocaoComentario
        restSolicitaRemocaoComentarioMockMvc.perform(delete("/api/solicitaRemocaoComentarios/{id}", solicitaRemocaoComentario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SolicitaRemocaoComentario> solicitaRemocaoComentarios = solicitaRemocaoComentarioRepository.findAll();
        assertThat(solicitaRemocaoComentarios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
