package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Assunto;
import br.com.unifieo.tmc.repository.AssuntoRepository;
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
 * Test class for the AssuntoResource REST controller.
 *
 * @see AssuntoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AssuntoResourceTest {

    private static final String DEFAULT_DESCRICAO = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRICAO = "UPDATED_TEXT";

    @Inject
    private AssuntoRepository assuntoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAssuntoMockMvc;

    private Assunto assunto;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssuntoResource assuntoResource = new AssuntoResource();
        ReflectionTestUtils.setField(assuntoResource, "assuntoRepository", assuntoRepository);
        this.restAssuntoMockMvc = MockMvcBuilders.standaloneSetup(assuntoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        assunto = new Assunto();
        assunto.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createAssunto() throws Exception {
        int databaseSizeBeforeCreate = assuntoRepository.findAll().size();

        // Create the Assunto

        restAssuntoMockMvc.perform(post("/api/assuntos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isCreated());

        // Validate the Assunto in the database
        List<Assunto> assuntos = assuntoRepository.findAll();
        assertThat(assuntos).hasSize(databaseSizeBeforeCreate + 1);
        Assunto testAssunto = assuntos.get(assuntos.size() - 1);
        assertThat(testAssunto.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = assuntoRepository.findAll().size();
        // set the field null
        assunto.setDescricao(null);

        // Create the Assunto, which fails.

        restAssuntoMockMvc.perform(post("/api/assuntos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isBadRequest());

        List<Assunto> assuntos = assuntoRepository.findAll();
        assertThat(assuntos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssuntos() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        // Get all the assuntos
        restAssuntoMockMvc.perform(get("/api/assuntos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assunto.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getAssunto() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        // Get the assunto
        restAssuntoMockMvc.perform(get("/api/assuntos/{id}", assunto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(assunto.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAssunto() throws Exception {
        // Get the assunto
        restAssuntoMockMvc.perform(get("/api/assuntos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssunto() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();

        // Update the assunto
        assunto.setDescricao(UPDATED_DESCRICAO);


        restAssuntoMockMvc.perform(put("/api/assuntos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isOk());

        // Validate the Assunto in the database
        List<Assunto> assuntos = assuntoRepository.findAll();
        assertThat(assuntos).hasSize(databaseSizeBeforeUpdate);
        Assunto testAssunto = assuntos.get(assuntos.size() - 1);
        assertThat(testAssunto.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void deleteAssunto() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        int databaseSizeBeforeDelete = assuntoRepository.findAll().size();

        // Get the assunto
        restAssuntoMockMvc.perform(delete("/api/assuntos/{id}", assunto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Assunto> assuntos = assuntoRepository.findAll();
        assertThat(assuntos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
