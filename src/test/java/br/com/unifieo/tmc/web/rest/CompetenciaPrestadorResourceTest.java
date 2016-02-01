package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.CompetenciaPrestador;
import br.com.unifieo.tmc.repository.CompetenciaPrestadorRepository;
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
 * Test class for the CompetenciaPrestadorResource REST controller.
 *
 * @see CompetenciaPrestadorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CompetenciaPrestadorResourceTest {

    private static final String DEFAULT_DESCRICAO = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRICAO = "UPDATED_TEXT";

    @Inject
    private CompetenciaPrestadorRepository competenciaPrestadorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompetenciaPrestadorMockMvc;

    private CompetenciaPrestador competenciaPrestador;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompetenciaPrestadorResource competenciaPrestadorResource = new CompetenciaPrestadorResource();
        ReflectionTestUtils.setField(competenciaPrestadorResource, "competenciaPrestadorRepository", competenciaPrestadorRepository);
        this.restCompetenciaPrestadorMockMvc = MockMvcBuilders.standaloneSetup(competenciaPrestadorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        competenciaPrestador = new CompetenciaPrestador();
        competenciaPrestador.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createCompetenciaPrestador() throws Exception {
        int databaseSizeBeforeCreate = competenciaPrestadorRepository.findAll().size();

        // Create the CompetenciaPrestador

        restCompetenciaPrestadorMockMvc.perform(post("/api/competenciaPrestadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competenciaPrestador)))
            .andExpect(status().isCreated());

        // Validate the CompetenciaPrestador in the database
        List<CompetenciaPrestador> competenciaPrestadors = competenciaPrestadorRepository.findAll();
        assertThat(competenciaPrestadors).hasSize(databaseSizeBeforeCreate + 1);
        CompetenciaPrestador testCompetenciaPrestador = competenciaPrestadors.get(competenciaPrestadors.size() - 1);
        assertThat(testCompetenciaPrestador.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = competenciaPrestadorRepository.findAll().size();
        // set the field null
        competenciaPrestador.setDescricao(null);

        // Create the CompetenciaPrestador, which fails.

        restCompetenciaPrestadorMockMvc.perform(post("/api/competenciaPrestadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competenciaPrestador)))
            .andExpect(status().isBadRequest());

        List<CompetenciaPrestador> competenciaPrestadors = competenciaPrestadorRepository.findAll();
        assertThat(competenciaPrestadors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompetenciaPrestadors() throws Exception {
        // Initialize the database
        competenciaPrestadorRepository.saveAndFlush(competenciaPrestador);

        // Get all the competenciaPrestadors
        restCompetenciaPrestadorMockMvc.perform(get("/api/competenciaPrestadors"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competenciaPrestador.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getCompetenciaPrestador() throws Exception {
        // Initialize the database
        competenciaPrestadorRepository.saveAndFlush(competenciaPrestador);

        // Get the competenciaPrestador
        restCompetenciaPrestadorMockMvc.perform(get("/api/competenciaPrestadors/{id}", competenciaPrestador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(competenciaPrestador.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompetenciaPrestador() throws Exception {
        // Get the competenciaPrestador
        restCompetenciaPrestadorMockMvc.perform(get("/api/competenciaPrestadors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetenciaPrestador() throws Exception {
        // Initialize the database
        competenciaPrestadorRepository.saveAndFlush(competenciaPrestador);

        int databaseSizeBeforeUpdate = competenciaPrestadorRepository.findAll().size();

        // Update the competenciaPrestador
        competenciaPrestador.setDescricao(UPDATED_DESCRICAO);


        restCompetenciaPrestadorMockMvc.perform(put("/api/competenciaPrestadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competenciaPrestador)))
            .andExpect(status().isOk());

        // Validate the CompetenciaPrestador in the database
        List<CompetenciaPrestador> competenciaPrestadors = competenciaPrestadorRepository.findAll();
        assertThat(competenciaPrestadors).hasSize(databaseSizeBeforeUpdate);
        CompetenciaPrestador testCompetenciaPrestador = competenciaPrestadors.get(competenciaPrestadors.size() - 1);
        assertThat(testCompetenciaPrestador.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void deleteCompetenciaPrestador() throws Exception {
        // Initialize the database
        competenciaPrestadorRepository.saveAndFlush(competenciaPrestador);

        int databaseSizeBeforeDelete = competenciaPrestadorRepository.findAll().size();

        // Get the competenciaPrestador
        restCompetenciaPrestadorMockMvc.perform(delete("/api/competenciaPrestadors/{id}", competenciaPrestador.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompetenciaPrestador> competenciaPrestadors = competenciaPrestadorRepository.findAll();
        assertThat(competenciaPrestadors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
