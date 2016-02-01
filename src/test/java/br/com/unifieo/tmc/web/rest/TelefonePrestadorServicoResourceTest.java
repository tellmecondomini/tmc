package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.TelefonePrestadorServico;
import br.com.unifieo.tmc.repository.TelefonePrestadorServicoRepository;
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
 * Test class for the TelefonePrestadorServicoResource REST controller.
 *
 * @see TelefonePrestadorServicoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TelefonePrestadorServicoResourceTest {


    private static final Long DEFAULT_NUMERO = 1L;
    private static final Long UPDATED_NUMERO = 2L;

    @Inject
    private TelefonePrestadorServicoRepository telefonePrestadorServicoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTelefonePrestadorServicoMockMvc;

    private TelefonePrestadorServico telefonePrestadorServico;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TelefonePrestadorServicoResource telefonePrestadorServicoResource = new TelefonePrestadorServicoResource();
        ReflectionTestUtils.setField(telefonePrestadorServicoResource, "telefonePrestadorServicoRepository", telefonePrestadorServicoRepository);
        this.restTelefonePrestadorServicoMockMvc = MockMvcBuilders.standaloneSetup(telefonePrestadorServicoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        telefonePrestadorServico = new TelefonePrestadorServico();
        telefonePrestadorServico.setNumero(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createTelefonePrestadorServico() throws Exception {
        int databaseSizeBeforeCreate = telefonePrestadorServicoRepository.findAll().size();

        // Create the TelefonePrestadorServico

        restTelefonePrestadorServicoMockMvc.perform(post("/api/telefonePrestadorServicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefonePrestadorServico)))
            .andExpect(status().isCreated());

        // Validate the TelefonePrestadorServico in the database
        List<TelefonePrestadorServico> telefonePrestadorServicos = telefonePrestadorServicoRepository.findAll();
        assertThat(telefonePrestadorServicos).hasSize(databaseSizeBeforeCreate + 1);
        TelefonePrestadorServico testTelefonePrestadorServico = telefonePrestadorServicos.get(telefonePrestadorServicos.size() - 1);
        assertThat(testTelefonePrestadorServico.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefonePrestadorServicoRepository.findAll().size();
        // set the field null
        telefonePrestadorServico.setNumero(null);

        // Create the TelefonePrestadorServico, which fails.

        restTelefonePrestadorServicoMockMvc.perform(post("/api/telefonePrestadorServicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefonePrestadorServico)))
            .andExpect(status().isBadRequest());

        List<TelefonePrestadorServico> telefonePrestadorServicos = telefonePrestadorServicoRepository.findAll();
        assertThat(telefonePrestadorServicos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTelefonePrestadorServicos() throws Exception {
        // Initialize the database
        telefonePrestadorServicoRepository.saveAndFlush(telefonePrestadorServico);

        // Get all the telefonePrestadorServicos
        restTelefonePrestadorServicoMockMvc.perform(get("/api/telefonePrestadorServicos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telefonePrestadorServico.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @Test
    @Transactional
    public void getTelefonePrestadorServico() throws Exception {
        // Initialize the database
        telefonePrestadorServicoRepository.saveAndFlush(telefonePrestadorServico);

        // Get the telefonePrestadorServico
        restTelefonePrestadorServicoMockMvc.perform(get("/api/telefonePrestadorServicos/{id}", telefonePrestadorServico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(telefonePrestadorServico.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    public void getNonExistingTelefonePrestadorServico() throws Exception {
        // Get the telefonePrestadorServico
        restTelefonePrestadorServicoMockMvc.perform(get("/api/telefonePrestadorServicos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelefonePrestadorServico() throws Exception {
        // Initialize the database
        telefonePrestadorServicoRepository.saveAndFlush(telefonePrestadorServico);

        int databaseSizeBeforeUpdate = telefonePrestadorServicoRepository.findAll().size();

        // Update the telefonePrestadorServico
        telefonePrestadorServico.setNumero(UPDATED_NUMERO);


        restTelefonePrestadorServicoMockMvc.perform(put("/api/telefonePrestadorServicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefonePrestadorServico)))
            .andExpect(status().isOk());

        // Validate the TelefonePrestadorServico in the database
        List<TelefonePrestadorServico> telefonePrestadorServicos = telefonePrestadorServicoRepository.findAll();
        assertThat(telefonePrestadorServicos).hasSize(databaseSizeBeforeUpdate);
        TelefonePrestadorServico testTelefonePrestadorServico = telefonePrestadorServicos.get(telefonePrestadorServicos.size() - 1);
        assertThat(testTelefonePrestadorServico.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void deleteTelefonePrestadorServico() throws Exception {
        // Initialize the database
        telefonePrestadorServicoRepository.saveAndFlush(telefonePrestadorServico);

        int databaseSizeBeforeDelete = telefonePrestadorServicoRepository.findAll().size();

        // Get the telefonePrestadorServico
        restTelefonePrestadorServicoMockMvc.perform(delete("/api/telefonePrestadorServicos/{id}", telefonePrestadorServico.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TelefonePrestadorServico> telefonePrestadorServicos = telefonePrestadorServicoRepository.findAll();
        assertThat(telefonePrestadorServicos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
