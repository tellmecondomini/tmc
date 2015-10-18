package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Topico;
import br.com.unifieo.tmc.repository.TopicoRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TopicoResource REST controller.
 *
 * @see TopicoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TopicoResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_CONTEUDO = "SAMPLE_TEXT";
    private static final String UPDATED_CONTEUDO = "UPDATED_TEXT";

    private static final DateTime DEFAULT_DATA = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATA = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATA_STR = dateTimeFormatter.print(DEFAULT_DATA);

    private static final Boolean DEFAULT_APROVADO = false;
    private static final Boolean UPDATED_APROVADO = true;

    @Inject
    private TopicoRepository topicoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTopicoMockMvc;

    private Topico topico;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TopicoResource topicoResource = new TopicoResource();
        ReflectionTestUtils.setField(topicoResource, "topicoRepository", topicoRepository);
        this.restTopicoMockMvc = MockMvcBuilders.standaloneSetup(topicoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        topico = new Topico();
        topico.setConteudo(DEFAULT_CONTEUDO);
        topico.setData(DEFAULT_DATA);
        topico.setAprovado(DEFAULT_APROVADO);
    }

    @Test
    @Transactional
    public void createTopico() throws Exception {
        int databaseSizeBeforeCreate = topicoRepository.findAll().size();

        // Create the Topico

        restTopicoMockMvc.perform(post("/api/topicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(topico)))
                .andExpect(status().isCreated());

        // Validate the Topico in the database
        List<Topico> topicos = topicoRepository.findAll();
        assertThat(topicos).hasSize(databaseSizeBeforeCreate + 1);
        Topico testTopico = topicos.get(topicos.size() - 1);
        assertThat(testTopico.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
        assertThat(testTopico.getData().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATA);
        assertThat(testTopico.getAprovado()).isEqualTo(DEFAULT_APROVADO);
    }

    @Test
    @Transactional
    public void checkConteudoIsRequired() throws Exception {
        int databaseSizeBeforeTest = topicoRepository.findAll().size();
        // set the field null
        topico.setConteudo(null);

        // Create the Topico, which fails.

        restTopicoMockMvc.perform(post("/api/topicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(topico)))
                .andExpect(status().isBadRequest());

        List<Topico> topicos = topicoRepository.findAll();
        assertThat(topicos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = topicoRepository.findAll().size();
        // set the field null
        topico.setData(null);

        // Create the Topico, which fails.

        restTopicoMockMvc.perform(post("/api/topicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(topico)))
                .andExpect(status().isBadRequest());

        List<Topico> topicos = topicoRepository.findAll();
        assertThat(topicos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTopicos() throws Exception {
        // Initialize the database
        topicoRepository.saveAndFlush(topico);

        // Get all the topicos
        restTopicoMockMvc.perform(get("/api/topicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(topico.getId().intValue())))
                .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO.toString())))
                .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA_STR)))
                .andExpect(jsonPath("$.[*].aprovado").value(hasItem(DEFAULT_APROVADO.booleanValue())));
    }

    @Test
    @Transactional
    public void getTopico() throws Exception {
        // Initialize the database
        topicoRepository.saveAndFlush(topico);

        // Get the topico
        restTopicoMockMvc.perform(get("/api/topicos/{id}", topico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(topico.getId().intValue()))
            .andExpect(jsonPath("$.conteudo").value(DEFAULT_CONTEUDO.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA_STR))
            .andExpect(jsonPath("$.aprovado").value(DEFAULT_APROVADO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTopico() throws Exception {
        // Get the topico
        restTopicoMockMvc.perform(get("/api/topicos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTopico() throws Exception {
        // Initialize the database
        topicoRepository.saveAndFlush(topico);

		int databaseSizeBeforeUpdate = topicoRepository.findAll().size();

        // Update the topico
        topico.setConteudo(UPDATED_CONTEUDO);
        topico.setData(UPDATED_DATA);
        topico.setAprovado(UPDATED_APROVADO);
        

        restTopicoMockMvc.perform(put("/api/topicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(topico)))
                .andExpect(status().isOk());

        // Validate the Topico in the database
        List<Topico> topicos = topicoRepository.findAll();
        assertThat(topicos).hasSize(databaseSizeBeforeUpdate);
        Topico testTopico = topicos.get(topicos.size() - 1);
        assertThat(testTopico.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
        assertThat(testTopico.getData().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATA);
        assertThat(testTopico.getAprovado()).isEqualTo(UPDATED_APROVADO);
    }

    @Test
    @Transactional
    public void deleteTopico() throws Exception {
        // Initialize the database
        topicoRepository.saveAndFlush(topico);

		int databaseSizeBeforeDelete = topicoRepository.findAll().size();

        // Get the topico
        restTopicoMockMvc.perform(delete("/api/topicos/{id}", topico.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Topico> topicos = topicoRepository.findAll();
        assertThat(topicos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
