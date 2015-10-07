package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Agenda;
import br.com.unifieo.tmc.repository.AgendaRepository;

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
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AgendaResource REST controller.
 *
 * @see AgendaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AgendaResourceTest {


    private static final LocalDate DEFAULT_DATA = new LocalDate(0L);
    private static final LocalDate UPDATED_DATA = new LocalDate();
    private static final String DEFAULT_HORA_INICIO = "SAMPLE_TEXT";
    private static final String UPDATED_HORA_INICIO = "UPDATED_TEXT";
    private static final String DEFAULT_HORA_FIM = "SAMPLE_TEXT";
    private static final String UPDATED_HORA_FIM = "UPDATED_TEXT";

    @Inject
    private AgendaRepository agendaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAgendaMockMvc;

    private Agenda agenda;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgendaResource agendaResource = new AgendaResource();
        ReflectionTestUtils.setField(agendaResource, "agendaRepository", agendaRepository);
        this.restAgendaMockMvc = MockMvcBuilders.standaloneSetup(agendaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        agenda = new Agenda();
        agenda.setData(DEFAULT_DATA);
        agenda.setHoraInicio(DEFAULT_HORA_INICIO);
        agenda.setHoraFim(DEFAULT_HORA_FIM);
    }

    @Test
    @Transactional
    public void createAgenda() throws Exception {
        int databaseSizeBeforeCreate = agendaRepository.findAll().size();

        // Create the Agenda

        restAgendaMockMvc.perform(post("/api/agendas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agenda)))
                .andExpect(status().isCreated());

        // Validate the Agenda in the database
        List<Agenda> agendas = agendaRepository.findAll();
        assertThat(agendas).hasSize(databaseSizeBeforeCreate + 1);
        Agenda testAgenda = agendas.get(agendas.size() - 1);
        assertThat(testAgenda.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testAgenda.getHoraInicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testAgenda.getHoraFim()).isEqualTo(DEFAULT_HORA_FIM);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = agendaRepository.findAll().size();
        // set the field null
        agenda.setData(null);

        // Create the Agenda, which fails.

        restAgendaMockMvc.perform(post("/api/agendas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agenda)))
                .andExpect(status().isBadRequest());

        List<Agenda> agendas = agendaRepository.findAll();
        assertThat(agendas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHoraInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = agendaRepository.findAll().size();
        // set the field null
        agenda.setHoraInicio(null);

        // Create the Agenda, which fails.

        restAgendaMockMvc.perform(post("/api/agendas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agenda)))
                .andExpect(status().isBadRequest());

        List<Agenda> agendas = agendaRepository.findAll();
        assertThat(agendas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHoraFimIsRequired() throws Exception {
        int databaseSizeBeforeTest = agendaRepository.findAll().size();
        // set the field null
        agenda.setHoraFim(null);

        // Create the Agenda, which fails.

        restAgendaMockMvc.perform(post("/api/agendas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agenda)))
                .andExpect(status().isBadRequest());

        List<Agenda> agendas = agendaRepository.findAll();
        assertThat(agendas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAgendas() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendas
        restAgendaMockMvc.perform(get("/api/agendas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(agenda.getId().intValue())))
                .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
                .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO.toString())))
                .andExpect(jsonPath("$.[*].horaFim").value(hasItem(DEFAULT_HORA_FIM.toString())));
    }

    @Test
    @Transactional
    public void getAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get the agenda
        restAgendaMockMvc.perform(get("/api/agendas/{id}", agenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(agenda.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.horaInicio").value(DEFAULT_HORA_INICIO.toString()))
            .andExpect(jsonPath("$.horaFim").value(DEFAULT_HORA_FIM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAgenda() throws Exception {
        // Get the agenda
        restAgendaMockMvc.perform(get("/api/agendas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

		int databaseSizeBeforeUpdate = agendaRepository.findAll().size();

        // Update the agenda
        agenda.setData(UPDATED_DATA);
        agenda.setHoraInicio(UPDATED_HORA_INICIO);
        agenda.setHoraFim(UPDATED_HORA_FIM);
        

        restAgendaMockMvc.perform(put("/api/agendas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agenda)))
                .andExpect(status().isOk());

        // Validate the Agenda in the database
        List<Agenda> agendas = agendaRepository.findAll();
        assertThat(agendas).hasSize(databaseSizeBeforeUpdate);
        Agenda testAgenda = agendas.get(agendas.size() - 1);
        assertThat(testAgenda.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testAgenda.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testAgenda.getHoraFim()).isEqualTo(UPDATED_HORA_FIM);
    }

    @Test
    @Transactional
    public void deleteAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

		int databaseSizeBeforeDelete = agendaRepository.findAll().size();

        // Get the agenda
        restAgendaMockMvc.perform(delete("/api/agendas/{id}", agenda.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Agenda> agendas = agendaRepository.findAll();
        assertThat(agendas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
