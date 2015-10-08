package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.TelefoneCondominio;
import br.com.unifieo.tmc.repository.TelefoneCondominioRepository;

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
 * Test class for the TelefoneCondominioResource REST controller.
 *
 * @see TelefoneCondominioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TelefoneCondominioResourceTest {


    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    @Inject
    private TelefoneCondominioRepository telefoneCondominioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTelefoneCondominioMockMvc;

    private TelefoneCondominio telefoneCondominio;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TelefoneCondominioResource telefoneCondominioResource = new TelefoneCondominioResource();
        ReflectionTestUtils.setField(telefoneCondominioResource, "telefoneCondominioRepository", telefoneCondominioRepository);
        this.restTelefoneCondominioMockMvc = MockMvcBuilders.standaloneSetup(telefoneCondominioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        telefoneCondominio = new TelefoneCondominio();
        telefoneCondominio.setNumero(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createTelefoneCondominio() throws Exception {
        int databaseSizeBeforeCreate = telefoneCondominioRepository.findAll().size();

        // Create the TelefoneCondominio

        restTelefoneCondominioMockMvc.perform(post("/api/telefoneCondominios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefoneCondominio)))
                .andExpect(status().isCreated());

        // Validate the TelefoneCondominio in the database
        List<TelefoneCondominio> telefoneCondominios = telefoneCondominioRepository.findAll();
        assertThat(telefoneCondominios).hasSize(databaseSizeBeforeCreate + 1);
        TelefoneCondominio testTelefoneCondominio = telefoneCondominios.get(telefoneCondominios.size() - 1);
        assertThat(testTelefoneCondominio.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefoneCondominioRepository.findAll().size();
        // set the field null
        telefoneCondominio.setNumero(null);

        // Create the TelefoneCondominio, which fails.

        restTelefoneCondominioMockMvc.perform(post("/api/telefoneCondominios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefoneCondominio)))
                .andExpect(status().isBadRequest());

        List<TelefoneCondominio> telefoneCondominios = telefoneCondominioRepository.findAll();
        assertThat(telefoneCondominios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTelefoneCondominios() throws Exception {
        // Initialize the database
        telefoneCondominioRepository.saveAndFlush(telefoneCondominio);

        // Get all the telefoneCondominios
        restTelefoneCondominioMockMvc.perform(get("/api/telefoneCondominios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(telefoneCondominio.getId().intValue())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @Test
    @Transactional
    public void getTelefoneCondominio() throws Exception {
        // Initialize the database
        telefoneCondominioRepository.saveAndFlush(telefoneCondominio);

        // Get the telefoneCondominio
        restTelefoneCondominioMockMvc.perform(get("/api/telefoneCondominios/{id}", telefoneCondominio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(telefoneCondominio.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    public void getNonExistingTelefoneCondominio() throws Exception {
        // Get the telefoneCondominio
        restTelefoneCondominioMockMvc.perform(get("/api/telefoneCondominios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelefoneCondominio() throws Exception {
        // Initialize the database
        telefoneCondominioRepository.saveAndFlush(telefoneCondominio);

		int databaseSizeBeforeUpdate = telefoneCondominioRepository.findAll().size();

        // Update the telefoneCondominio
        telefoneCondominio.setNumero(UPDATED_NUMERO);
        

        restTelefoneCondominioMockMvc.perform(put("/api/telefoneCondominios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefoneCondominio)))
                .andExpect(status().isOk());

        // Validate the TelefoneCondominio in the database
        List<TelefoneCondominio> telefoneCondominios = telefoneCondominioRepository.findAll();
        assertThat(telefoneCondominios).hasSize(databaseSizeBeforeUpdate);
        TelefoneCondominio testTelefoneCondominio = telefoneCondominios.get(telefoneCondominios.size() - 1);
        assertThat(testTelefoneCondominio.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void deleteTelefoneCondominio() throws Exception {
        // Initialize the database
        telefoneCondominioRepository.saveAndFlush(telefoneCondominio);

		int databaseSizeBeforeDelete = telefoneCondominioRepository.findAll().size();

        // Get the telefoneCondominio
        restTelefoneCondominioMockMvc.perform(delete("/api/telefoneCondominios/{id}", telefoneCondominio.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TelefoneCondominio> telefoneCondominios = telefoneCondominioRepository.findAll();
        assertThat(telefoneCondominios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
