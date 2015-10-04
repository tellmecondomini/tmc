package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.TelefoneFuncionario;
import br.com.unifieo.tmc.repository.TelefoneFuncionarioRepository;

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
 * Test class for the TelefoneFuncionarioResource REST controller.
 *
 * @see TelefoneFuncionarioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TelefoneFuncionarioResourceTest {


    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    @Inject
    private TelefoneFuncionarioRepository telefoneFuncionarioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTelefoneFuncionarioMockMvc;

    private TelefoneFuncionario telefoneFuncionario;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TelefoneFuncionarioResource telefoneFuncionarioResource = new TelefoneFuncionarioResource();
        ReflectionTestUtils.setField(telefoneFuncionarioResource, "telefoneFuncionarioRepository", telefoneFuncionarioRepository);
        this.restTelefoneFuncionarioMockMvc = MockMvcBuilders.standaloneSetup(telefoneFuncionarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        telefoneFuncionario = new TelefoneFuncionario();
        telefoneFuncionario.setNumero(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createTelefoneFuncionario() throws Exception {
        int databaseSizeBeforeCreate = telefoneFuncionarioRepository.findAll().size();

        // Create the TelefoneFuncionario

        restTelefoneFuncionarioMockMvc.perform(post("/api/telefoneFuncionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefoneFuncionario)))
                .andExpect(status().isCreated());

        // Validate the TelefoneFuncionario in the database
        List<TelefoneFuncionario> telefoneFuncionarios = telefoneFuncionarioRepository.findAll();
        assertThat(telefoneFuncionarios).hasSize(databaseSizeBeforeCreate + 1);
        TelefoneFuncionario testTelefoneFuncionario = telefoneFuncionarios.get(telefoneFuncionarios.size() - 1);
        assertThat(testTelefoneFuncionario.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefoneFuncionarioRepository.findAll().size();
        // set the field null
        telefoneFuncionario.setNumero(null);

        // Create the TelefoneFuncionario, which fails.

        restTelefoneFuncionarioMockMvc.perform(post("/api/telefoneFuncionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefoneFuncionario)))
                .andExpect(status().isBadRequest());

        List<TelefoneFuncionario> telefoneFuncionarios = telefoneFuncionarioRepository.findAll();
        assertThat(telefoneFuncionarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTelefoneFuncionarios() throws Exception {
        // Initialize the database
        telefoneFuncionarioRepository.saveAndFlush(telefoneFuncionario);

        // Get all the telefoneFuncionarios
        restTelefoneFuncionarioMockMvc.perform(get("/api/telefoneFuncionarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(telefoneFuncionario.getId().intValue())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @Test
    @Transactional
    public void getTelefoneFuncionario() throws Exception {
        // Initialize the database
        telefoneFuncionarioRepository.saveAndFlush(telefoneFuncionario);

        // Get the telefoneFuncionario
        restTelefoneFuncionarioMockMvc.perform(get("/api/telefoneFuncionarios/{id}", telefoneFuncionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(telefoneFuncionario.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    public void getNonExistingTelefoneFuncionario() throws Exception {
        // Get the telefoneFuncionario
        restTelefoneFuncionarioMockMvc.perform(get("/api/telefoneFuncionarios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelefoneFuncionario() throws Exception {
        // Initialize the database
        telefoneFuncionarioRepository.saveAndFlush(telefoneFuncionario);

		int databaseSizeBeforeUpdate = telefoneFuncionarioRepository.findAll().size();

        // Update the telefoneFuncionario
        telefoneFuncionario.setNumero(UPDATED_NUMERO);
        

        restTelefoneFuncionarioMockMvc.perform(put("/api/telefoneFuncionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefoneFuncionario)))
                .andExpect(status().isOk());

        // Validate the TelefoneFuncionario in the database
        List<TelefoneFuncionario> telefoneFuncionarios = telefoneFuncionarioRepository.findAll();
        assertThat(telefoneFuncionarios).hasSize(databaseSizeBeforeUpdate);
        TelefoneFuncionario testTelefoneFuncionario = telefoneFuncionarios.get(telefoneFuncionarios.size() - 1);
        assertThat(testTelefoneFuncionario.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void deleteTelefoneFuncionario() throws Exception {
        // Initialize the database
        telefoneFuncionarioRepository.saveAndFlush(telefoneFuncionario);

		int databaseSizeBeforeDelete = telefoneFuncionarioRepository.findAll().size();

        // Get the telefoneFuncionario
        restTelefoneFuncionarioMockMvc.perform(delete("/api/telefoneFuncionarios/{id}", telefoneFuncionario.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TelefoneFuncionario> telefoneFuncionarios = telefoneFuncionarioRepository.findAll();
        assertThat(telefoneFuncionarios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
