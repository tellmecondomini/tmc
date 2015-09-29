package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Ocorrencia;
import br.com.unifieo.tmc.domain.enumeration.StatusSolicitacao;
import br.com.unifieo.tmc.repository.OcorrenciaRepository;
import org.joda.time.LocalDate;
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
 * Test class for the OcorrenciaResource REST controller.
 *
 * @see OcorrenciaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OcorrenciaResourceTest {


    private static final LocalDate DEFAULT_DATA_ABERTURA = new LocalDate(0L);
    private static final LocalDate UPDATED_DATA_ABERTURA = new LocalDate();

    private static final LocalDate DEFAULT_DATA_FECHAMENTO = new LocalDate(0L);
    private static final LocalDate UPDATED_DATA_FECHAMENTO = new LocalDate();
    private static final String DEFAULT_MENSSAGEM = "SAMPLE_TEXT";
    private static final String UPDATED_MENSSAGEM = "UPDATED_TEXT";

    private static final StatusSolicitacao DEFAULT_STATUS = StatusSolicitacao.PENDENTE;
    private static final StatusSolicitacao UPDATED_STATUS = StatusSolicitacao.EM_ANALISE;

    @Inject
    private OcorrenciaRepository ocorrenciaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOcorrenciaMockMvc;

    private Ocorrencia ocorrencia;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OcorrenciaResource ocorrenciaResource = new OcorrenciaResource();
        ReflectionTestUtils.setField(ocorrenciaResource, "ocorrenciaRepository", ocorrenciaRepository);
        this.restOcorrenciaMockMvc = MockMvcBuilders.standaloneSetup(ocorrenciaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ocorrencia = new Ocorrencia();
        ocorrencia.setDataAbertura(DEFAULT_DATA_ABERTURA);
        ocorrencia.setDataFechamento(DEFAULT_DATA_FECHAMENTO);
        ocorrencia.setMenssagem(DEFAULT_MENSSAGEM);
        ocorrencia.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createOcorrencia() throws Exception {
        int databaseSizeBeforeCreate = ocorrenciaRepository.findAll().size();

        // Create the Ocorrencia

        restOcorrenciaMockMvc.perform(post("/api/ocorrencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrencia)))
                .andExpect(status().isCreated());

        // Validate the Ocorrencia in the database
        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAll();
        assertThat(ocorrencias).hasSize(databaseSizeBeforeCreate + 1);
        Ocorrencia testOcorrencia = ocorrencias.get(ocorrencias.size() - 1);
        assertThat(testOcorrencia.getDataAbertura()).isEqualTo(DEFAULT_DATA_ABERTURA);
        assertThat(testOcorrencia.getDataFechamento()).isEqualTo(DEFAULT_DATA_FECHAMENTO);
        assertThat(testOcorrencia.getMenssagem()).isEqualTo(DEFAULT_MENSSAGEM);
        assertThat(testOcorrencia.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkDataAberturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ocorrenciaRepository.findAll().size();
        // set the field null
        ocorrencia.setDataAbertura(null);

        // Create the Ocorrencia, which fails.

        restOcorrenciaMockMvc.perform(post("/api/ocorrencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrencia)))
                .andExpect(status().isBadRequest());

        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAll();
        assertThat(ocorrencias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataFechamentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ocorrenciaRepository.findAll().size();
        // set the field null
        ocorrencia.setDataFechamento(null);

        // Create the Ocorrencia, which fails.

        restOcorrenciaMockMvc.perform(post("/api/ocorrencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrencia)))
                .andExpect(status().isBadRequest());

        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAll();
        assertThat(ocorrencias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMenssagemIsRequired() throws Exception {
        int databaseSizeBeforeTest = ocorrenciaRepository.findAll().size();
        // set the field null
        ocorrencia.setMenssagem(null);

        // Create the Ocorrencia, which fails.

        restOcorrenciaMockMvc.perform(post("/api/ocorrencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrencia)))
                .andExpect(status().isBadRequest());

        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAll();
        assertThat(ocorrencias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = ocorrenciaRepository.findAll().size();
        // set the field null
        ocorrencia.setStatus(null);

        // Create the Ocorrencia, which fails.

        restOcorrenciaMockMvc.perform(post("/api/ocorrencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrencia)))
                .andExpect(status().isBadRequest());

        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAll();
        assertThat(ocorrencias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOcorrencias() throws Exception {
        // Initialize the database
        ocorrenciaRepository.saveAndFlush(ocorrencia);

        // Get all the ocorrencias
        restOcorrenciaMockMvc.perform(get("/api/ocorrencias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ocorrencia.getId().intValue())))
                .andExpect(jsonPath("$.[*].dataAbertura").value(hasItem(DEFAULT_DATA_ABERTURA.toString())))
                .andExpect(jsonPath("$.[*].dataFechamento").value(hasItem(DEFAULT_DATA_FECHAMENTO.toString())))
                .andExpect(jsonPath("$.[*].menssagem").value(hasItem(DEFAULT_MENSSAGEM.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getOcorrencia() throws Exception {
        // Initialize the database
        ocorrenciaRepository.saveAndFlush(ocorrencia);

        // Get the ocorrencia
        restOcorrenciaMockMvc.perform(get("/api/ocorrencias/{id}", ocorrencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ocorrencia.getId().intValue()))
            .andExpect(jsonPath("$.dataAbertura").value(DEFAULT_DATA_ABERTURA.toString()))
            .andExpect(jsonPath("$.dataFechamento").value(DEFAULT_DATA_FECHAMENTO.toString()))
            .andExpect(jsonPath("$.menssagem").value(DEFAULT_MENSSAGEM.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOcorrencia() throws Exception {
        // Get the ocorrencia
        restOcorrenciaMockMvc.perform(get("/api/ocorrencias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOcorrencia() throws Exception {
        // Initialize the database
        ocorrenciaRepository.saveAndFlush(ocorrencia);

		int databaseSizeBeforeUpdate = ocorrenciaRepository.findAll().size();

        // Update the ocorrencia
        ocorrencia.setDataAbertura(UPDATED_DATA_ABERTURA);
        ocorrencia.setDataFechamento(UPDATED_DATA_FECHAMENTO);
        ocorrencia.setMenssagem(UPDATED_MENSSAGEM);
        ocorrencia.setStatus(UPDATED_STATUS);


        restOcorrenciaMockMvc.perform(put("/api/ocorrencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrencia)))
                .andExpect(status().isOk());

        // Validate the Ocorrencia in the database
        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAll();
        assertThat(ocorrencias).hasSize(databaseSizeBeforeUpdate);
        Ocorrencia testOcorrencia = ocorrencias.get(ocorrencias.size() - 1);
        assertThat(testOcorrencia.getDataAbertura()).isEqualTo(UPDATED_DATA_ABERTURA);
        assertThat(testOcorrencia.getDataFechamento()).isEqualTo(UPDATED_DATA_FECHAMENTO);
        assertThat(testOcorrencia.getMenssagem()).isEqualTo(UPDATED_MENSSAGEM);
        assertThat(testOcorrencia.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteOcorrencia() throws Exception {
        // Initialize the database
        ocorrenciaRepository.saveAndFlush(ocorrencia);

		int databaseSizeBeforeDelete = ocorrenciaRepository.findAll().size();

        // Get the ocorrencia
        restOcorrenciaMockMvc.perform(delete("/api/ocorrencias/{id}", ocorrencia.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAll();
        assertThat(ocorrencias).hasSize(databaseSizeBeforeDelete - 1);
    }
}
