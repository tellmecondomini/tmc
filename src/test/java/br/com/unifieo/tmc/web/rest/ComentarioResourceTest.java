package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Comentario;
import br.com.unifieo.tmc.repository.ComentarioRepository;

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
 * Test class for the ComentarioResource REST controller.
 *
 * @see ComentarioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ComentarioResourceTest {

    private static final String DEFAULT_CONTEUDO = "SAMPLE_TEXT";
    private static final String UPDATED_CONTEUDO = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_DATA = new LocalDate(0L);
    private static final LocalDate UPDATED_DATA = new LocalDate();

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    @Inject
    private ComentarioRepository comentarioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restComentarioMockMvc;

    private Comentario comentario;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ComentarioResource comentarioResource = new ComentarioResource();
        ReflectionTestUtils.setField(comentarioResource, "comentarioRepository", comentarioRepository);
        this.restComentarioMockMvc = MockMvcBuilders.standaloneSetup(comentarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        comentario = new Comentario();
        comentario.setConteudo(DEFAULT_CONTEUDO);
        comentario.setData(DEFAULT_DATA);
        comentario.setAtivo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    public void createComentario() throws Exception {
        int databaseSizeBeforeCreate = comentarioRepository.findAll().size();

        // Create the Comentario

        restComentarioMockMvc.perform(post("/api/comentarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comentario)))
                .andExpect(status().isCreated());

        // Validate the Comentario in the database
        List<Comentario> comentarios = comentarioRepository.findAll();
        assertThat(comentarios).hasSize(databaseSizeBeforeCreate + 1);
        Comentario testComentario = comentarios.get(comentarios.size() - 1);
        assertThat(testComentario.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
        assertThat(testComentario.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testComentario.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    public void checkConteudoIsRequired() throws Exception {
        int databaseSizeBeforeTest = comentarioRepository.findAll().size();
        // set the field null
        comentario.setConteudo(null);

        // Create the Comentario, which fails.

        restComentarioMockMvc.perform(post("/api/comentarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comentario)))
                .andExpect(status().isBadRequest());

        List<Comentario> comentarios = comentarioRepository.findAll();
        assertThat(comentarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = comentarioRepository.findAll().size();
        // set the field null
        comentario.setData(null);

        // Create the Comentario, which fails.

        restComentarioMockMvc.perform(post("/api/comentarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comentario)))
                .andExpect(status().isBadRequest());

        List<Comentario> comentarios = comentarioRepository.findAll();
        assertThat(comentarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComentarios() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarios
        restComentarioMockMvc.perform(get("/api/comentarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(comentario.getId().intValue())))
                .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO.toString())))
                .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
                .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void getComentario() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get the comentario
        restComentarioMockMvc.perform(get("/api/comentarios/{id}", comentario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(comentario.getId().intValue()))
            .andExpect(jsonPath("$.conteudo").value(DEFAULT_CONTEUDO.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingComentario() throws Exception {
        // Get the comentario
        restComentarioMockMvc.perform(get("/api/comentarios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComentario() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

		int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();

        // Update the comentario
        comentario.setConteudo(UPDATED_CONTEUDO);
        comentario.setData(UPDATED_DATA);
        comentario.setAtivo(UPDATED_ATIVO);
        

        restComentarioMockMvc.perform(put("/api/comentarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comentario)))
                .andExpect(status().isOk());

        // Validate the Comentario in the database
        List<Comentario> comentarios = comentarioRepository.findAll();
        assertThat(comentarios).hasSize(databaseSizeBeforeUpdate);
        Comentario testComentario = comentarios.get(comentarios.size() - 1);
        assertThat(testComentario.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
        assertThat(testComentario.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testComentario.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    public void deleteComentario() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

		int databaseSizeBeforeDelete = comentarioRepository.findAll().size();

        // Get the comentario
        restComentarioMockMvc.perform(delete("/api/comentarios/{id}", comentario.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Comentario> comentarios = comentarioRepository.findAll();
        assertThat(comentarios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
